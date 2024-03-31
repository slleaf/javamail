package com.atchihaya.service;

import com.atchihaya.pojo.Inbox;
import com.atchihaya.pojo.ReceiveMail;
import com.atchihaya.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
* @author 水律
* @description 针对表【receive_mail】的数据库操作Service
* @createDate 2024-03-30 00:16:16
*/
public interface ReceiveMailService extends IService<ReceiveMail> {


    Result receiveMail(Inbox inbox) throws MessagingException, IOException;

    Result showInboxDetail(Integer id);
}
