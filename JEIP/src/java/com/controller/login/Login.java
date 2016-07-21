/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.login;

import com.enumset.OpenAuth;
import com.enumset.UserLevel;
import com.exception.MyException;
import com.factory.ServiceFactory;
import com.model.userheader.UserHeader;
import com.model.userheader.UserHeaderService;
import com.google.gson.Gson;
import com.utility.MailUtility;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.UUID;
import javax.mail.MessagingException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 功能描述：登入畫面
 *
 * @author Vance
 */
@Controller
@RequestMapping(value = {"/", "/index"})
public class Login extends HttpServlet {

    private final Logger logger = Logger.getLogger(Login.class);
    //紀錄註冊表單的驗證結果
    private boolean[] validateAry = new boolean[3];

    /**
     * 登入畫面
     *
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET)
    protected String PageLoad(HttpSession session) throws Exception {
        UserHeader loginInfo = (UserHeader) session.getAttribute("LoginInfo");
        //若有session則直接導到Default頁面
        if (loginInfo != null) {
            return "redirect:Default";
        }
        return "Login/index";
    }

    /**
     * 登入驗證
     *
     * @param session
     * @param req
     * @param rps
     * @throws IOException
     * @throws Exception
     */
    @RequestMapping(value = "/Login", method = RequestMethod.POST)
    protected void Login(HttpSession session, HttpServletRequest req, HttpServletResponse rps) throws IOException, Exception {

        String account = req.getParameter("UserAccount").trim();
        String passWord = req.getParameter("Password").trim();

        String errrMsg = "";
        //<editor-fold defaultstate="collapsed" desc="驗證帳號密碼是否為空">
        if (account.isEmpty()) {
            errrMsg = "帳號、";
        }
        if (passWord.isEmpty()) {
            errrMsg += "密碼、";
        }
        if (!errrMsg.isEmpty()) {
            errrMsg = errrMsg.substring(0, errrMsg.lastIndexOf("、"));
            throw new MyException("請輸入：" + errrMsg);
        }
        //</editor-fold>
        //取得Service
        UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");

        UserHeader userHeader = new UserHeader();
        userHeader.setAccount(account);
        userHeader = userHeaderService.findByOne(userHeader);
        if (userHeader == null) {
            throw new MyException("查無使用者帳號。");
        }
        if (!passWord.equals(userHeader.getPassword().trim())) {
            throw new MyException("密碼錯誤。");
        }
        if (OpenAuth.未開通.getValue().equals(userHeader.getOpenAuth().trim())) {
            //寄送驗證信
            try {
                sendAuthEmail(req, userHeader.getEmail(), userHeader.getAccount(), userHeader.getAuthCode());
            } catch (Exception e) {
                //不處理發送失敗的錯誤
            }
            throw new MyException("此帳號尚未開通，請至註冊信箱中收取驗證信。");
        }
        //建立使用者session
        session.setAttribute("LoginInfo", userHeader);

        //網頁導到/Default
        rps.sendRedirect(req.getContextPath() + "/Default");
    }

    /**
     * 登入驗證
     *
     * @param session
     * @param req
     * @param rps
     * @throws IOException
     * @throws Exception
     */
    @RequestMapping(value = "/FBLogin", method = RequestMethod.POST)
    protected void FBLogin(HttpSession session, HttpServletRequest req, HttpServletResponse rps) throws IOException, Exception {

        String email = req.getParameter("hidEmail");
        //取得Service
        UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");

        UserHeader userHeader = new UserHeader();
        userHeader.setAccount(email);
        userHeader = userHeaderService.findByOne(userHeader);
        if (userHeader == null) {
            throw new MyException("查無使用者帳號。");
        }

        //建立使用者session
        session.setAttribute("LoginInfo", userHeader);

        //網頁導到/Default
        rps.sendRedirect(req.getContextPath() + "/Default");
    }

