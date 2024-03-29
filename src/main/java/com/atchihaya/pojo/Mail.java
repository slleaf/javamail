package com.atchihaya.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @TableName mail
 */

/**
 * 邮件接收实体类
 */
@TableName(value ="mail")
@Data
public class Mail implements Serializable {
    private Long id;

    private String mailFrom;

    private String mailFromName;

    private String mailTo;

    private String mailToName;

    private String mailCc;

    private String mailBcc;

    private String mailSubject;

    private String mailText;

    private Date sentDate;

    private String status;

    private String error;

    private String fileDir;
    @JsonIgnore
    @TableField(exist = false)
    private MultipartFile[] multipartFiles;//邮件附件

    private static final long serialVersionUID = 1L;
}