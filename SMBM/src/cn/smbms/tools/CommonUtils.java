package cn.smbms.tools;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    /**
     * 时间格式化为字符串
     * @param date 时间
     * @return 字符串
     */
    public static String format(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 字符串格式化成时间
     * @param dateString 时间字符串
     * @param format 时间格式
     * @return
     */
    public static Date parseDate(String dateString, String format) {
        Date date = null;
        if (!StringUtils.isEmpty(dateString)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                date = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 获取当前系统时间
     * @return
     */
    public static Date newDate(){
        return new Date();
    }

}
