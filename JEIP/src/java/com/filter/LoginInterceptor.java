/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.filter;

import com.exception.MyException;
import com.model.userheader.UserHeader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 登入權限控管
 * 
 * @author Vance
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final String[] FILTER_URL_PATTERNS = {"/Default", "/System", "/Dialog"};

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse rps, Object obj) throws Exception {
        //user請求路徑
        String contextPath = req.getRequestURI().toString();
        HttpSession session = req.getSession();
        UserHeader loginInfo = (UserHeader) session.getAttribute("LoginInfo");
        if (loginInfo == null) {
            for (String url : FILTER_URL_PATTERNS) {
                if (contextPath.contains(url)) {
                    throw new MyException("請先登入系統。");
                }
            }
        }
        return true;
    }

}
