package com.atchihaya.util.HashUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.zip.CRC32;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;

/**
 * 模型层，实现计算过程的方法类
 */
public class Method {
    //待计算的任务集合
    private Set<String> typeSet;
    //遍历需要计算指定摘要类型的set集合，key=摘要类型，value=对应摘要类型的摘要对象
    private Map<String, MessageDigest> mapMD = new HashMap<>();
    //遍历需要计算指定冗余类型的set集合，key=冗余类型，value=对应冗余类型的冗余对象
    private Map<String, Checksum> mapCS = new HashMap<>();

    public Runnable getMethod1() {
        return method1;
    }

    public BiConsumer<byte[], Integer> getMethod2() {
        return method2;
    }

    public Function<Boolean, Map<String, String>> getMethod3() {
        return method3;
    }

    public Method(Set<String> typeSet) {
        this.typeSet = typeSet;
    }

    //初始化方法的Lambda表达式
    private Runnable method1 = () -> {
        //创建摘要对象集合
        typeSet.forEach(type -> {
            switch (type.toUpperCase()) {
                case "CRC32":
                    //crc32算法
                    mapCS.put(type, new CRC32());
                    break;
                case "CRC32C":
                    //crc32c算法
                    mapCS.put(type, new CRC32C());
                    break;
                default:
                    try {
                        mapMD.put(type, MessageDigest.getInstance(type));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        //不支持的算法跳过
                        mapMD.put(type, null);
                    }
                    break;
            }
        });
    };

    //计算方法
    private BiConsumer<byte[], Integer> method2 = (a, b) -> {
        typeSet.forEach(type -> {
            switch (type.toUpperCase()) {
                case "CRC32":
                case "CRC32C":
                    //冗余算法
                    mapCS.get(type).update(a, 0, b);
                    break;
                default:
                    //摘要算法，没有得到对象的直接跳过
                    if (mapMD.get(type) != null) {
                        //摘要值生成，分段更新
                        mapMD.get(type).update(a, 0, b);
                    }
                    break;
            }
        });
    };

    //结果方法的Lambda表达式
    private Function<Boolean, Map<String, String>> method3 = (a) -> {
        //结果的map集合，key=类型，value=值
        Map<String, String> resultMap = new HashMap<>();
        typeSet.forEach(type -> {
            String resultStr = null;
            switch (type.toUpperCase()) {
                case "CRC32":
                case "CRC32C":
                    resultStr = dec2hex(mapCS.get(type).getValue(), a);
                    break;
                default:
                    if (mapMD.get(type) != null) {
                        resultStr = bts2Hex(mapMD.get(type).digest(), a);
                    }
                    break;
            }
            resultMap.put(type, resultStr);
        });
        return resultMap;
    };

    /**
     * 任意进制的数字转换[2-36]
     *
     * @param num         待转换的数字
     * @param fromType    待转换数字的进制类型
     * @param toType      目标数字的进制类型
     * @param isLowerCase 目标大小写控制
     * @return 字符串数字
     * @deprecated 执行效率原因，不到万不得已不推荐使用
     */
    @Deprecated
    public String numTranslate(String num, int fromType, int toType, boolean isLowerCase) {
        num = new BigInteger(num, fromType).toString(toType);
        return isLowerCase ? num.toLowerCase() : num.toUpperCase();
    }

    /**
     * 十进制转十六进制
     *
     * @param num         十进制待转换的数字
     * @param isLowerCase 目标大小写控制
     * @return 十六进制的数字
     */
    public String dec2hex(Long num, boolean isLowerCase) {
        String str = Long.toHexString(num);
        return isLowerCase ? str.toLowerCase() : str.toUpperCase();
    }

    /**
     * 将字节数组转换为十六进制字符串的结果
     *
     * @param bytes       摘要字节数组
     * @param isLowerCase 目标结果大小写
     * @return 32位的十六进制摘要结果，如果需要16位的MD5摘要，substring(8, 24)截取即可
     */
    public String bts2Hex(byte[] bytes, boolean isLowerCase) {
        List<Character> table;
        if (isLowerCase) {
            table = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f');
        } else {
            table = List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');
        }
        int length = bytes.length;
        char[] temp = new char[length << 1];
        int i = 0;
        int var = 0;
        while (i < length) {
            temp[var++] = table.get((240 & bytes[i]) >>> 4);
            temp[var++] = table.get(15 & bytes[i++]);
        }
        return new String(temp);
    }
}

