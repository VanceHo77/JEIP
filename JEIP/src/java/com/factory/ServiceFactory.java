/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.factory;

import com.configuration.SpringContextUtil;
import org.springframework.context.ApplicationContext;

/**
 * Service的工廠
 *
 * 
 * 
 * 設計模式之Singleton(單態)
 * Singleton模式主要作用是保證在Java應用程式中，一個Class只有一個實例存在。
 * 一個實例表示是單線程，在很多操作中，比如建立目錄 資料庫連接都需要單線程操作，Singleton模式經常用於控制對系統資源的控制,我們常常看到工廠模式中工廠方法也用Singleton模式實現的。
 * 
 * @author Vance
 */
public class ServiceFactory {

    //Beans管理員
    private static final ApplicationContext APPCONTEXT = SpringContextUtil.getApplicationContext();
    
    public static Object getService(String beanName) {
        return APPCONTEXT.getBean(beanName);
    }
}
