package com.atchihaya.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.atchihaya.pojo.*;
import com.atchihaya.util.EmlBasicTest;
import com.atchihaya.util.JwtHelper;
import com.atchihaya.util.Result;
import com.atchihaya.util.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atchihaya.service.MailService;
import com.atchihaya.mapper.MailMapper;
import jakarta.mail.*;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author chihaya
 * @description 针对表【mail】的数据库操作Service实现
 * @createDate 2024-03-11 23:54:51
 */
@Service
@Slf4j
public class MailServiceImpl extends ServiceImpl<MailMapper, Mail>
        implements MailService {


    @Autowired
    private JavaMailSenderImpl mailSender;//注入邮件工具类
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private MailMapper mailMapper;

    /**
     * 发送邮件
     *
     * @param mail
     * @return
     */
    @Override
    public Result sendMail(Mail mail, MailUser mailUser) throws MessagingException {
        /**
         * 邮件检验
         */
        if (StringUtils.isEmpty(mail.getMailTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mail.getMailSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mail.getMailText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
        /**
         * 邮件发送
         */
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);//true表示支持复杂类型
        messageHelper.setFrom(mail.getMailFrom());//邮件发信人
        messageHelper.setTo(mail.getMailTo().split(","));//邮件收信人
        messageHelper.setSubject(mail.getMailSubject());//邮件主题
        messageHelper.setText(mail.getMailText());//邮件内容
        if (!StringUtils.isEmpty(mail.getMailCc())) {//抄送
            messageHelper.setCc(mail.getMailCc().split(","));
        }
        if (!StringUtils.isEmpty(mail.getMailBcc())) {//密送
            messageHelper.setCc(mail.getMailBcc().split(","));
        }
        if (mail.getMultipartFiles() != null) {//添加邮件附件
            for (MultipartFile multipartFile : mail.getMultipartFiles()) {
                messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
            }
        }
        //发送时间
        mail.setSentDate(new Date());
        messageHelper.setSentDate(mail.getSentDate());

        mailSender.send(messageHelper.getMimeMessage());//正式发送邮件
        mail.setStatus("ok");
        log.info("发送邮件成功：{}->{}", mail.getMailFrom(), mail.getMailTo());
        /**
         * 邮件保存到数据库
         */
        mail.setMailFromName(mailUser.getUsername());
        mail.setMailToName(mail.getMailTo().substring(0, mail.getMailTo().indexOf("@")));
        mailMapper.insert(mail);
        return Result.ok(null);
    }

    @Override
    public Result mailLoad(PortalVo portalVo) {
        if (portalVo.getUsername() == null) {
            return Result.build(null, ResultCodeEnum.NOTLOGIN);
        }
        LambdaQueryWrapper<Mail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mail::getMailFromName, portalVo.getUsername());
        //分页参数
        IPage<Mail> page = new Page<>(portalVo.getPageNum(), portalVo.getPageSize());
        mailMapper.selectMailPage(page, portalVo);
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("pageData", page.getRecords());
        pageInfo.put("pageNum", page.getCurrent());
        pageInfo.put("pageSize", page.getSize());
        pageInfo.put("totalPage", page.getPages());
        pageInfo.put("totalSize", page.getTotal());
        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("pageInfo", pageInfo);
        return Result.ok(pageInfoMap);
    }

    @Override
    public Result showMailDetail(Integer id) {
        Mail mail = mailMapper.selectById(id);
        Map<String, Object> data = new HashMap<>();
        data.put("mail", mail);
        return Result.ok(data);
    }

    @Override
    public Result receiveMail(Inbox inbox) throws MessagingException {
        //创建链接会话的配置类
        Properties props = new Properties();
        if (inbox.getProtocol().equals(("imap").toLowerCase())) {
            props.setProperty("mail.store.protocol", "imap");    // 设置协议为 IMAP
            props.setProperty("mail.imap.port", "993");          // 设置 IMAP 端口号，通常为 993
            props.setProperty("mail.imap.host", "imap.qq.com");  // 设置 IMAP 服务器主机名
            // 使用 SSL 连接,仅在imap协议时需要
            props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.imap.socketFactory.fallback", "false");
        } else if (inbox.getProtocol().equals(("pop3").toLowerCase())) {
            props.setProperty("mail.store.protocol", "pop3");        // 协议
            props.setProperty("mail.pop3.port", "110");                // 端口
            props.setProperty("mail.pop3.host", "pop.qq.com");    // pop3服务器
        }

        // 创建 Session 实例对象
        Session session = Session.getInstance(props);
        Store store = session.getStore(inbox.getProtocol());

        //链接邮箱
        store.connect(inbox.getMailDir(), inbox.getPassWord());

        // 获得收件箱
        Folder folder = store.getFolder("INBOX");
        /* Folder.READ_ONLY：只读权限
         * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
         */
        folder.open(Folder.READ_WRITE);    //打开收件箱
        // 得到收件箱中的所有邮件,并解析
        Message[] messages = folder.getMessages();
        if (messages == null || messages.length < 1){
            return Result.build(null,ResultCodeEnum.MAIL_NULL);
        }
    }

}





