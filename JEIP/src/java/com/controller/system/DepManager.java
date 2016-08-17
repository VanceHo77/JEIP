/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.system;

import com.enumset.UnitLevel;
import com.utility.ServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.model.depheader.DepHeader;
import com.model.depheader.DepHeaderService;
import com.model.userheader.UserHeader;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 單位設定
 *
 * @author Vance
 */
@Controller
@RequestMapping(value = "/System/DepManager")
public class DepManager extends HttpServlet {

    /**
     * 初始化網頁
     *
     * @param model
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    public String PageLoad(HttpServletRequest req, Model model) throws Exception {
        return "System/DepManager_q";
    }

    /**
     * 新增單位
     *
     * @param req
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String doNew(HttpServletRequest req, Model model) throws Exception {
        return "System/DepManager_n";
    }

    /**
     * 維護畫面
     *
     * @param req
     * @param model
     * @param depID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String doModify(HttpServletRequest req, Model model, @RequestParam("depid") String depID) throws Exception {
        try {
            //取得Service
            DepHeaderService depHeaderService = (DepHeaderService) ServiceFactory.getService("depHeaderService");
            DepHeader depHeader = new DepHeader();
            depHeader.setDepID(depID);
            depHeader.setUnitLevel(-1);
            depHeader = depHeaderService.findByOne(depHeader);
            model.addAttribute("depHeader", depHeader);
        } catch (Exception e) {
            throw e;
        }
        return "/System/DepManager_u";
    }

    /**
     * 存檔
     *
     * @param session
     * @param req
     * @param rps
     * @param depHeader
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doSave(HttpSession session, HttpServletRequest req, HttpServletResponse rps,
            @ModelAttribute DepHeader depHeader) throws Exception {
        int userID = ((UserHeader) session.getAttribute("LoginInfo")).getUserID();
        //取得系統時間
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());
        
        JsonObject jsonObj = new JsonObject();
        try {
            //取得Service
            DepHeaderService depHeaderService = (DepHeaderService) ServiceFactory.getService("depHeaderService");
            depHeader.setModTime(sysTime);
            depHeader.setModUserID(userID);
            depHeader.setCreTime(sysTime);
            if (depHeaderService.addDepHeader(depHeader)) {
                jsonObj.addProperty("success", "存檔成功。");
            } else {
                jsonObj.addProperty("fail", "存檔失敗。");
            }
        } catch (Exception e) {
            throw e;
        }
        return jsonObj.toString();
    }

    /**
     * 更新
     *
     * @param session
     * @param req
     * @param rps
     * @param depHeader
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doUpdate(HttpSession session, HttpServletRequest req, HttpServletResponse rps,
            @ModelAttribute DepHeader depHeader) throws Exception {
        int userID = ((UserHeader) session.getAttribute("LoginInfo")).getUserID();
        //取得系統時間
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());
        JsonObject jsonObj = new JsonObject();
        try {
            //取得Service
            DepHeaderService depHeaderService = (DepHeaderService) ServiceFactory.getService("depHeaderService");
            DepHeader depObj = new DepHeader();
            depObj.setDepID(depHeader.getDepID());
            depObj = depHeaderService.findByOne(depObj);
            depObj.setDepName(depHeader.getDepName());
            depObj.setUnitLevel(depHeader.getUnitLevel());
            depObj.setModUserID(userID);
            depObj.setModTime(sysTime);
            if (depHeaderService.updateDepHeader(depObj)) {
                jsonObj.addProperty("success", "更新成功。");
            } else {
                jsonObj.addProperty("fail", "更新失敗。");
            }
        } catch (Exception e) {
            throw e;
        }
        return jsonObj.toString();
    }

    /**
     * 刪除
     *
     * @param req
     * @param rps
     * @param depID
     * @return
     * @throws Exception
     */
    @RequestMapping(params = {"del"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doDel(HttpServletRequest req, HttpServletResponse rps, @RequestParam("depid") String depID) throws Exception {
        JsonObject jsonObj = new JsonObject();
        try {
            //取得Service
            DepHeaderService depHeaderService = (DepHeaderService) ServiceFactory.getService("depHeaderService");
            DepHeader depHeader = new DepHeader();
            depHeader.setDepID(depID);
            depHeader = depHeaderService.findByOne(depHeader);
            if (depHeaderService.deleteDepHeader(depHeader)) {
                jsonObj.addProperty("success", "刪除成功。");
            } else {
                jsonObj.addProperty("fail", "刪除失敗。");
            }
        } catch (Exception e) {
            throw e;
        }
        return jsonObj.toString();
    }

    /**
     * 瀏覽資料
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/browse", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getBrowse(HttpServletRequest req, HttpServletResponse rps) throws Exception {
        JsonArray jsonArray = new JsonArray();
        try {
            //取得Service
            DepHeaderService depHeaderService = (DepHeaderService) ServiceFactory.getService("depHeaderService");
            List<DepHeader> list = depHeaderService.findAll();
            
            list.stream().map((DepHeader depObj) -> {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("DepID", depObj.getDepID());
                jsonObj.addProperty("DepName", depObj.getDepName());
                jsonObj.addProperty("UnitLevel", UnitLevel.getName(depObj.getUnitLevel()));
                return jsonObj;
            }).forEach(jsonArray::add);
        } catch (Exception e) {
            throw e;
        }
        return jsonArray.toString();
    }
    
}
