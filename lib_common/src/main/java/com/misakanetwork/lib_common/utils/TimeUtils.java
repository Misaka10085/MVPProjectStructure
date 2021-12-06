package com.misakanetwork.lib_common.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created By：Misaka10085
 * on：2021/7/8
 * package：com.misakanetwork.lib_common.utils
 * class name：TimeUtils
 * desc：TimeUtils
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {
    public static final String FORMAT_YMD_HMS_ONE = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YMD_HMS_TWO = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String FORMAT_YMD_ONE = "yyyy-MM-dd";
    public static final String FORMAT_YMD_TWO = "yyyy年MM月dd日";
    public static final String FORMAT_YMD_HM_ONE = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YMD_HM_TWO = "yyyy/MM/dd HH:mm";
    public static final String FORMAT_YM_ONE = "yyyy年MM月";
    public static final String FORMAT_YM_TWO = "yyyy-MM";
    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static StringBuilder mFormatBuilder;
    private static Formatter mFormatter;

    /**
     * String 格式切换
     */
    public static String stringToString(String time, String originFormat, String formatStr) {
        if (time.isEmpty()) {
            return "";
        }
        return long2String(str2Long(time, originFormat), formatStr);
    }

    /**
     * 时间戳转String
     */
    public static String long2String(long timeMillis, String format) {
        if (format == null) {
            format = FORMAT_YMD_ONE;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(timeMillis);
    }

    /**
     * 日期转时间戳
     */
    public static Long str2Long(String dateStr, String formatStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr, Locale.CHINA);
            Date parse = format.parse(dateStr);

            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    /**
     * 根据格式转日期
     */
    public static Date getDateByStr(String dateStr, String format) {
        if (dateStr == null || TextUtils.isEmpty(dateStr)) {
            return Calendar.getInstance().getTime();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 获取指定年月
     */
    public static String getMonth(Long l) {
        Calendar c = Calendar.getInstance();
        Date date = new Date(l);
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        Date time = c.getTime();

        return long2String(time.getTime(), FORMAT_YM_ONE);
    }

    /**
     * 获取上一个月
     */
    public static String getLeftMonth(Long l) {
        Calendar c = Calendar.getInstance();
        Date date = new Date(l);
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        Date time = c.getTime();
        return long2String(time.getTime(), FORMAT_YM_TWO);
    }

    /**
     * 获取上个月首日
     */
    public static String getLeftMonthFirstDay(Long l, String format) {
        Date date = new Date(l);
        // 获取指定日期上个月的第一天
        Calendar calendar = Calendar.getInstance();
        // 设置指定日期
        calendar.setTime(date);
        // -1上个月
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, 1);
        date = calendar.getTime();
        return long2String(date.getTime(), format);
    }

    /**
     * 获取下一个月
     */
    public static String getRightMonth(Long l) {
        Calendar c = Calendar.getInstance();
        Date date = new Date(l);
        c.setTime(date);
        c.add(Calendar.MONTH, +1);
        Date time = c.getTime();
        return long2String(time.getTime(), FORMAT_YM_TWO);
    }

    /**
     * 获取下一个月首日
     */
    public static String getRightMonthFirstDay(Long l, String format) {
        Date date = new Date(l);
        // 获取指定日期下个月的第一天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // +1下个月
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        date = calendar.getTime();
        return long2String(date.getTime(), format);
    }

    /**
     * 获取季度
     */
    public static int getCurrentQuarterStartTime(long l) {
        Calendar c = Calendar.getInstance();
        Date date = new Date(l);
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int now = 1;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                now = 1;
            else if (currentMonth >= 4 && currentMonth <= 6)
                now = 2;
            else if (currentMonth >= 7 && currentMonth <= 9)
                now = 3;
            else if (currentMonth >= 10 && currentMonth <= 12)
                now = 4;
            c.set(Calendar.DATE, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获取季度结束时间
     */
    public static long getQuarterEndTime(long l) {
        Calendar c = Calendar.getInstance();
        Date date = new Date(l);
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now.getTime();
    }

    /**
     * 获取季度开始时间
     */
    public static long getQuarterStartTime(long l) {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now.getTime();
    }

    /**
     * 获取两个时间之间的天数
     */
    public static int getDifferenceDay(long start, long end) {
        int days = 0;
        try {
            long diff = start - end;
            days = (int) (diff / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 获取是几年
     */
    public static int getYearSpec(Long aLong) {
        Calendar c = Calendar.getInstance();
        Date date = new Date(aLong);
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        L.e("TimeUtils", "year->" + year);
        return year;
    }

    /**
     * 获取是几月
     */
    public static int getMonthSpec(Long aLong) {
        Calendar c = Calendar.getInstance();
        Date date = new Date(aLong);
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        L.e("TimeUtils", "month->" + month);
        return month + 1;
    }

    /**
     * 获取是几号
     */
    public static int getMonthDay(Long aLong) {
        Calendar c = Calendar.getInstance();
        Date date = new Date(aLong);
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        L.e("TimeUtils", "day->" + day);
        return day;
    }

    /**
     * 获取年
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     */
    public static int getDay() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.DATE);
    }

    /**
     * 获取时
     */
    public static int getHour() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.HOUR);
    }

    /**
     * 获取分
     */
    public static int getMinute() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     */
    public static int getSeconds() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.SECOND);
    }

    /**
     * 根据时间字符串获取毫秒数
     */
    public static long getTimeMillis(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YMD_HM_TWO);
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (ParseException e) {
            L.e("millerror", e.toString());
        }
        return returnMillis;
    }

    /**
     * 根据毫秒数的差值来计算时间差
     * 传入开始时间和结束时间字符串来计算消耗时长
     */
    public static String getTimeExpend(String startTime, String endTime) {
        //传入字串类型 2016/06/28 08:30
        long longStart = getTimeMillis(startTime); //获取开始时间毫秒数
        long longEnd = getTimeMillis(endTime);  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差

        int longHours = (int) (longExpend / (60 * 60 * 1000)); //根据时间差来计算小时数
        int longMinutes = (int) ((longExpend - longHours * (60 * 60 * 1000)) / (60 * 1000));   //根据时间差来计算分钟数

        if (longHours == 0) {
            return longMinutes + "分钟前";
        } else {
            return longHours + "小时前";
        }
//        if(longHours==0){
//            return longMinutes+"分钟前";
//        }else {
//            return longHours+"小时"+longMinutes+"分钟前";
//        }
    }

    /**
     * 与当前时间比较早晚
     *
     * @param time 需要比较的时间
     * @return 输入的时间比现在时间晚则返回true
     */
    public static boolean compareNowTime(String time, String format) {
        boolean isDayu = false;

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        try {
            Date parse = dateFormat.parse(time);
            Date parse1 = dateFormat.parse(long2String(System.currentTimeMillis(), format));

            long diff = parse1.getTime() - parse.getTime();
            isDayu = diff <= 0;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return isDayu;
    }

    /**
     * 比较两个时间
     *
     * @param starTime  开始时间
     * @param endString 结束时间
     * @return 结束时间大于开始时间返回true，否则反之֮
     */
    public static boolean compareTwoTime(String starTime, String endString, String format) {
        boolean isDayu = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endString);

            long diff = parse1.getTime() - parse.getTime();
            isDayu = diff >= 0;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isDayu;

    }

    /**
     * 判断是否是当天及以后的时间
     */
    public static boolean compareToday(String dateFormat) {
        boolean isAfterToDay = false;
        Date nowdate = new Date();
        String date = formatDate(nowdate, FORMAT_YMD_ONE);
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YMD_ONE,
                Locale.CHINA);
        Date d;
        try {
            d = sdf.parse(dateFormat);
            int flag = d.compareTo(getDateByStr(date, FORMAT_YMD_ONE));
            if (flag >= 0) {//当天及当天之后，<0就是在日期之前
                isAfterToDay = true;
            } else {
                isAfterToDay = false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return isAfterToDay;
    }

    public static String formatDate(Date date, String formatType) {
        return new SimpleDateFormat(formatType).format(date);
    }


    /**
     * 格式化时间
     * 设置每个阶段时间
     */
    private static final int seconds_of_1minute = 60;

    private static final int seconds_of_30minutes = 30 * 60;

    private static final int seconds_of_1hour = 60 * 60;

    private static final int seconds_of_1day = 24 * 60 * 60;

    private static final int seconds_of_15days = seconds_of_1day * 15;

    private static final int seconds_of_30days = seconds_of_1day * 30;

    private static final int seconds_of_6months = seconds_of_30days * 6;

    private static final int seconds_of_1year = seconds_of_30days * 12;

    public static String getTimeRange(String mTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YMD_HMS_ONE);
        /*获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        Date startTime = null;
        try {
            /*将时间转化成Date*/
            curDate = sdf.parse(dataStrNew);
            startTime = sdf.parse(mTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*除以1000是为了转换成秒*/
        long between = (curDate.getTime() - startTime.getTime()) / 1000;
        int elapsedTime = (int) (between);
        if (elapsedTime < seconds_of_1minute) {

            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {

            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {

            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {

            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_15days) {

            return elapsedTime / seconds_of_1day + "天前";
        }
        if (elapsedTime < seconds_of_30days) {

            return "半个月前";
        }
        if (elapsedTime < seconds_of_6months) {

            return elapsedTime / seconds_of_30days + "月前";
        }
        if (elapsedTime < seconds_of_1year) {

            return "半年前";
        }
        return elapsedTime / seconds_of_1year + "年前";
    }

    /**
     * 把毫秒转换成：1:20:30这里形式
     */
    public static String stringForTime(int timeMs) {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;

        int minutes = (totalSeconds / 60) % 60;

        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 计算相差的小时
     */
    public String getTimeDifferenceHour(String starTime, String endTime, String format) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();
            String string = Long.toString(diff);

            float parseFloat = Float.parseFloat(string);

            float day1 = parseFloat / (60 * 60 * 1000);

            timeString = Float.toString(day1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timeString;
    }

    /**
     * 计算相差的天数
     */
    public static String getTimeDifferenceDays(String starTime, String endTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_YMD_HMS_ONE);

        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();
            String string = Long.toString(diff);

            float parseFloat = Float.parseFloat(string);

            int hour1 = Math.round(parseFloat / (24 * 60 * 60 * 1000));

            timeString = hour1 + "";
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timeString;

    }
}
