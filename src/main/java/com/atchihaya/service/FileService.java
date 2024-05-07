package com.atchihaya.service;

import com.atchihaya.util.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

/**
 * ClassName: FileService
 * Package: com.atchihaya.service
 * Description:
 *
 * @Author: chihaya
 * @Create: 2024/3/22 20:18
 * @Version: 1.0
 */
public interface FileService {
    Result fileVerification(MultipartFile[] files);

    Result analyzeMail(MultipartFile[] files) throws IOException;

    Result dataAnalyze(MultipartFile[] files) throws IOException, ParseException;
}
