/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * 處理所有URL請求的錯誤事件-已不使用servlet的filter，改用Spring的ControllerAdvice(ControllerAdviceHandler.java)
 *
 * @author Vance
 */
//@WebFilter(filterName = "ExceptionFilter", urlPatterns = {"/*"})
public class ExceptionFilter implements Filter {

    private String filterName;
    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public void destroy() {
        log.debug("關閉 Exception Filter: " + filterName);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            filterName = config.getFilterName();
            log.debug("啟動 Exception Filter: " + filterName);
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
        try {
            chain.doFilter(req, rps);
        } catch (Exception e) {
            //因使用了@ControllerAdvice，故不再使用以下的錯誤捕捉方式，但在卡登入權限時仍然需要用到
            //，因此繼續保留這段程式碼
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) rps;
            HttpSession session = request.getSession();
            String[] errMsg = e.getMessage().split(":");
            String sysErrMsg = "", userErrMsg = "";
            //長度大於1表示有自訂exception訊息
            if (errMsg.length > 1) {
                userErrMsg = errMsg[1];
                session.setAttribute("SysErrMessage", "");
                session.setAttribute("UserErrMessage", userErrMsg);
                session.setAttribute("ReturnPath", request.getContextPath());
                req.getRequestDispatcher("/ErrorPage").forward(req, rps);
            } else {
                StackTraceElement[] traceAry = e.getStackTrace();
                for (StackTraceElement ele : traceAry) {
                    sysErrMsg += "ClassName：" + ele.getClassName()
                            + "<br/>MethodName：" + ele.getMethodName()
                            + "<br/>FileName：" + ele.getFileName()
                            + "<br/>LineNum：" + ele.getLineNumber() + "<br/><br/>";
                }
                //捕捉較常見的錯誤訊息
                if (e.getMessage().contains("NullPointerException")) {
                    userErrMsg = "欄位傳入Null值";
                }
                session.setAttribute("SysErrMessage", sysErrMsg);
                session.setAttribute("UserErrMessage", userErrMsg);
                session.setAttribute("ReturnPath", request.getRequestURI());
                response.sendRedirect(request.getContextPath() + "/ErrorPage");
            }

        }
    }

}
