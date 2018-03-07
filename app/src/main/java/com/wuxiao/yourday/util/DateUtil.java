package com.wuxiao.yourday.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lihuabin on 2016/8/18.
 */
public class DateUtil {

    private static Calendar mCalendar = Calendar.getInstance();
    private static DateUtil singleton;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DateUtil() {
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
    }

    public static  Date getDateFromString(String date){
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     *格式化成 2016-11-07 的格式
     * @param date
     * @return
     */
    public static String formatDay(Date date){
        return sdf.format(date);
    }

    /**
     * 获得今天的日期
     * @return String 例如 2016-08-28
     */
    public static String getToday() {
        mCalendar.setTime(new Date(System.currentTimeMillis()));//初始化日历为当前时间
        int y = mCalendar.get(Calendar.YEAR);
        String m = (mCalendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (mCalendar.get(Calendar.MONTH) + 1) : (mCalendar.get(Calendar.MONTH) + 1) + "";
        String d = mCalendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + mCalendar.get(Calendar.DAY_OF_MONTH) : mCalendar.get(Calendar.DAY_OF_MONTH) + "";

        return y + "-" + m + "-" + d;
    }

    /**
     * 获取本周的日期集合，周日为每周第一天
     * @return ArrayList<String> 例如[2016-08-28, 2016-08-29, 2016-08-30, 2016-08-31, 2016-09-01, 2016-09-02, 2016-09-03]
     */
    public static ArrayList<String> getThisWeek() {
        return getWeekOfDate(getToday());
    }

    /**
     * 获取指定月的天数
     * @param month 指定月
     * @return int 如 31
     */
    public static int getMaxDayOfMonth(int month) {
        mCalendar.set(mCalendar.get(Calendar.YEAR), month, 1);
        mCalendar.add(Calendar.DAY_OF_MONTH, -1);
        int maxDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.setTime(new Date(System.currentTimeMillis()));//回复日期到正确日期

        return maxDay;
    }

    /**
     * 获取指定的日期那一周的日期集合，周日为每周第一天
     * @param date 指定的日期 如 "2016-08-29"
     * @return ArrayList<String> 例如[2016-08-28, 2016-08-29, 2016-08-30, 2016-08-31, 2016-09-01, 2016-09-02, 2016-09-03]
     */
    public static ArrayList<String> getWeekOfDate(String date){
        Log.i("date",date);
        ArrayList<String> week = new ArrayList<String>();
        //获取当前选中的时间
        int year = Integer.parseInt(date.subSequence(0, 4).toString());
        int month = Integer.parseInt(date.subSequence(5, 7).toString());//真实月份，DatePicker中月份从零开始算，使用时注意
        int day = Integer.parseInt(date.subSequence(8, 10).toString());

        mCalendar.set(year,month-1,day);
        int whichDayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);//获取当天星期几
        Log.i("whichDayOfWeek",whichDayOfWeek+"");
        for (int i = (whichDayOfWeek-1); i <= whichDayOfWeek; i--) {
            if (i < 0) break;//防止无限循环
            mCalendar.add(Calendar.DAY_OF_MONTH, -i);
            int y = mCalendar.get(Calendar.YEAR);
            String m = (mCalendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (mCalendar.get(Calendar.MONTH) + 1) : "" + (mCalendar.get(Calendar.MONTH) + 1);
            String d = mCalendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + mCalendar.get(Calendar.DAY_OF_MONTH) : "" + mCalendar.get(Calendar.DAY_OF_MONTH);
            mCalendar.setTime(new Date(System.currentTimeMillis()));

            week.add(y + "-" + m + "-" + d);
            Log.i("week",week.toString());
        }

        for (int i = 1; i <= (7 - whichDayOfWeek); i++) {
            mCalendar.add(Calendar.DAY_OF_MONTH, i);
            int y = mCalendar.get(Calendar.YEAR);
            String m = (mCalendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (mCalendar.get(Calendar.MONTH) + 1) : "" + (mCalendar.get(Calendar.MONTH) + 1);
            String d = mCalendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + mCalendar.get(Calendar.DAY_OF_MONTH) : "" + mCalendar.get(Calendar.DAY_OF_MONTH);
            mCalendar.setTime(new Date(System.currentTimeMillis()));

            week.add(y + "-" + m + "-" + d);
        }
        return week;
    }

    /**
     * 获得今天的日期
     * @return String 例如 2016-08-28
     */
    public static String getThisMonthFirstDay() {
        mCalendar.setTime(new Date(System.currentTimeMillis()));//初始化日历为当前时间
        int y = mCalendar.get(Calendar.YEAR);
        String m = (mCalendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (mCalendar.get(Calendar.MONTH) + 1) : (mCalendar.get(Calendar.MONTH) + 1) + "";
        
        return y + "-" + m + "-01" ;
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTimeNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String s=sdf.format(System.currentTimeMillis());
        return s;
    }
}
