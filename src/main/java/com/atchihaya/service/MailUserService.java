package com.atchihaya.service;

import com.atchihaya.pojo.MailUser;
import com.atchihaya.util.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author chihaya
* @description 针对表【mail_user】的数据库操作Service
* @createDate 2024-03-11 23:54:51
*/
public interface MailUserService extends IService<MailUser> {

    Result login(MailUser mailUser);

    Result getUserInfo(String token);

    Result checkUserName(String username);

    Result regist(MailUser user);
}
