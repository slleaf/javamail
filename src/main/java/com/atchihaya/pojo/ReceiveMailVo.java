package com.atchihaya.pojo;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: ReceiveMailVo
 * Package: com.atchihaya.pojo
 * Description:
 *
 * @Author: chihaya
 * @Create: 2024/3/30 0:06
 * @Version: 1.0
 */
@Data
public class ReceiveMailVo {
    public long id;
    public String subject;
    public String from;
    public String to;
    public Date date;
    public boolean isSeen;
    public Integer size;
    public String fileName;
}
