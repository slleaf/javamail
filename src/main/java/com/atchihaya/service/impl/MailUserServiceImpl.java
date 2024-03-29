package com.atchihaya.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.atchihaya.util.JwtHelper;
import com.atchihaya.util.MD5Util;
import com.atchihaya.util.Result;
import com.atchihaya.util.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atchihaya.pojo.MailUser;
import com.atchihaya.service.MailUserService;
import com.atchihaya.mapper.MailUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chihaya
 * @description 针对表【mail_user】的数据库操作Service实现
 * @createDate 2024-03-11 23:54:51
 */
@Service
public class MailUserServiceImpl extends ServiceImpl<MailUserMapper, MailUser>
        implements MailUserService {
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private MailUserMapper mailUserMapper;
    /**
     * 登录业务实现
     * @param mailUser
     * @return result封装
     */
    @Override
    public Result login(MailUser mailUser) {
        //根据账号查询
        LambdaQueryWrapper<MailUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MailUser::getUsername, mailUser.getUsername());
        MailUser loginUser = mailUserMapper.selectOne(queryWrapper);

        //账号判断
        if (loginUser == null) {
            //账号错误
            return Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }

        //判断密码
        if (!StringUtils.isEmpty(mailUser.getUserPwd())
                && loginUser.getUserPwd().equals(MD5Util.encrypt(mailUser.getUserPwd()))) {
            //账号密码正确
            //根据用户唯一标识生成token
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));

            Map data = new HashMap();
            data.put("token", token);

            return Result.ok(data);
        }

        //密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    @Override
    /**
     * 根据token查询用户数据
     * @param token
     * @return result封装
     */
    public Result getUserInfo(String token) {

        //1.判定是否有效期
        if (jwtHelper.isExpiration(token)) {
            //true过期,直接返回未登录
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }

        //2.获取token对应的用户
        int userId = jwtHelper.getUserId(token).intValue();

        //3.查询数据
        MailUser user = mailUserMapper.selectById(userId);

        if (user != null) {
            user.setUserPwd(null);
            Map data = new HashMap();
            data.put("loginUser",user);
            return Result.ok(data);
        }

        return Result.build(null,ResultCodeEnum.NOTLOGIN);
    }

    /**
     * 检查账号是否可以注册
     *
     * @param username 账号信息
     * @return
     */
    @Override
    public Result checkUserName(String username) {

        LambdaQueryWrapper<MailUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MailUser::getUsername,username);
        MailUser user = mailUserMapper.selectOne(queryWrapper);

        if (user != null){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        return Result.ok(null);
    }

    @Override
    public Result regist(MailUser user) {
        LambdaQueryWrapper<MailUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MailUser::getUsername,user.getUsername());
        Long count = mailUserMapper.selectCount(queryWrapper);

        if (count > 0){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        int rows = mailUserMapper.insert(user);
        System.out.println("rows = " + rows);
        return Result.ok(null);
    }
}