    /**
     * 註冊
     *
     * @param session
     * @param req
     * @param rps
     * @param userHeader
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/Signup", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    protected String Signup(HttpSession session, HttpServletRequest req, HttpServletResponse rps, @ModelAttribute UserHeader userHeader) throws Exception {
        //再次輸入密碼
        String passwdAgain = req.getParameter("txtPasswdAgain").trim();
        //取得系統時間
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());
        Gson gson = new Gson();
        HashMap rtnMap = new HashMap();

        //<editor-fold defaultstate="collapsed" desc="欄位驗證">
        if (!userHeader.getPassword().toLowerCase().equals(passwdAgain.toLowerCase())) {
            rtnMap.put("fail", "請再次確認密碼是否輸入正確");
            return gson.toJson(rtnMap);
        }
        //檢查帳號、名稱、信箱是否有通過檢查重複的驗證
        for (boolean b : validateAry) {
            if (b != true) {
                rtnMap.put("fail", "請重新輸入帳號、名稱、信箱。");
                return gson.toJson(rtnMap);
            }
        }
        //</editor-fold>

        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");

            //產生唯一的隨機識別碼
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //新增user
            userHeader.setUserLevel(UserLevel.訪客.getValue());
            userHeader.setOpenAuth(OpenAuth.未開通.getValue());
            userHeader.setAuthCode(uuid);
            userHeader.setModUserID(null);
            userHeader.setModTime(sysTime);
            userHeader.setCreTime(sysTime);

            if (userHeaderService.addUserHeader(userHeader)) {
                rtnMap.put("success", "註冊成功。");
                logger.info("系統增加了一位新的使用者[" + userHeader.getUserName() + "]");
            } else {
                rtnMap.put("fail", "註冊失敗。");
                logger.info("系統增加一位新的使用者[" + userHeader.getUserName() + "]時發生錯誤。");
                return gson.toJson(rtnMap);
            }

            //寄送驗證信
            sendAuthEmail(req, userHeader.getEmail(), userHeader.getAccount(), uuid);
        } catch (BeansException | MessagingException e) {
            rtnMap.put("fail", e.getMessage());
        }
        return gson.toJson(rtnMap);
    }

    /**
     * 使用FB註冊
     *
     * @param session
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/FBsignup", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    protected String FBsignup(HttpSession session, HttpServletRequest req, HttpServletResponse rps) throws Exception {
        String name = req.getParameter("name").trim();
        String email = req.getParameter("email").trim();

        Timestamp sysTime = new Timestamp(System.currentTimeMillis());
        Gson gson = new Gson();
        HashMap rtnMap = new HashMap();

        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            UserHeader userHeader = new UserHeader();
            userHeader.setAccount(email);
            //檢查是否已經註冊過會員
            userHeader = userHeaderService.findByOne(userHeader);
            if (userHeader != null) {
                rtnMap.put("fail", "您已經註冊過會員，5秒後頁面將重新導回登入畫面。");
                return gson.toJson(rtnMap);
            }
            //新增user
            userHeader = new UserHeader();
            userHeader.setAccount(email);//FB註冊時，帳號等於信箱
            userHeader.setPassword("");
            userHeader.setUserName(name);
            userHeader.setEmail(email);
            userHeader.setUserLevel(UserLevel.訪客.getValue());
            userHeader.setOpenAuth(OpenAuth.開通.getValue());
            userHeader.setAuthCode("");
            userHeader.setModUserID(null);
            userHeader.setModTime(sysTime);
            userHeader.setCreTime(sysTime);

            if (userHeaderService.addUserHeader(userHeader)) {
                rtnMap.put("success", "註冊成功。");
                logger.info("系統增加了一位新的使用者[" + name + "]");
            } else {
                rtnMap.put("fail", "註冊失敗。");
                logger.info("系統增加一位新的使用者[" + name + "]時發生錯誤。");
                return gson.toJson(rtnMap);
            }

        } catch (BeansException e) {
            rtnMap.put("fail", e.getMessage());
        }
        return gson.toJson(rtnMap);
    }

    /**
     * 開通會員
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/OpenAuth", method = RequestMethod.GET)
    protected String OpenAuth(HttpServletRequest req, HttpServletResponse rps) throws Exception {
        String account = req.getParameter("account");
        String authcode = req.getParameter("authcode");

        UserHeader userHeader = new UserHeader();
        userHeader.setAccount(account);
        userHeader.setAuthCode(authcode);
        userHeader.setOpenAuth(OpenAuth.未開通.getValue());

        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            //查詢該使用者
            userHeader = userHeaderService.findByOne(userHeader);
            userHeader.setOpenAuth(OpenAuth.開通.getValue());
            userHeaderService.updateUserHeader(userHeader);
        } catch (Exception e) {
            throw new MyException("查無使用者帳號");
        }

        return "OpenAuth";
    }

    /**
     * 忘記密碼
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ForgetPwd", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    protected String ForgetPwd(HttpServletRequest req, HttpServletResponse rps) throws Exception {
        String email = req.getParameter("txtEmail");
        email = email == null ? "" : email.trim();

        HashMap rtnMap = new HashMap();
        Gson gson = new Gson();

        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            UserHeader userHeader = new UserHeader();
            userHeader.setEmail(email);
            userHeader = userHeaderService.findByOne(userHeader);
            sendForgetPwdEmail(req, userHeader.getEmail(), userHeader.getPassword());
            rtnMap.put("success", "已成功寄出。");
        } catch (Exception e) {
            rtnMap.put("fail", "查無使用者信箱。");
        }
        return gson.toJson(rtnMap);
    }

    /**
     * 檢查帳號是否已被使用
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ChkAccount", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    protected String ChkAccount(HttpServletRequest req, HttpServletResponse rps) throws Exception {
        String account = req.getParameter("account");
        account = account == null ? "" : account.trim();

        HashMap rtnMap = new HashMap();
        Gson gson = new Gson();

        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            UserHeader userHeader = new UserHeader();
            userHeader.setAccount(account);
            userHeader.setDepHeader(null);
            userHeader = userHeaderService.findByOne(userHeader);
            if (userHeader == null) {
                validateAry[0] = true;
                rtnMap.put("success", "可使用此帳號。");
            } else {
                validateAry[0] = false;
                rtnMap.put("fail", "此帳號已被使用。");
            }
        } catch (Exception e) {
            throw e;
        }
        return gson.toJson(rtnMap);
    }

    /**
     * 檢查名稱是否已被使用
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ChkUserName", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    protected String ChkUserName(HttpServletRequest req, HttpServletResponse rps) throws Exception {
        String name = req.getParameter("name");
        name = name == null ? "" : name.trim();

        HashMap rtnMap = new HashMap();
        Gson gson = new Gson();

        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");
            UserHeader userHeader = new UserHeader();
            userHeader.setUserName(name);
            userHeader = userHeaderService.findByOne(userHeader);
            if (userHeader == null) {
                validateAry[1] = true;
                rtnMap.put("success", "可使用此名稱。");
            } else {
                validateAry[1] = false;
                rtnMap.put("fail", "此名稱已被使用。");
            }
        } catch (Exception e) {
            throw e;
        }
        return gson.toJson(rtnMap);
    }

    /**
     * 檢查信箱是否已被使用
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ChkEmail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    protected String ChkEmail(HttpServletRequest req, HttpServletResponse rps) throws Exception {
        String email = req.getParameter("email");
        email = email == null ? "" : email.trim();

        HashMap rtnMap = new HashMap();
        Gson gson = new Gson();

        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");

            UserHeader userHeader = new UserHeader();
            userHeader.setEmail(email);
            userHeader = userHeaderService.findByOne(userHeader);
            if (userHeader == null) {
                validateAry[2] = true;
                rtnMap.put("success", "可使用此信箱。");
            } else {
                validateAry[2] = false;
                rtnMap.put("fail", "此信箱已被使用。");
            }
        } catch (Exception e) {
            throw e;
        }
        return gson.toJson(rtnMap);
    }

    /**
     * 錯誤畫面
     *
     * @return
     */
    @RequestMapping(value = {"/ErrorPage"})
    protected String ErrorPage() {
        return "ErrorPage";
    }

