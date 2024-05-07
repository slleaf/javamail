package com.atchihaya.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: StringToData
 * Package: com.atchihaya.util
 * Description:
 *
 * @Author: chihaya
 * @Create: 2024/4/29 17:47
 * @Version: 1.0
 */
public class StringToDate {
    public static Date stringUtil(String string) throws ParseException {
        Date newDate=null;
        // 定义日期信息的正则表达式模式
        String pattern = "\\b\\w{3}, \\d{1,2} \\w{3} \\d{4} \\d{2}:\\d{2}:\\d{2} \\+\\d{4}\\b";
        Pattern r = Pattern.compile(pattern);

        // 创建 Matcher 对象
        Matcher m = r.matcher(string);
        if (m.find()) {
            // 提取日期信息
            String dateString = m.group(0);
            // 定义日期格式
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            inputDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 设置输出日期格式的时区为 GMT
            newDate = inputDateFormat.parse(dateString); // 解析日期字符串


        }

        return newDate;
    }

    public static Date dateUtil(String string) throws ParseException {
        String dateStr = string; // 日期字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义日期格式
            Date date = dateFormat.parse(dateStr); // 将字符串转换为日期
            return date;

    }
}
