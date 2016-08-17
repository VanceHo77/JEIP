/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.rest;

import com.enumset.AtaType;
import com.exception.MyException;
import com.filetransfer.CreateFilePath;
import com.google.gson.JsonObject;
import com.model.userheader.UserHeader;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 處理檔案的上、下傳
 *
 * @author Vance
 */
@RestController
public class FileREST {

    /**
     * 檢查AP暫存檔是否存在
     *
     * @param session
     * @param req
     * @param ataType
     * @return
     * @throws java.lang.Exception
     */
    @RequestMapping(value = "/file/isAPTempFileExists", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String isAPTempFileExists(HttpSession session, HttpServletRequest req, @RequestParam("ataType") String ataType) throws Exception {
        JsonObject jsonObj = new JsonObject();
        //取得使用者資訊
        UserHeader LOGININFO = ((UserHeader) session.getAttribute("LoginInfo"));
        AtaType AtaTypeEnum;
        //取得AtaType euum
        try {
            AtaTypeEnum = AtaType.valueOf(ataType);
        } catch (Exception e) {
            throw new MyException("系統無法分辨附件類別。");
        }
        try {
            //建立AP暫存檔路徑物件
            Path tempPath = new CreateFilePath().GetAPTempPath(
                    req.getSession().getServletContext().getRealPath("/resources/temp"),
                    LOGININFO, AtaTypeEnum, "", "");
            //檢查是否有上傳附件
            File books = new File(tempPath.toString());
            if (books.listFiles().length == 0) {
                throw new MyException("系統找不到附件暫存檔。");
            }
            jsonObj.addProperty("success", "OK");
        } catch (Exception e) {
            throw e;
        }
        return jsonObj.toString();
    }

    

    /**
     * 檔案下載
     *
     * @param session
     * @param req
     * @param rps
     * @throws java.io.UnsupportedEncodingException
     */
    @RequestMapping(value = "/file/doDownload", method = RequestMethod.POST, produces = "application/octet-stream;charset=UTF-8")
    public void doDownload(HttpSession session, HttpServletRequest req, HttpServletResponse rps) throws UnsupportedEncodingException, Exception {
        //取得使用者資訊
        UserHeader LOGININFO = ((UserHeader) session.getAttribute("LoginInfo"));
        String annID = req.getParameter("annID");
        String fileName = req.getParameter("fileName");
        String ataType = req.getParameter("ataType");

        try {
            //建立AtaFile目錄
            Path tempPath = new CreateFilePath().GetAtaFilePath(LOGININFO, AtaType.表單, annID, fileName);

            byte[] data = Files.readAllBytes(tempPath);

            rps.setHeader("content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            try (ServletOutputStream out = rps.getOutputStream()) {
                out.write(data, 0, data.length);
                out.flush();
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
