/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utility;

import java.sql.Timestamp;

/**
 * 通用工具箱
 *
 * @author Vance
 */
public class MyLib {

    /**
     * 字串左補0
     *
     * @author Vance
     * @param zeroStr
     * @param value
     * @param length
     * @return
     */
    public static String left(String zeroStr, String value, int length) {
        String rtnStr = "";
        if (!value.trim().isEmpty()) {
            int maxLength = (zeroStr + value).length();
            value = (zeroStr + value);
            rtnStr = value.substring(maxLength - length, maxLength);
        }
        return rtnStr;
    }

    /**
     * 取得當前系統日期+時間
     *
     * @return "YYY/MM/DD hh/mm"
     */
    public static String sysDateTime() {
        return MyLib.sysDate() + " " + MyLib.sysTime();
    }

    /**
     * 取得當前系統日期
     *
     * @return "YYY/MM/DD"
     */
    public static String sysDate() {
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());
        String date = sysTime.toString().split(" ")[0];
        date = MyLib.sysYear(date) + "/" + MyLib.sysMonth(date) + "/" + MyLib.sysDay(date);
        return date;
    }

    /**
     * 取得當前系統時間
     *
     * @return "hh/mm"
     */
    public static String sysTime() {
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());
        String time = sysTime.toString().split(" ")[1];
        time = time.substring(0, time.lastIndexOf(":"));
        return time;
    }

    /**
     * 取得當前系統年度(民國年)
     *
     * @param date
     * @return "hh/mm"
     */
    public static String sysYear(String date) {
        date = date.split("-")[0];
        date = String.valueOf(Integer.parseInt(date) - 1911);
        return date;
    }

    /**
     * 取得當前系統月份
     *
     * @param date
     * @return "hh/mm"
     */
    public static String sysMonth(String date) {
        return date.split("-")[1];
    }

    /**
     * 取得當前系統年度(民國年)
     *
     * @param date
     * @return "hh/mm"
     */
    public static String sysDay(String date) {
        return date.split("-")[2];
    }

}
