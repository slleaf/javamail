package com.atchihaya.pojo;

import lombok.Data;

/**
 * 发件历史分页查询实体类
 */
@Data
public class PortalVo {
    private String username;
    private Integer pageNum = 1;
    private Integer pageSize =10;
}