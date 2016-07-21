/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utility;

import com.enumset.UnitLevel;
import com.enumset.UserLevel;
import com.factory.ServiceFactory;
import com.model.depheader.DepHeader;
import com.model.depheader.DepHeaderService;
import java.util.List;

/**
 * 下拉選單
 *
 * @author Vance
 */
public class DroupDownList {

    /**
     * 身分
     *
     * @return
     */
    public static UserLevel[] getUserLvlList() {
        return UserLevel.values();
    }

    /**
     * 單位
     *
     * @return
     */
    public static List<DepHeader> getDepList() {
        //取得Service
        DepHeaderService depHeaderService = (DepHeaderService) ServiceFactory.getService("depHeaderService");
        List<DepHeader> depList = depHeaderService.findAll();
        return depList;
    }

    //單位層級
    public static UnitLevel[] getUnitLevelList() {
        return UnitLevel.values();
    }
}
