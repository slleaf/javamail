package com.atchihaya.controller;

import com.atchihaya.service.FileService;
import com.atchihaya.util.Result;
import com.atchihaya.util.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

/**
 * ClassName: FileController
 * Package: com.atchihaya.controller
 * Description:
 *
 * @Author: chihaya
 * @Create: 2024/3/22 20:12
 * @Version: 1.0
 */
@RestController
@RequestMapping("file")
@MapperScan("com.atchihaya.mapper")
@CrossOrigin
@Slf4j
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * 附件校验模块
     * @param files
     * @return
     */
    @PostMapping("verification")
    public Result fileVerification(@RequestPart(value = "file",required = false) MultipartFile[] files){
        if (files == null || files.length < 2) {
            return Result.build(null,ResultCodeEnum.FILE_MISSING);
        }
        return fileService.fileVerification(files);
    }
    /**
     * 返回邮件结构
     */
    @PostMapping("analyze")
    public Result analyzeMail(@RequestPart(value = "file",required = false)MultipartFile[] files) throws IOException {
        if (files==null){
            return Result.build(null, ResultCodeEnum.FILE_MISSING);
        }
        for (MultipartFile multipartFile: files){
            String originalFilename = multipartFile.getOriginalFilename();
            //文件名后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!".eml".equals(suffix)){
                return Result.build(null,ResultCodeEnum.INCORRECT_FILE_TYPES);
            }
        }
        return fileService.analyzeMail(files);
    }
    /**
     * 进行日期对比的接口
     */
    @PostMapping("dataAnalyze")
    public Result dataAnalyze(@RequestPart(value = "file",required = false)MultipartFile[] files) throws IOException, ParseException {
        if (files==null){
            return Result.build(null, ResultCodeEnum.FILE_MISSING);
        }
        return fileService.dataAnalyze(files);
    }
}
