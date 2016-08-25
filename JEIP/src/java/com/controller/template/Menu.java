/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.template;

import com.enumset.UserLevel;

/**
 * 左側功能選單
 * 
 * @author Vance
 */
public class Menu {

    public static String getMenuItems(String menu, String contextP) {
        StringBuilder htmlCode = new StringBuilder("");
        String roleName = UserLevel.getName(menu);
        UserLevel userlvl = UserLevel.valueOf(roleName);
        switch (userlvl) {
            case 系統管理員:
                if (menu.equals(UserLevel.系統管理員.getValue())) {
                    htmlCode.append("<a href='#'><i class='fa fa-wrench fa-fw'></i> 系統管理<span class='fa arrow'></span></a>")
                            .append("<ul class='nav nav-second-level collapse in' aria-expanded='true'>")
                            .append("<li> <a href='" + contextP + "/System/UserManager'>使用者設定</a></li>")
                            .append("<li> <a href='" + contextP + "/System/DepManager'>單位設定</a></li>")
                            .append("<li> <a href='" + contextP + "/System/AnnManager'>系統公告設定</a></li>")
                            .append("</ul>");
                }
                break;
        }
        return htmlCode.toString();
    }

}
