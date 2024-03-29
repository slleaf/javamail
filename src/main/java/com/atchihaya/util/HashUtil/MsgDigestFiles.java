package com.atchihaya.util.HashUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MsgDigestFiles {

    //基础变量
    private Set<String> set;
    private Map<File, Map<String, String>> resultMap;
    private StringBuffer sb;
    private int fileCacheSize = 1048576;//1MB
    private boolean isLowerCase;

    //文件集合
    private List<File> fileList;
    //文件个数
    private int fileCount;
    //文件总大小，字节
    private long filesSize;

    public MsgDigestFiles(Set<String> set, boolean isLowerCase, List<File> fileList) {
        this.set = set;
        this.isLowerCase = isLowerCase;
        this.fileList = fileList;

        this.fileCount = fileList.size();
        this.resultMap = new HashMap<>();
        this.sb = new StringBuffer();
    }

    /**
     * 多线程方法
     */
    public Map<String, String> hash() {
        filesSize = 0;
        ExecutorService threadPool = Executors.newFixedThreadPool(fileCount);
        List<MsgDigestFile> msgDigestFileList = new ArrayList<>();
        Map<File, Future<Map<String, String>>> fileFutureMap = new HashMap<>();
        Map<String, String> resultMap = new HashMap<>(); // 修改为只返回哈希值的Map

        fileList.forEach(file -> {
            filesSize += file.length();
            MsgDigestFile msgDigestFile = new MsgDigestFile(set, isLowerCase, file);
            msgDigestFile.setFileCacheSize(fileCacheSize);
            msgDigestFileList.add(msgDigestFile);
        });

        try {
            msgDigestFileList.forEach(msgDigestFile -> fileFutureMap.put(msgDigestFile.getFile(), threadPool.submit(msgDigestFile)));
        } finally {
            threadPool.shutdown();
        }

        fileFutureMap.forEach((file, mapFuture) -> {
            try {
                Map<String, String> fileHashes = mapFuture.get();
                fileHashes.forEach((hashType, hashValue) -> resultMap.put(file.getName(), hashValue)); // 只将哈希值存入 resultMap
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        return resultMap;
    }


    @Override
    public String toString() {
        resultMap.forEach((file, map) -> {
            sb.append("文件：").append(file.getAbsolutePath()).append("\n").append("大小：").append(file.length()).append("\n");
            map.forEach((k, v) -> sb.append(k).append("：").append(v).append("\n"));
        });
        return sb.toString();
    }

    public long getFilesSize() {
        return filesSize;
    }
}
