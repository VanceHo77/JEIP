/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enumset;

/**
 * 使用者的權限
 *
 * @author Vance
 */
public enum UserLevel {
    訪客("訪客", "0"),
    系統管理員("系統管理員", "1"),
    董事長("董事長", "2"),
    副董事長("副董事長", "3"),
    總經理("總經理", "4"),
    副總經理("副總經理", "5"),
    部長("部長", "6"),
    一般職員("一般職員", "7");

    private String name;       //顯示的文字
    private String value;       //DB中記的內容值

    // 建構方法
    private UserLevel(String oname, String ovalue) {
        this.name = oname;
        this.value = ovalue;
    }

    //用名稱來取value
    public static String getValue(String name) {
        for (UserLevel u : UserLevel.values()) {
            if (u.name.equals(name)) {
                return u.value;
            }
        }
        return "";
    }

    //用value來取名稱
    public static String getName(String value) {
        for (UserLevel u : UserLevel.values()) {
            if (u.value.equals(value)) {
                return u.name;
            }
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserLevel{" + "name=" + name + ", value=" + value + '}';
    }

}
