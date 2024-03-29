package com.atchihaya.util.HashUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class MsgDigestFile implements Callable<Map<String, String>> {
    //方法对象
    private Method method;

    private Map<String, String> map;
    private StringBuffer sb;
    private int fileCacheSize = 1048576;//1MB
    private boolean isLowerCase;

    //文件
    private File file;
    //文件大小，字节
    private long fileSize;
    //剩余未读取字节数
    private long surplusFileSize;


    public MsgDigestFile(Set<String> set, boolean isLowerCase, File file) {
        this.isLowerCase = isLowerCase;
        this.file = file;

        this.sb = new StringBuffer();
        this.method = new Method(set);
        surplusFileSize = fileSize = file.length();
        sb.append("文件：").append(file.getAbsolutePath()).append("\n");
        sb.append("大小：").append(file.length()).append(" 字节\n");
    }


    /**
     * 线程池方法
     *
     * @return 执行结果
     * @throws Exception
     */
    @Override
    public Map<String, String> call() throws Exception {
        return hash();
    }

    /**
     * 单线程方法
     *
     * @return
     */
    public Map<String, String> hash() {
        //创建缓冲区
        byte[] bytes = new byte[fileCacheSize];
        //初始化方法对象
        method.getMethod1().run();
        //创建文件输入流，自动关闭资源
        try (FileInputStream fis = new FileInputStream(file)) {
            //读取文件字节到缓存，在缓存中进行计算
            while (fis.read(bytes) != -1) {
                //计算偏移量，bytes中有效的未计算的字节长度
                int offset = surplusFileSize > fileCacheSize ? fis.available() != 0 ? fileCacheSize : (int) surplusFileSize : (int) surplusFileSize;
                //计算所有的任务
                method.getMethod2().accept(bytes, offset);
                //更新剩余未读取的字节
                surplusFileSize -= offset;
            }
            //生成结果
            return map = method.getMethod3().apply(isLowerCase);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 结果集合
     *
     * @return
     */
    public Map<String, String> getMap() {
        return map;
    }

    @Override
    public String toString() {
        map.forEach((k, v) -> sb.append(k).append("：").append(v).append("\n"));
        return sb.toString();
    }

    /**
     * 文件字节总数
     *
     * @return
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * 剩余未读取的字节数
     *
     * @return
     */
    public long getSurplusFileSize() {
        return surplusFileSize;
    }

    /**
     * 设置缓冲区大小，字节，默认1MB
     *
     * @param fileCacheSize
     */
    public void setFileCacheSize(int fileCacheSize) {
        if (fileCacheSize > 0) {
            this.fileCacheSize = fileCacheSize;
        }
    }

    /**
     * 获取文件对象
     *
     * @return
     */
    public File getFile() {
        return file;
    }
}