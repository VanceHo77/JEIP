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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
     * @param postURL
     * @param filebytes
     * @return
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
                    LOGININFO, AtaTypeEnum);
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
     * 檔案上傳
     *
     * @param postURL
     * @param filebytes
     * @return
     */
    @RequestMapping(value = "/file/fileupload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String upload(HttpSession session, HttpServletRequest req, String postURL, byte[] filebytes) throws UnsupportedEncodingException {
        //取得使用者資訊
        UserHeader LOGININFO = ((UserHeader) session.getAttribute("LoginInfo"));
        JsonObject jsonObj = new JsonObject();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        httpClient = HttpClients.createDefault();
//        //負責處理上傳的servlet
//        HttpPost httpPost = new HttpPost(session.getServletContext() + "/file/fileUploadToServer");
//        // 把文件轉換成流對象FileBody
//        FileBody bin = new FileBody(new File("C:/log.txt"));
//        Path tempPath = new CreateFilePath().GetAPTempPath(
//                req.getSession().getServletContext().getRealPath("/resources/temp"),
//                LOGININFO, AtaTypeEnum);
//
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.addBinaryBody("file", bin);
//        httpPost.setEntity(builder.build());
//
//        httpPost.setEntity(reqEntity);
//
//        // 發起請求並返回請求的響應
//        response = httpClient.execute(httpPost);
//
//        System.out.println("The response value of token:" + response.getFirstHeader("token"));
//
//        // 獲取響應對象
//        HttpEntity resEntity = response.getEntity();
//        if (resEntity != null) {
//            // 打印響應長度
//            System.out.println("Response content length: " + resEntity.getContentLength());
//            // 打印響應內容
//            System.out.println(EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
//        }
//
//        // 銷毀
//        EntityUtils.consume(resEntity);

        return jsonObj.toString();
    }
}
