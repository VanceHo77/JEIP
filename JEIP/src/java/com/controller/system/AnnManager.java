/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.system;

import com.enumset.AnnAtaType;
import com.enumset.AtaType;
import com.factory.ServiceFactory;
import com.filetransfer.CreateFilePath;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.model.annatadetail.AnnAtaDetail;
import com.model.announcementheader.AnnouncementHeader;
import com.model.announcementheader.AnnouncementHeaderService;
import com.model.userheader.UserHeader;
import com.utility.MyLib;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 系統公告設定
 *
 * @author Vance
 */
@Controller
@RequestMapping(value = "/System/AnnManager")
public class AnnManager extends HttpServlet {

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
        return "System/AnnManager_q";
    }

    /**
     * 新增系統公告
     *
     * @param req
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String doNew(HttpServletRequest req, Model model) throws Exception {
        return "System/AnnManager_n";
    }

    /**
     * 維護畫面
     *
     * @param req
     * @param model
     * @param annid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String doModify(HttpServletRequest req, Model model, @RequestParam("annid") Integer annid) throws Exception {
        try {
            //取得Service
            AnnouncementHeaderService announcementheaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
            AnnouncementHeader announcementHeader = new AnnouncementHeader();
            announcementHeader.setAnnID(annid);
            announcementHeader = announcementheaderService.findByOne(announcementHeader);
            model.addAttribute("annHeader", announcementHeader);
        } catch (Exception e) {
            throw e;
        }
        return "/System/AnnManager_u";
    }

    /**
     * 存檔
     *
     * @param session
     * @param req
     * @param rps
     * @param announcementHeader
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doSave(HttpSession session, HttpServletRequest req, HttpServletResponse rps,
            @ModelAttribute AnnouncementHeader announcementHeader) throws Exception {
        UserHeader LOGININFO = ((UserHeader) session.getAttribute("LoginInfo"));
        //取得系統時間
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());

        JsonObject jsonObj = new JsonObject();
        try {
            //取得Service
            AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
            announcementHeader.setBegTime(announcementHeader.getBegTime().replace(",", " "));
            announcementHeader.setEndTime(announcementHeader.getEndTime().replace(",", " "));
            announcementHeader.setModUserID(LOGININFO.getUserID());
            announcementHeader.setModTime(sysTime);
            announcementHeader.setCreTime(sysTime);
            //取得AP暫存檔路徑 
            Path tempPath = new CreateFilePath().GetAPTempPath(
                    req.getSession().getServletContext().getRealPath("/resources/temp"),
                    LOGININFO, AtaType.系統公告);
            //取得目錄底下所有檔案清單
            File folder = new File(tempPath.toFile().getPath());
            String[] list = folder.list();
            //檢查AP暫存檔是否有附件
            if (list.length < 1) {
                jsonObj.addProperty("fail", "附件上傳至檔案伺服器時已遺失。");
                return jsonObj.toString();
            }
            //開始上傳至FileServer，先略過上傳到fileServer這段
//            HttpPost httpPost = new HttpPost("http://localhost:8081" + req.getContextPath() + "/file/fileupload");//負責處理上傳的servlet
//            CloseableHttpClient httpclient;
//            CloseableHttpResponse response = null;
//            httpclient = HttpClients.createDefault();
//            response =  httpclient.execute(httpPost);
//            response.getStatusLine();
            Integer id = announcementHeaderService.addAnnAndGetID(announcementHeader);
            if (announcementHeaderService.addAnnAndGetID(announcementHeader) > 0) {
                //新增 AnnAtaDetail
                AnnAtaDetail annAtaDetail = new AnnAtaDetail();
                annAtaDetail.setAnnID(id);
                annAtaDetail.setSeqNO(1);
                annAtaDetail.setAtaFileName("XXX");
                annAtaDetail.setAtaType(AnnAtaType.附件.getValue());
                annAtaDetail.setCretime(sysTime);
                annAtaDetail.setModtime(sysTime);
                annAtaDetail.setModUserID(LOGININFO.getUserID());
                List<AnnAtaDetail> lstAnnAta = new ArrayList<>();
                lstAnnAta.add(annAtaDetail);
                announcementHeader.setAnnAtaDetails(lstAnnAta);

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
     * @param announcementHeader
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doUpdate(HttpSession session, HttpServletRequest req, HttpServletResponse rps,
            @ModelAttribute AnnouncementHeader announcementHeader) throws Exception {
        int userID = ((UserHeader) session.getAttribute("LoginInfo")).getUserID();
        //取得系統時間
        Timestamp sysTime = new Timestamp(System.currentTimeMillis());

        JsonObject jsonObj = new JsonObject();
        try {
            //取得Service
            AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
            AnnouncementHeader annObj = new AnnouncementHeader();
            annObj.setAnnID(announcementHeader.getAnnID());
            annObj = announcementHeaderService.findByOne(annObj);
            annObj.setBegTime(announcementHeader.getBegTime().replace(",", " "));
            annObj.setEndTime(announcementHeader.getEndTime().replace(",", " "));
            annObj.setAnnouncementDesc(announcementHeader.getAnnouncementDesc());
            annObj.setModUserID(userID);
            annObj.setModTime(sysTime);
            if (announcementHeaderService.addAnnouncementHeader(annObj)) {
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
     * @param annid
     * @return
     * @throws Exception
     */
    @RequestMapping(params = {"del"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doDel(HttpServletRequest req, HttpServletResponse rps, @RequestParam("annid") Integer annid) throws Exception {
        JsonObject jsonObj = new JsonObject();
        try {
            //取得Service
            AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
            AnnouncementHeader annObj = new AnnouncementHeader();
            annObj.setAnnID(annid);
            annObj = announcementHeaderService.findByOne(annObj);
            if (announcementHeaderService.deleteAnnouncementHeader(annObj)) {
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
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/browse", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getBrowse() throws Exception {
        JsonArray jsonArray = new JsonArray();
        try {
            //取得Service
            AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
            List<AnnouncementHeader> list = announcementHeaderService.findBegTimeBetween(MyLib.sysDateTime());
            list.stream().map((AnnouncementHeader annObj) -> {
                JsonObject jsonObj = new JsonObject();
                jsonObj.addProperty("AnnID", annObj.getAnnID());
                jsonObj.addProperty("AnnouncementDesc", annObj.getAnnouncementDesc());
                jsonObj.addProperty("BegTime", String.valueOf(annObj.getBegTime()) + "<input type='hidden' id='hidAnnID' value='" + annObj.getAnnID() + "' />");
                jsonObj.addProperty("EndTime", String.valueOf(annObj.getEndTime()));
                return jsonObj;
            }).forEach(jsonArray::add);

        } catch (Exception e) {
            throw e;
        }
        return jsonArray.toString();
    }

    /**
     * 上傳附件至AP
     *
     * @param session
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fileUploadAPTemp", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String doFileUploadAPTemp(HttpSession session, HttpServletRequest req, HttpServletResponse rps) throws Exception {
        //取得使用者資訊
        UserHeader LOGININFO = ((UserHeader) session.getAttribute("LoginInfo"));
        //檔名
        String[] fileName = req.getParameterValues("fileName");
        //上傳附件
        Collection<Part> fileParts = req.getParts();
        JsonObject jsonObj;
        JsonArray jsonnArray = new JsonArray();
        int i = 0;
        for (Part p : fileParts) {
            //因Parts物件會包含非上傳的資料，所以要濾掉
            if (p.getContentType() == null) {
                continue;
            }
            jsonObj = new JsonObject();
            int read;
            Path tempPath = new CreateFilePath().GetAPTempPath(
                    req.getSession().getServletContext().getRealPath("/resources/temp"),
                    LOGININFO, AtaType.系統公告);
            try (InputStream in = p.getInputStream()) {
                try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                    byte[] temp = new byte[4096];
                    while ((read = in.read(temp)) >= 0) {
                        buffer.write(temp, 0, read);
                    }
                    byte[] data = buffer.toByteArray();
                    //檢查資料夾是否存在
                    if (!tempPath.toFile().exists()) {
                        tempPath.toFile().mkdirs();//建立資料夾
                    }
                    //輸出至AP Temp
                    try (FileOutputStream out = new FileOutputStream(Paths.get(tempPath.toFile().getPath(), fileName[i]).toString())) {
                        out.write(data);
                    }
                }
            } catch (IOException | NullPointerException e) {
                jsonObj.addProperty("fail", "附件上傳失敗。系統發生以下錯誤：<br>" + e.getMessage());
            }
            jsonObj.addProperty("filename", fileName[i]);
            jsonnArray.add(jsonObj);
            i++;
        }
        return jsonnArray.toString();
    }

}
