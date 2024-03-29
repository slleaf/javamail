package com.atchihaya.controller;

import com.atchihaya.pojo.MailUser;
import com.atchihaya.service.MailUserService;
import com.atchihaya.util.Result;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: UserController
 * Package: com.atchihaya.controller
 * Description:
 *
 * @Author: chihaya
 * @Create: 2024/3/24 23:52
 * @Version: 1.0
 */
@RestController
@RequestMapping("user")
@MapperScan("com.atchihaya.mapper")
public class UserController {
    /**
     * 登录接口
     */
    @Autowired
    private MailUserService mailUserService;
    @PostMapping("login")
    public Result login(@RequestBody MailUser mailUser){
        Result result = mailUserService.login(mailUser);
        System.out.println("result = " + result);
        return result;
    }

    /**
     * 根据登录状态返回token
     * @param token
     * @return
     */
    @GetMapping("getUserInfo")
    public Result userInfo(@RequestHeader String token) {
        Result result = mailUserService.getUserInfo(token);
        return result;
    }

    /**
     * 校验注册信息
     * @param username
     * @return
     */
    @PostMapping("checkUserName")
    public Result checkUserName(String username) {
        Result result = mailUserService.checkUserName(username);
        return result;
    }

    /**
     * 注册功能实现
     * @param user
     * @return
     */
    @PostMapping("regist")
    public Result regist(@RequestBody MailUser user) {
        Result result = mailUserService.regist(user);
        return result;
    }
}
