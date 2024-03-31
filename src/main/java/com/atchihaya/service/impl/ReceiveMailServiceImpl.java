package com.atchihaya.service.impl;

import com.atchihaya.pojo.Inbox;
import com.atchihaya.util.ReceiveMailUtil;
import com.atchihaya.util.Result;
import com.atchihaya.util.ResultCodeEnum;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atchihaya.pojo.ReceiveMail;
import com.atchihaya.service.ReceiveMailService;
import com.atchihaya.mapper.ReceiveMailMapper;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
* @author 水律
* @description 针对表【receive_mail】的数据库操作Service实现
* @createDate 2024-03-30 00:16:16
*/
@Service
public class ReceiveMailServiceImpl extends ServiceImpl<ReceiveMailMapper, ReceiveMail>
    implements ReceiveMailService {
    @Autowired
    private ReceiveMailMapper receiveMailMapper;

    @Override
    public Result receiveMail(Inbox inbox) throws MessagingException, IOException {
        receiveMailMapper.deleteAll();
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
        if (messages == null || messages.length < 1) {
            return Result.build(null, ResultCodeEnum.MAIL_NULL);
        }
        ReceiveMail receiveMail = null;
        List<ReceiveMail>list=new ArrayList<>();
        int num=1;
        if (messages.length <= 30) {
            for (int i = messages.length - 1; i > 0; i--) {
                 receiveMail = setReceiveMail(messages, i, num);
                list.add(receiveMail);
                num++;
            }
        } else if (messages.length > 30) {
            for (int i = messages.length - 1, count = 0; i > 0 && count < 30; i--, count++) {
                 receiveMail = setReceiveMail(messages, i, num);
                list.add(receiveMail);
                num++;
            }
        }
        //邮件的存入数据库
        saveBatch(list);
        //分页查询
        IPage<ReceiveMail>page=new Page<>(inbox.getPageNum(),inbox.getPageSize());
        receiveMailMapper.selectAll(page);
        Map<String,Object> pageInfo=new HashMap<>();
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
    public Result showInboxDetail(Integer id) {
        ReceiveMail receiveMail=receiveMailMapper.selectById(id);
        Map<String,Object>data=new HashMap<>();
        data.put("mail",receiveMail);
        return Result.ok(data);
    }

    /**
     * 封装邮件信息的类
     * @param messages
     * @param i
     * @param num
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public ReceiveMail setReceiveMail(Message[]messages,int i,int num) throws MessagingException, IOException {
        ReceiveMail receiveMail=new ReceiveMail();
        StringBuffer sb = new StringBuffer();
        MimeMessage msg = (MimeMessage) messages[i];
        receiveMail.setId((long) num);
        receiveMail.setSubject(ReceiveMailUtil.getSubject(msg));
        receiveMail.setReceiveMailFrom(ReceiveMailUtil.getFrom(msg));
        receiveMail.setReceiveMailTo(ReceiveMailUtil.getReceiveAddress(msg, null));
        receiveMail.setSentDate(ReceiveMailUtil.getSentDate(msg, null));
        receiveMail.setSeen(ReceiveMailUtil.isSeen(msg));
        receiveMail.setSize(msg.getSize() * 1024 + "kb");
        if (ReceiveMailUtil.isContainAttachment(msg)) {
            List<String> filename = ReceiveMailUtil.getAttachmentFileNames(msg);
            for (String str : filename) {
                sb.append(str);
            }
            receiveMail.setFileName(sb.toString());
        }
        StringBuffer content = new StringBuffer();
        ReceiveMailUtil.getMailTextContent(msg, content);
        receiveMail.setText(String.valueOf(content));
        return receiveMail;
    }
}




