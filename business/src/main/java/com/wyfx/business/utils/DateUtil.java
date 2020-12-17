package com.wyfx.business.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author johnson liu
 * @date 2019/11/13
 * @description 日期转换工具类
 */
public class DateUtil {

    public static String convertTimeToStr(Date time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    /**
     * 将字符串类型的时间转为Date类型
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date formatDate(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(time);
    }

    public static Date formatDate(Long time) throws ParseException {
        Date date = new Date(time);
        return date;
    }

    /**
     * 补全时间段的时分秒
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static String completionDate(String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(time);
        StringBuffer stringBuffer = new StringBuffer(simpleDateFormat.format(date));
        stringBuffer.append(" 00:00:00");
        return stringBuffer.toString();
    }

    /**
     * 补全时间段的时分秒
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static String completionDate(String time, boolean flag) throws ParseException {
        StringBuffer stringBuffer = null;
        if (time != null && !"".equals(time)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(time);
            stringBuffer = new StringBuffer(simpleDateFormat.format(date));
            if (flag) {
                stringBuffer.append(" 00:00:00");
            } else {
                stringBuffer.append(" 23:59:59");
            }
        }
        String afterTime = (time == null || "".equals(time)) ? null : stringBuffer.toString();

        return afterTime;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar now = Calendar.getInstance();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(now.get(Calendar.YEAR) + "-");
        stringBuffer.append(now.get(Calendar.MONTH) + 1 + "-");
        stringBuffer.append(now.get(Calendar.DAY_OF_MONTH));
        return stringBuffer.toString();
    }

    /**
     * 对比日期大小
     *
     * @param beforeDateStr
     * @param afterDateStr
     * @return
     */
    public static Boolean compareDate(String beforeDateStr, String afterDateStr) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date beforeDate = simpleDateFormat.parse(beforeDateStr);
            Date afterDate = simpleDateFormat.parse(afterDateStr);
            return afterDate.after(beforeDate);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前日期指定天数前的日期
     *
     * @param day 天数
     * @return
     */
    public static String getAppointDate(int day) {
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
            Date date = now.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        try {
            boolean b = true;
            if (b == true) {
                System.out.println("相等");
            } else {
                System.out.println("不相等");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
