/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dialog;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.utility.ServiceFactory;
import com.model.announcementheader.AnnouncementHeader;
import com.model.announcementheader.AnnouncementHeaderService;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 首頁的最新公告(查看看多)
 *
 * @author Vance
 */
@Controller
@RequestMapping(value = "/Dialog/ShowNewAnnDetail")
public class ShowNewAnnDetail extends HttpServlet {

    /**
     * 載入網頁
     *
     * @param session
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET)
    protected String PageLoad(HttpSession session, Model model) throws Exception {
        return "Dialog/ShowNewAnnDetail";
    }

    /**
     * 查詢資料
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "query", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doQuery(HttpServletRequest req) throws Exception {
        JsonArray jsonArray = new JsonArray();
        String queryStr = req.getParameter("queryStr").trim();
        JsonObject rtnJson = new JsonObject();
        try {
            //取得Service
            AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
            List<AnnouncementHeader> list;
            if (queryStr.isEmpty()) {
                list = announcementHeaderService.findAll();
            } else {
                AnnouncementHeader annHeader = new AnnouncementHeader();
                annHeader.setAnnouncementDesc(queryStr);
                list = announcementHeaderService.query(annHeader);
            }
            if (list != null && !list.isEmpty()) {
                list.stream().map((AnnouncementHeader annObj) -> {
                    JsonObject jsonObj = new JsonObject();
                    jsonObj.addProperty("AnnouncementDesc", annObj.getAnnouncementDesc() + "<input type='hidden' id='hidAnnID' value='" + annObj.getAnnID() + "' />");
                    jsonObj.addProperty("BegTime", String.valueOf(annObj.getBegTime()));
                    return jsonObj;
                }).forEach(jsonArray::add);
            }
            if (jsonArray.size() == 0) {
                rtnJson.addProperty("fail", "目前結果查無資料。");
            } else {
                rtnJson.addProperty("success", jsonArray.toString());
            }
        } catch (Exception e) {
            throw e;
        }
        return rtnJson.toString();
    }

}
