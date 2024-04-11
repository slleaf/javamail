package com.atchihaya.controller;

import com.atchihaya.pojo.Inbox;
import com.atchihaya.service.ReceiveMailService;
import com.atchihaya.util.Result;
import jakarta.mail.MessagingException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * ClassName: ReceiveMailController
 * Package: com.atchihaya.controller
 * Description:
 *
 * @Author: chihaya
 * @Create: 2024/3/30 0:30
 * @Version: 1.0
 */
@RestController
@RequestMapping("receiveMail")
@MapperScan("com.atchihaya.mapper")
@CrossOrigin
public class ReceiveMailController {
    @Autowired
    private ReceiveMailService receiveMail;
    /**
     * 收件箱功能实现
     * @param inbox
     * @return
     */
    @PostMapping("inbox")
    public Result receiveMail(@RequestBody Inbox inbox) throws MessagingException, IOException {
        Result result=receiveMail.receiveMail(inbox);
        return result;
    }

    /**
     * 根据id返回inbox详情
     * @param id
     * @return
     */
    @PostMapping("showInboxDetail")
    public Result showInboxDetail(Integer id){
        Result result=receiveMail.showInboxDetail(id);
        return result;
    }
}
