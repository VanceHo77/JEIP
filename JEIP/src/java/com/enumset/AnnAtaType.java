/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enumset;

/**
 * 系統公告的附件類型
 *
 * @author Vance
 */
public enum AnnAtaType {
    連結("連結", "L"),
    附件("附件", "F");

    private String name;       //顯示的文字
    private String value;       //DB中記的內容值

    // 建構方法
    private AnnAtaType(String oname, String ovalue) {
        this.name = oname;
        this.value = ovalue;
    }

    //用名稱來取value
    public static String getValue(String name) {
        for (AnnAtaType u : AnnAtaType.values()) {
            if (u.name.equals(name)) {
                return u.value;
            }
        }
        return "";
    }

    //用value來取名稱
    public static String getName(String value) {
        for (AnnAtaType u : AnnAtaType.values()) {
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
        return "AtaType{" + "name=" + name + ", value=" + value + '}';
    }

}
