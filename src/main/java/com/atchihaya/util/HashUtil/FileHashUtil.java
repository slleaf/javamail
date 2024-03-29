package com.atchihaya.util.HashUtil;



import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
@Component
public class FileHashUtil {
    /**
     * 返回 哈希算法：检验结果的方法
     * @param files
     * @return
     */
    public Map<String,String>fileHash(MultipartFile[] files){
        Map<String,String> map=new HashMap<>();
        map.putAll(fileDemoCRC32(files));
        map.putAll(fileDemoMD5(files));
        map.putAll(fileDemoSHA1(files));
        map.putAll(fileDemoSHA512(files));
        return map;
    }
    /**
     * 多文件，多线程的示例
     */
    public Map<String,String> fileDemoCRC32(MultipartFile[] files) {
        //1.创建文件集合
        List<File> fileList = new ArrayList<>();

        for (MultipartFile file : files) {
            File convertedFile = convertMultipartFileToFile(file);
            if (convertedFile != null) {
                fileList.add(convertedFile);
            }
        }
        //2.准备需要执行的方法集合，支持的类型在Algorithm接口中
        Set<String> set = Set.of(
                Algorithm.CRC32
        );
        //3.创建多线程的摘要对象，传入摘要类型，大小写输出，文件list集合
        MsgDigestFiles msgDigestFiles = new MsgDigestFiles(set, false, fileList);
        //4.获取结果
        Map<String, String> map = msgDigestFiles.hash();
        String temp = null;
        boolean flag = true;
        for (String value : map.values()) {
            if (temp == null) {
                temp = (String) value;
            } else {
                if (!temp.equals(value)) {
                    flag = false;
                    break; // 如果找到相等的值，可以提前结束循环
                }
            }
        }
        Map<String,String> data=new HashMap<>();
        if (flag==true){
            data.put("CRC32算法","文件无被篡改迹象");
            return data;
        }else {
            data.put("CRC32算法","文件被篡改");
            return data;
        }

    }
    public Map<String,String> fileDemoMD5(MultipartFile[] files) {
        //1.创建文件集合
        List<File> fileList = new ArrayList<>();

        for (MultipartFile file : files) {
            File convertedFile = convertMultipartFileToFile(file);
            if (convertedFile != null) {
                fileList.add(convertedFile);
            }
        }
        //2.准备需要执行的方法集合，支持的类型在Algorithm接口中
        Set<String> set = Set.of(
                Algorithm.MD5
        );
        //3.创建多线程的摘要对象，传入摘要类型，大小写输出，文件list集合
        MsgDigestFiles msgDigestFiles = new MsgDigestFiles(set, false, fileList);
        //4.获取结果
        Map<String, String> map = msgDigestFiles.hash();
        String temp = null;
        boolean flag = true;
        for (String value : map.values()) {
            if (temp == null) {
                temp = (String) value;
            } else {
                if (!temp.equals(value)) {
                    flag = false;
                    break; // 如果找到相等的值，可以提前结束循环
                }
            }
        }
        Map<String,String> data=new HashMap<>();
        if (flag==true){
            data.put("MD5算法","文件无被篡改迹象");
            return data;
        }else {
            data.put("MD5算法","文件被篡改");
            return data;
        }

    }
    public Map<String,String> fileDemoSHA1(MultipartFile[] files) {
        //1.创建文件集合
        List<File> fileList = new ArrayList<>();

        for (MultipartFile file : files) {
            File convertedFile = convertMultipartFileToFile(file);
            if (convertedFile != null) {
                fileList.add(convertedFile);
            }
        }
        //2.准备需要执行的方法集合，支持的类型在Algorithm接口中
        Set<String> set = Set.of(
                Algorithm.SHA_1
        );
        //3.创建多线程的摘要对象，传入摘要类型，大小写输出，文件list集合
        MsgDigestFiles msgDigestFiles = new MsgDigestFiles(set, false, fileList);
        //4.获取结果
        Map<String, String> map = msgDigestFiles.hash();
        String temp = null;
        boolean flag = true;
        for (String value : map.values()) {
            if (temp == null) {
                temp = (String) value;
            } else {
                if (!temp.equals(value)) {
                    flag = false;
                    break; // 如果找到相等的值，可以提前结束循环
                }
            }
        }
        Map<String,String> data=new HashMap<>();
        if (flag==true){
            data.put("SHA_1算法","文件无被篡改迹象");
            return data;
        }else {
            data.put("SHA_1算法","文件被篡改");
            return data;
        }

    }
    public Map<String,String> fileDemoSHA512(MultipartFile[] files) {
        //1.创建文件集合
        List<File> fileList = new ArrayList<>();

        for (MultipartFile file : files) {
            File convertedFile = convertMultipartFileToFile(file);
            if (convertedFile != null) {
                fileList.add(convertedFile);
            }
        }
        //2.准备需要执行的方法集合，支持的类型在Algorithm接口中
        Set<String> set = Set.of(
                Algorithm.SHA3_512
        );
        //3.创建多线程的摘要对象，传入摘要类型，大小写输出，文件list集合
        MsgDigestFiles msgDigestFiles = new MsgDigestFiles(set, false, fileList);
        //4.获取结果
        Map<String, String> map = msgDigestFiles.hash();
        String temp = null;
        boolean flag = true;
        for (String value : map.values()) {
            if (temp == null) {
                temp = (String) value;
            } else {
                if (!temp.equals(value)) {
                    flag = false;
                    break; // 如果找到相等的值，可以提前结束循环
                }
            }
        }
        Map<String,String> data=new HashMap<>();
        if (flag==true){
            data.put("SHA_512算法","文件无被篡改迹象");
            return data;
        }else {
            data.put("SHA_512算法","文件被篡改");
            return data;
        }

    }
    /**
     * 将multipartFile转为file的方法
     * @param multipartFile
     * @return
     */
    private File convertMultipartFileToFile(MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}



