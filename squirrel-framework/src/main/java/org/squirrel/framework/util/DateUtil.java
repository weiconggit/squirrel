package org.squirrel.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理
 * @author weicong
 * @time   2022年10月19日
 * @version 1.0
 */
public class DateUtil {

    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取日期加时间的字符串
     *
     * @param date
     * @return
     */
    public static String convertDateToString(Date date) {
        if (date == null)
            return null;
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return df.format(date);
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            throw new ParseException("日期格式错误: " + dateString , 0);
        }
        return date;
    }
}
