package com.atchihaya.controller;

import com.atchihaya.pojo.Inbox;
import com.atchihaya.pojo.Mail;
import com.atchihaya.pojo.MailUser;
import com.atchihaya.pojo.PortalVo;
import com.atchihaya.service.MailService;
import com.atchihaya.util.Result;
import com.atchihaya.util.ResultCodeEnum;
import jakarta.mail.MessagingException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: MailController
 * Package: com.atchihaya.controller
 * Description:
 *
 * @Author: chihaya
 * @Create: 2024/3/12 0:05
 * @Version: 1.0
 */
@RestController
@RequestMapping("mail")
@MapperScan("com.atchihaya.mapper")
public class MailController {
    @Value("${file.upload.dir}")
    private String basePath;
    @Autowired
    private MailService mailService;

    /**
     * 发送邮件
     */
    @PostMapping("send")
    public Result sendMail(Mail mail,
                           MailUser mailUser,
                           @RequestPart(value = "file",required = false)MultipartFile[] files) throws MessagingException {
        mail.setMultipartFiles(files);
        /**
         * 判断是否携带附件
         */
        if (files!=null) {
            StringBuffer stringBuffer = new StringBuffer();
            int count = 0;
            for (MultipartFile multipartFile : mail.getMultipartFiles()) {
                //获取文件原始名
                String originalFilename = multipartFile.getOriginalFilename();
                //邮件文件名拼接
                if (count > 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(originalFilename);
                count++;
            }
            String filedir = stringBuffer.toString();
            mail.setFileDir(filedir);
        }
        return mailService.sendMail(mail,mailUser);
    }

    /**
     * 查询发件记录
     * @return
     */
    @PostMapping ("mailload")
    public Result mailLoad(@RequestBody PortalVo portalVo){
        Result result=mailService.mailLoad(portalVo);
        return result;
    }

    /**
     * 根据id返回mail详情
     * @param id
     * @return
     */
    @PostMapping("showMailDetail")
    public Result showMailDetail(Integer id){
        Result result=mailService.showMailDetail(id);
        return result;
    }

    /**
     * 收件箱功能实现
     * @param inbox
     * @return
     */
    @PostMapping("receivemail")
    public Result receiveMail(@RequestBody Inbox inbox){
        Result result=mailService.receiveMail(inbox);
        return result;
    }
}
