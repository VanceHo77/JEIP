/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.template;

import com.controller.login.Login;
import com.exception.MyException;
import com.utility.ServiceFactory;
import com.model.announcementheader.AnnouncementHeader;
import com.model.announcementheader.AnnouncementHeaderService;
import com.model.userheader.UserHeader;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;

/**
 * 模板頁面
 *
 * @author Vance
 */
@Controller
@RequestMapping(value = "/Default")
public class Template extends HttpServlet {

    private final Logger logger = Logger.getLogger(Login.class);

    /**
     * 預設登入後的畫面
     *
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    protected String PageLoad(HttpSession session, Model model) throws Exception {
        UserHeader userInfo = (UserHeader) session.getAttribute("LoginInfo");
        //左側選單
        session.setAttribute("Menu", userInfo.getUserLevel());
        try {
            //取得Service
            AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");

            List<AnnouncementHeader> list = announcementHeaderService.findAll();

            //最新公告筆數
            model.addAttribute("NewAnnMsgTotal", list.size());
            //只取前三筆最新公告
            if (list.size() < 4) {
                model.addAttribute("NewAnnMsg", list);
            } else {
                model.addAttribute("NewAnnMsg", list.subList(0, 3));
            }
        } catch (Exception e) {
            throw new MyException("取得最新公告失敗！");
        }
        return "Default";
    }

    /**
     * 登出
     *
     * @param session
     * @param req
     * @param rps
     * @throws Exception
     */
    @RequestMapping(value = "/Logout", method = RequestMethod.GET)
    protected void Logout(HttpSession session, HttpServletRequest req, HttpServletResponse rps) throws Exception {
        String userName = "";
        try {
            userName = ((UserHeader) session.getAttribute("LoginInfo")).getUserName();
            //清除使用者資訊
            req.getSession().invalidate();
            logger.info("使用者[" + userName + "]登出成功。");
        } catch (Exception e) {
            logger.error("使用者[" + userName + "]登出失敗。");
            logger.error("登出失敗的錯誤訊息：" + e.getMessage());
        }
        //網頁導到/index
        rps.sendRedirect(req.getContextPath());
    }
}
