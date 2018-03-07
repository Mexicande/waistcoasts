package com.wuxiao.yourday.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 格式化工具类
 * Created by lihuabin on 2016/10/26.
 */
public class FormatUtil {
    public static String getTwoDecimalString(double num){
        NumberFormat formater = new DecimalFormat("#.00");
        formater.setGroupingUsed(false);
        String returnstr = formater.format(num);
        if (returnstr.startsWith(".")) {
            returnstr = "0" + returnstr;
        } else if (returnstr.startsWith("-.")) {
            returnstr = "-0" + returnstr.substring(1, returnstr.length());
        }
        return returnstr;
    }

    public static String getTwoDecimalString(String num){
        NumberFormat formater = new DecimalFormat("#.00");
        formater.setGroupingUsed(false);
        String returnstr = formater.format(Double.parseDouble(num));
        if (returnstr.startsWith(".")) {
            returnstr = "0" + returnstr;
        } else if (returnstr.startsWith("-.")) {
            returnstr = "-0" + returnstr.substring(1, returnstr.length());
        }
        return returnstr;
    }
}