    /**
     * 寄送驗證信
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    private void sendAuthEmail(HttpServletRequest req, String email, String account, String uuid) throws MessagingException {
        MailUtility mail = new MailUtility("");
        String authHref;
        //組合驗證網址
        authHref = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath()
                + "/OpenAuth?account=" + account + "&authcode=" + uuid;
        mail.sendMail(email, //收件人信箱
                "JEIP入口網站-會員開通驗證", //信件標題
                "您好，請點擊以下連結開通帳號<br/>"
                + "<a href='" + authHref + "' target='_blank'>開通連結網址</a>"//信件內容
        );
    }

    /**
     * 寄送忘記密碼信
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    private void sendForgetPwdEmail(HttpServletRequest req, String email, String pwd) throws MessagingException {
        MailUtility mail = new MailUtility("");
        mail.sendMail(email, //收件人信箱
                "JEIP入口網站會員-忘記密碼", //信件標題
                "您好，密碼如下：<br/>" + pwd//信件內容
        );
    }

}

//<editor-fold defaultstate="collapsed" desc="使用XML讀取Bean，已註解不使用此段程式">
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
//        UserHeaderService userHeaderService = (UserHeaderService) context.getBean("userHeaderService");
//        UserHeaderVO user = userHeaderService.findByKeyUserHeader(1);
//        message = user.getUserName();
        //</editor-fold>
