/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enumset;

/**
 * 單位層級
 *
 * @author Vance
 */
public enum UnitLevel {
    核示單位("核示單位", 1),
    一般單位("一般單位", 2);

    private String name;       //顯示的文字
    private int value;       //DB中記的內容值

    // 建構方法
    private UnitLevel(String oname, int ovalue) {
        this.name = oname;
        this.value = ovalue;
    }

    //用名稱來取value
    public static int getValue(int name) {
        for (UnitLevel u : UnitLevel.values()) {
            if (u.name.equals(name)) {
                return u.value;
            }
        }
        return -1;
    }

    //用value來取名稱
    public static String getName(int value) {
        for (UnitLevel u : UnitLevel.values()) {
            if (u.value == value) {
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OpenStatus{" + "name=" + name + ", value=" + value + '}';
    }
}
