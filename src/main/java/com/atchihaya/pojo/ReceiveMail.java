package com.atchihaya.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName receive_mail
 */
@TableName(value = "receive_mail")
@Data
public class ReceiveMail implements Serializable {
    @TableId
    private Long id;

    private String subject;

    private String receiveMailFrom;

    private String receiveMailTo;

    private String sentDate;

    private boolean isSeen;

    private String size;

    private String fileName;
    private String text;

    private static final long serialVersionUID = 1L;
}