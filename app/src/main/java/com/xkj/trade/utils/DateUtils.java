package com.xkj.trade.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by huangsc on 2016-12-01.
 * TODO:时间格式工具类
 */
public class DateUtils {
    public static String getXTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));
        return sdf.format(new Date(time * 1000));
    }

    /**
     * @author huangsc
     * Created at 2016-11-16 10:46
     * 10位 unix时间戳
     */
    public static String getCurrentTimeNoS() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取当前时间yy:MM:dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrenTime() {
        return DateUtils.getShowTimeNoTimeZone(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * 获取以手机当前时区的时间yyyyMMddHHmmss
     *
     * @param date
     * @return
     */
    public static String getShowTime(Long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date(date));
    }
    /**
     * 获取以手机当前时区的时间formatType
     *
     * @param date
     * @return
     */
    public static String getShowTime(Long date,String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        return format.format(new Date(date));
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 获取时间天、小时单为位
     *
     * @param time
     * @return
     */
    public static String getXTimeDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH");
        sdf.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));
        return sdf.format(new Date(time * 1000));
    }

    /**
     * @author huangsc
     * <p>
     * 获取当前时间格式:=
     */
    public static String getXTShowTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));
        return sdf.format(new Date(time));
    }

    public static String getShowTime(Date date) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    }

    /**
     * @author huangsc
     * 获取当前时间格式:=
     */
    public static String getShowTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));
        return sdf.format(new Date(time));
    }


    /**
     * @author huangsc.
     * 获取当前时间格式没有时区
     */
    public static String getShowTimeNoTimeZone(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }

    /**
     * 获取订单开始时间
     *
     * @param open_time
     * @return
     */
    public static long getOrderStartTime(String open_time) {
        return getOrderStartTime(open_time, "yyyy-MM-dd HH:mm:ss");
    }

    public static long getOrderStartTime(String open_time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        sdf.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));
        try {
            return sdf.parse(open_time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取订单开始时间
     *
     * @param open_time
     * @return
     */
    public static long getOrderStartTimeNoTimeZone(String open_time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long time = sdf.parse(open_time).getTime();
            return sdf.parse(open_time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return createGmtOffsetString(true, true, tz.getRawOffset());
    }

    private static String createGmtOffsetString(boolean includeGmt,
                                                boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
//        appendNumber(builder, 2, offsetMinutes % 60);
        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }

}
