/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.system;

import com.enumset.OpenAuth;
import com.enumset.UserLevel;
import com.utility.ServiceFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.model.autouseraccount.AutoUserAccount;
import com.model.autouseraccount.AutoUserAccountService;
import com.model.depheader.DepHeader;
import com.model.userheader.UserHeader;
import com.model.userheader.UserHeaderService;
import com.utility.MyLib;
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
 * 使用者設定
 *
 * @author Vance
 */
@Controller
@RequestMapping(value = "/System/UserManager")
public class UserManager extends HttpServlet {

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
        return "System/UserManager_q";

    }

    /**
     * 新增使用者
     *
     * @param req
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String doNew(HttpServletRequest req, Model model) throws Exception {
        model.addAttribute("defAccount", getDefAccount());
        return "System/UserManager_n";
    }

    /**
     * 維護畫面
     *
     * @param req
     * @param model
     * @param account
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modify", params = "account", method = RequestMethod.GET)
    public String doModify(HttpServletRequest req, Model model, @RequestParam("account") String account) throws Exception {
        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            UserHeader userHeader = new UserHeader();
            userHeader.setAccount(account);
            userHeader = userHeaderService.findByOne(userHeader);
            model.addAttribute("userHeader", userHeader);
        } catch (Exception e) {
            throw e;
        }
        return "/System/UserManager_u";
    }

    /**
     * 存檔
     *
     * @param session
     * @param req
     * @param rps
     * @param userHeader
     * @param depHeader
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doSave(HttpSession session, HttpServletRequest req, HttpServletResponse rps,
            @ModelAttribute UserHeader userHeader, @ModelAttribute DepHeader depHeader) throws Exception {
        int userID = ((UserHeader) session.getAttribute("LoginInfo")).getUserID();
        //取得系統時間
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());

        JsonObject jsonObj = new JsonObject();
        boolean saveFlag = false;
        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            userHeader.setPassword(userHeader.getAccount());
            userHeader.setDepHeader(depHeader);
            userHeader.setOpenAuth(OpenAuth.開通.getValue());
            userHeader.setModUserID(userID);
            userHeader.setModTime(sysTime);
            userHeader.setCreTime(sysTime);
            if (userHeaderService.addUserHeader(userHeader)) {
                saveFlag = true;
                jsonObj.addProperty("success", "存檔成功。");
            } else {
                jsonObj.addProperty("fail", "存檔失敗。");
            }
            //異動Table:系統預設帳號
            AutoUserAccountService autoUserAccountService = (AutoUserAccountService) ServiceFactory.getService("autoUserAccountService");
            AutoUserAccount autoUserAccount = autoUserAccountService.findByOne();

            if (autoUserAccount == null && saveFlag == true) {
                autoUserAccount = new AutoUserAccount();
                autoUserAccount.setAccount(userHeader.getAccount());
                autoUserAccount.setModUserID(userID);
                autoUserAccount.setModtime(sysTime);
                autoUserAccount.setCretime(sysTime);
                autoUserAccountService.addAutoUserAccount(autoUserAccount);
            } else if (autoUserAccount != null && saveFlag == true) {
                autoUserAccount.setAccount(userHeader.getAccount());
                autoUserAccount.setModUserID(userID);
                autoUserAccount.setModtime(sysTime);
                autoUserAccountService.updateAutoUserAccount(autoUserAccount);
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
     * @param userHeader
     * @param depHeader
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doUpdate(HttpSession session, HttpServletRequest req, HttpServletResponse rps,
            @ModelAttribute UserHeader userHeader, @ModelAttribute DepHeader depHeader) throws Exception {
        int userID = ((UserHeader) session.getAttribute("LoginInfo")).getUserID();
        //取得系統時間
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());

        JsonObject jsonObj = new JsonObject();
        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            UserHeader userObj = new UserHeader();
            userObj.setAccount(userHeader.getAccount());
            userObj = userHeaderService.findByOne(userObj);
            userObj.setUserName(userHeader.getUserName());
            userObj.setEmail(userHeader.getEmail());
            userObj.setUserLevel(userHeader.getUserLevel());
            userObj.setDepHeader(depHeader);
            userObj.setModUserID(userID);
            userObj.setModTime(sysTime);
            if (userHeaderService.updateUserHeader(userObj)) {
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
     * @param account
     * @return
     * @throws Exception
     */
    @RequestMapping(params = {"del"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doDel(HttpServletRequest req, HttpServletResponse rps, @RequestParam("account") String account) throws Exception {
        JsonObject jsonObj = new JsonObject();
        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            UserHeader userObj = new UserHeader();
            userObj.setAccount(account);
            userObj = userHeaderService.findByOne(userObj);
            if (userHeaderService.deleteUserHeader(userObj)) {
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
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            List<UserHeader> list = userHeaderService.findAll();
            list.stream().map((UserHeader userObj) -> {
                JsonObject jsonObj = new JsonObject();
                String depName = userObj.getDepHeader() == null ? "" : userObj.getDepHeader().getDepName();
                jsonObj.addProperty("Account", userObj.getAccount());
                jsonObj.addProperty("UserName", userObj.getUserName());
                jsonObj.addProperty("Email", userObj.getEmail());
                jsonObj.addProperty("Dep", depName);
                jsonObj.addProperty("UserLvl", UserLevel.getName(userObj.getUserLevel()));
                return jsonObj;
            }).forEach(jsonArray::add);

        } catch (Exception e) {
            throw e;
        }
        return jsonArray.toString();
    }

    /*取得系統預設帳號*/
    private String getDefAccount() {
        String defAccount;
        String defAccount_1 = "A";
        int defAccount_2 = 1;
        //取得Service
        AutoUserAccountService autoUserAccountService = (AutoUserAccountService) ServiceFactory.getService("autoUserAccountService");
        AutoUserAccount autoUserAccount = autoUserAccountService.findByOne();

        if (autoUserAccount == null) {
            defAccount = defAccount_1 + MyLib.left("0000000", String.valueOf(defAccount_2), 7);

        } else {
            String account = autoUserAccount.getAccount();
            defAccount_2 = Integer.parseInt(account.substring(1, account.length())) + 1;
            defAccount = defAccount_1 + MyLib.left("0000000", String.valueOf(defAccount_2), 7);
        }

        return defAccount;
    }

}
