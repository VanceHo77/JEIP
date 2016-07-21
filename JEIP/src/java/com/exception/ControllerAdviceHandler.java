/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exception;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

/**
 * Exception異常處理<br>
 * 備註：要注意回傳的訊息有沒有包含html非法字元，若有(Exception:Unexpected token)會導致自訂的錯誤頁面無法正常顯示
 * 
 * @author Vance
 */
@ControllerAdvice
public class ControllerAdviceHandler extends DefaultHandlerExceptionResolver {
    //自訂錯誤頁面
    private static final String ERROR_PAGE = "/ErrorPage";
    ModelAndView modelView = new ModelAndView(ERROR_PAGE);
    private String exceptionName = "";
    private String sysErrMsg;
    private String userErrMsg = "";

    /**
     * 處理自訂錯誤
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MyException.class})
    public ModelAndView exceptionHandler(HttpServletRequest req, MyException e) {
        userErrMsg = e.getMessage().replace("\"", "'");
        modelView.addObject("ExceptionName", "");
        modelView.addObject("SysErrMessage", "");
        modelView.addObject("UserErrMessage", userErrMsg);
        modelView.addObject("ReturnPath", req.getContextPath());
        return modelView;
    }

    /**
     * 處理較常遇到的錯誤
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class, NumberFormatException.class, NullPointerException.class})
    public ModelAndView exceptionHandler(HttpServletRequest req, Exception e) {
        exceptionName = e.getClass().getSimpleName();
        userErrMsg = e.getMessage().replace("\"", "'");
        StackTraceElement[] traceAry = e.getStackTrace();
        for (StackTraceElement ele : traceAry) {
            sysErrMsg += "ClassName：" + ele.getClassName()
                    + "<br/>MethodName：" + ele.getMethodName()
                    + "<br/>FileName：" + ele.getFileName()
                    + "<br/>LineNum：" + ele.getLineNumber() + "<br/><br/>";
        }
        modelView.addObject("ExceptionName", "異常類型：" + exceptionName);
        modelView.addObject("SysErrMessage", sysErrMsg);
        modelView.addObject("UserErrMessage", userErrMsg);
        modelView.addObject("ReturnPath", req.getRequestURI());

        return modelView;
    }

    /**
     * 處理找不到頁面(404)的錯誤
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView pageNotFoundHandler(HttpServletRequest req, Exception e) {
        exceptionName = e.getClass().getSimpleName();
        userErrMsg = e.getMessage().replace("\"", "'");
        sysErrMsg = "找不到頁面。";
        modelView.addObject("ExceptionName", "異常類型：" + exceptionName);
        modelView.addObject("SysErrMessage", "找不到頁面。");
        modelView.addObject("UserErrMessage", userErrMsg);
        modelView.addObject("ReturnPath", req.getRequestURI());
        return modelView;
    }

}
