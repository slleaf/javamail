package com.atchihaya.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
/**
 * 用户信息实体类
 */

/**
 * @TableName mail_user
 */
@TableName(value ="mail_user")
@Data
public class MailUser implements Serializable {
    @TableId
    private Integer uid;

    private String username;

    private String userPwd;

    private String nickName;

    private static final long serialVersionUID = 1L;


}