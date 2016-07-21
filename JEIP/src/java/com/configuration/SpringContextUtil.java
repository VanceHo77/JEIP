/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * applicationContext静態化 使用了ApplicationContextAware API的類別，如果受spring容器管理的話，
 * 那麼就會自動調用ApplicationContextAware中的setApplicationContext方法
 *
 * @author Vance
 */
public class SpringContextUtil implements ApplicationContextAware {

    /*
    ApplicationContext提供取得資源檔案更方便的方法。
    ApplicationContext提供文字訊息解析的方法，並支援國際化（Internationalization, I18N）訊息。
    ApplicationContext可以發佈事件，對事件感興趣的Bean可以接收到這些事件。
     */
    private static ApplicationContext applicationContext; // Spring上下文對象，類似BeanFactory

    /**
     * 實現ApplicationContextAware接口的回調方法，設置上下文環境
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 獲取Bean
     *
     * @param name
     * @return Object 一個已所给名字註冊的bean的實例
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }
}
