package com.atchihaya.mapper;

import com.atchihaya.pojo.Mail;
import com.atchihaya.pojo.PortalVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author chihaya
* @description 针对表【mail】的数据库操作Mapper
* @createDate 2024-03-11 23:54:51
* @Entity com.atchihaya.pojo.Mail
*/
public interface MailMapper extends BaseMapper<Mail> {


    IPage<Map> selectMailPage(IPage<Mail> page,@Param("portalVo") PortalVo portalVo);
}




