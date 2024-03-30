package com.atchihaya.mapper;

import com.atchihaya.pojo.ReceiveMail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
* @author 水律
* @description 针对表【receive_mail】的数据库操作Mapper
* @createDate 2024-03-30 00:16:16
* @Entity com.atchihaya.pojo.ReceiveMail
*/
public interface ReceiveMailMapper extends BaseMapper<ReceiveMail> {


    void deleteAll();

    IPage<Map> selectAll(IPage<ReceiveMail> page);
}




