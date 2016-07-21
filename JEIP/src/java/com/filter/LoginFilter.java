/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.filter;

import com.model.userheader.UserHeader;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * 處理沒有經過登入權限的控管事件-已不使用servlet的filter，改用Spring的ControllerAdvice(LoginInterceptor.java)
 *
 * @author Vance
 */
//@WebFilter(filterName = "LoginFilter", urlPatterns = {"/Default", "/System/*", "/Dialog/*"})
public class LoginFilter implements Filter {

    private String filterName;
    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public void destroy() {
        log.debug("關閉 Login Filter: " + filterName);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            filterName = config.getFilterName();
            log.debug("啟動 Login Filter: " + filterName);
        } catch (Exception e) {
            log.error("讀取權限控制檔案失敗。", e);
        }
    }

    /**
     *
     * @param req
     * @param rps
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse rps, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        UserHeader loginInfo = (UserHeader) session.getAttribute("LoginInfo");
        //若沒有session則直接導到Default頁面
        if (loginInfo == null) {
            //用exception的方式倒回登入畫面
//            throw new RuntimeException(new Exception("請先登入系統"));
            throw new RuntimeException("請先登入系統");
        }
        //繼續執行接下來的程式
        chain.doFilter(request, rps);
    }

}
