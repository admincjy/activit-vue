package com.wl.wlp2ploansystem.infrastructures.common.support;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 通用工具累
 */
public class CommonHelper {
    /**
     *时间（秒）间隔转化为文字显示间隔
     */
    public static String translateSecondsOfDisplay(String secsondsString) {
        if (StringUtils.isEmpty(secsondsString) || secsondsString.equals("-")) {
            return "-";
        }

        long secsonds = Long.valueOf(secsondsString).longValue();

        long day = secsonds / (24 * 60 * 60);
        long hour = (secsonds / (60 * 60) - day * 24);
        long min = ((secsonds / (60)) - day * 24 * 60 - hour * 60);
        long secs = (secsonds - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        String s = "";

        if (day > 0) {
            s = day + "天" + hour + "小时" + min + "分钟" + secs + "秒";
        } else if (hour > 0) {
            s = hour + "小时" + min + "分钟" + secs + "秒";
        } else if (min > 0) {
            s = min + "分钟" + secs + "秒";
        } else {
            s = secs + "秒";
        }
        return s;
    }
    /**
     *使用";"合并字段串列表
     */
    public static String stringJoin(List<String> list)
    {
        String[] arr = new String[1];
        arr = list.toArray(arr);

       return String.join(";", arr);
    }
    /**
     *日期格式化
     */
    public static String localDateTimeFormat(LocalDateTime dateTime,String pattern){

        if(dateTime == null){
            return  "";
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);

        return df.format(dateTime);
    }
    /**
     *java.util.Date 转换为 LocalDateTime 类型
     */
    public static LocalDateTime dateToLocalDateTime(java.util.Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }
    /**
     *对象拷贝
     */
    public static<T>  T deepCopy( T src){
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            T dest = (T) in.readObject();
            return dest;
        }
        catch (IOException ex){
            throw  new RuntimeException(ex.getMessage(),ex);
        }
        catch (ClassNotFoundException ex){
            throw  new RuntimeException(ex.getMessage(),ex);
        }
    }
	 /**
     *列表对象拷贝
     */
	public static <T> List<T> deepCopyList(List<T> src){
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<T> dest = (List<T>) in.readObject();
            return dest;
        }
        catch (IOException ex){
            throw  new RuntimeException(ex.getMessage(),ex);
        }
        catch (ClassNotFoundException ex){
            throw  new RuntimeException(ex.getMessage(),ex);
        }
    }

}
