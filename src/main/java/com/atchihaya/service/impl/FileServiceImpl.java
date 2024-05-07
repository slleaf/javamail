package com.atchihaya.service.impl;

import com.atchihaya.pojo.EmlEntry;
import com.atchihaya.service.FileService;
import com.atchihaya.util.EmlBasicTest;
import com.atchihaya.util.HashUtil.FileHashUtil;
import com.atchihaya.util.Result;
import com.atchihaya.util.ResultCodeEnum;
import com.atchihaya.util.StringToDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ClassName: FileService
 * Package: com.atchihaya.service.impl
 * Description:
 *
 * @Author: chihaya
 * @Create: 2024/3/22 20:26
 * @Version: 1.0
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Autowired
    private FileHashUtil fileHashUtil;
    @Override
    public Result fileVerification(MultipartFile[] files) {
        log.info("附件分析接口被调用");
        Map<String, String> map = fileHashUtil.fileHash(files);
        return Result.ok(map);
    }

    /**
     * 邮件结构的分析
     *
     * @param files
     * @return
     */
    @Override
    public Result analyzeMail(MultipartFile[] files) throws IOException {
        InputStream in = null;
        HashMap<String, Object> map = new HashMap<>();
        int i =1;
        try {
            for (MultipartFile multipartFile : files) {
                in = multipartFile.getInputStream();
                String emlParseJson = EmlBasicTest.emlParse(in);

                String key="eml"+i;
                map.put(key,emlParseJson);
                i++;
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        } finally {
            if (in != null) {
                in.close();
            }
        }
        log.info("邮件分析模块被调用");
        return Result.ok(map);
    }

    @Override
    public Result dataAnalyze(MultipartFile[] files) throws IOException, ParseException {
        InputStream in = null;
        List<Date>list=new ArrayList<>();
        Map data=new HashMap<>();
        //先进行对文件类型的判定
        for (MultipartFile multipartFile: files){
            String originalFilename = multipartFile.getOriginalFilename();
            //文件名后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            //如果是eml格式的文件则进行解析
            if (!".eml".equals(suffix)){
                return Result.build(null, ResultCodeEnum.FILE_MISSING);
            }
            in = multipartFile.getInputStream();
            EmlEntry entry = EmlBasicTest.emlParseByTime(in);
            Date date1 = StringToDate.stringUtil(entry.getReceived());
            list.add(date1);
            Date date2 = StringToDate.dateUtil(entry.getDateTime());
            list.add(date2);
        }
        long timeAbs = Math.abs(list.get(0).getTime() - list.get(1).getTime());
        if (timeAbs<=30000){
            data.put("status",200);
        }else {
            data.put("status",400);
        }
        return Result.ok(data);
    }
}
