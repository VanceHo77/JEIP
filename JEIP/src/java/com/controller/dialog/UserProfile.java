/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dialog;

import com.factory.ServiceFactory;
import com.google.gson.Gson;
import com.model.userheader.UserHeader;
import com.model.userheader.UserHeaderService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 右上角個人資料維護
 *
 * @author Vance
 */
@Controller
@RequestMapping(value = "/Dialog/UserProfile")
public class UserProfile extends HttpServlet {

    /**
     * 載入網頁
     *
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET)
    protected String PageLoad(HttpSession session) throws IOException {
        return "Dialog/UserProfile";
    }

    /**
     * 讀取圖片
     *
     * @param session
     * @param req
     * @param res
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/GetPhoto", method = RequestMethod.GET, produces = "image/gif;charset=UTF-8")
    protected void doGetImage(HttpSession session, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletOutputStream out = res.getOutputStream();
        byte[] byteAry;
        byte[] temp = new byte[4096];
        int read;
        InputStream in = null;

        UserHeader userInfo = (UserHeader) session.getAttribute("LoginInfo");
        byteAry = userInfo.getPhoto();
        //沒有照片則讀取預設圖片
        if (byteAry == null || byteAry.length == 0) {
            //檔案的真實路徑：
            //C:\MyProject\MyKM\build\web\resources\img\bg1.jpg
            String defImgPath = req.getSession().getServletContext().getRealPath("/") + "resources/img/defUserPhoto.jpg";
            File defImg = new File(defImgPath);
            BufferedImage bufferedImg = ImageIO.read(defImg);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImg, "jpg", baos);
            baos.flush();
            byte[] originalImgByte = baos.toByteArray();
            in = new ByteArrayInputStream(originalImgByte);
        } else {//有照片則讀取user上傳的
            in = new ByteArrayInputStream(byteAry);
        }
        try {
            while ((read = in.read(temp)) >= 0) {
                out.write(temp, 0, read);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 存檔
     *
     * @param session
     * @param req
     * @param rps
     * @param formUserHeader
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    @RequestMapping(params = "Save", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    protected void doSave(HttpSession session, HttpServletRequest req, HttpServletResponse rps, @ModelAttribute UserHeader formUserHeader) throws Exception {
        //取得使用者資訊
        UserHeader LOGININFO = ((UserHeader) session.getAttribute("LoginInfo"));
        //取得系統時間
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());
        //取得使用者上傳照片
        Part uploadPhoto = req.getPart("fileUpload");
        //舊密碼
        String oldPwd = req.getParameter("txtOldPassword") == null ? "" : req.getParameter("txtOldPassword").trim();
        //新密碼
        String newPwd = req.getParameter("txtNewPassword") == null ? "" : req.getParameter("txtNewPassword").trim();
        HashMap rtnMsg = new HashMap();
        int read;
        try {
            //取得Service
            UserHeaderService userHeaderService = (UserHeaderService) ServiceFactory.getService("userHeaderService");

            UserHeader userHeader = userHeaderService.findByOne(LOGININFO);
            userHeader.setUserName(formUserHeader.getUserName().trim());
            userHeader.setEmail(formUserHeader.getEmail().trim());
            userHeader.setModTime(sysTime);
            userHeader.setModUserID(LOGININFO.getUserID());

            //修改密碼
            if (!oldPwd.isEmpty() && !newPwd.isEmpty()) {
                userHeader.setPassword(newPwd);
            }

            try (InputStream in = uploadPhoto.getInputStream()) {
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                    byte[] temp = new byte[4096];
                    while ((read = in.read(temp)) >= 0) {
                        buffer.write(temp, 0, read);
                    }
                    byte[] data = buffer.toByteArray();
                    if (data.length != 0) {
                        //有上傳圖片
                        userHeader.setPhoto(data);
                    }
                }
            }

            //更新資料
            if (userHeaderService.updateUserHeader(userHeader)) {

                rtnMsg.put("success", "更新成功。");
            } else {
                rtnMsg.put("fail", "更新失敗。");
            }
            //更新session
            session.setAttribute("LoginInfo", userHeader);
        } catch (IOException | BeansException e) {
            throw e;
        }
        //顯示存檔結果
        session.setAttribute("UserProfileSaveRS", rtnMsg);
        //網頁導到/Default
        rps.sendRedirect(req.getContextPath() + "/Default");
    }

    /**
     * 檢查舊密碼
     *
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/ChkOldPwd", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    protected String ChkAccount(HttpServletRequest req, HttpServletResponse rps) throws Exception {
        UserHeader userInfo = (UserHeader) req.getSession().getAttribute("LoginInfo");
        String oldPwd = req.getParameter("oldPwd");
        oldPwd = oldPwd == null ? "" : oldPwd.trim();

        HashMap rtnMap = new HashMap();
        Gson gson = new Gson();
        //檢查輸入的密碼是否正確
        if (userInfo.getPassword().equals(oldPwd)) {
            rtnMap.put("success", "OK");
        } else {
            rtnMap.put("fail", "與舊密碼不符");
        }
        return gson.toJson(rtnMap);
    }

}
