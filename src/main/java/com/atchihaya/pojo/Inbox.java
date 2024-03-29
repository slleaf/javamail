package com.atchihaya.pojo;

import lombok.Data;

/**
 * ClassName: Inbox
 * Package: com.atchihaya.pojo
 * Description: 定义收件箱属性的实体类
 *
 * @Author: chihaya
 * @Create: 2024/3/29 0:19
 * @Version: 1.0
 */
@Data
public class Inbox {
    public long id;
    public String protocol;//协议
    public String mailDir;//邮箱地址
    public String passWord;//授权码
    private Integer pageNum = 1;
    private Integer pageSize =10;
}
