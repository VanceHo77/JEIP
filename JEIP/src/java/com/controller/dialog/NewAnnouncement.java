/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dialog;

import java.io.IOException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首頁的最新公告的詳細內容
 *
 * @author Vance
 */
@Controller
@RequestMapping(value = "/Dialog/NewAnnouncement")
public class NewAnnouncement extends HttpServlet {

    /**
     * 載入網頁
     *
     * @param session
     * @param model
     * @param desc
     * @param begTime
     * @param endTime
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET)
    protected String PageLoad(HttpSession session, Model model,
            @RequestParam("desc") String desc,
            @RequestParam("begTime") String begTime,
            @RequestParam("endTime") String endTime) throws Exception {
        model.addAttribute("Desc", URLDecoder.decode(desc, "utf-8"));
        model.addAttribute("BegTime", URLDecoder.decode(begTime, "utf-8"));
        model.addAttribute("EndTime", URLDecoder.decode(endTime, "utf-8"));
        return "Dialog/NewAnnouncement";
    }

}
