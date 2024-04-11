package com.atchihaya.service.impl;

import com.atchihaya.service.FileService;
import com.atchihaya.util.EmlBasicTest;
import com.atchihaya.util.HashUtil.FileHashUtil;
import com.atchihaya.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
}
