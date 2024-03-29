package com.atchihaya.service;

import com.atchihaya.pojo.Inbox;
import com.atchihaya.pojo.Mail;
import com.atchihaya.pojo.MailUser;
import com.atchihaya.pojo.PortalVo;
import com.atchihaya.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author chihaya
* @description 针对表【mail】的数据库操作Service
* @createDate 2024-03-11 23:54:51
*/
public interface MailService extends IService<Mail> {

    Result sendMail(Mail mail, MailUser mailUser) throws MessagingException;


    Result mailLoad(PortalVo portalVo);

    Result showMailDetail(Integer id);

    Result receiveMail(Inbox inbox) throws MessagingException;
}
