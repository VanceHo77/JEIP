/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.system;

import com.enumset.AnnAtaType;
import com.enumset.AtaType;
import com.utility.ServiceFactory;
import com.filetransfer.CreateFilePath;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.model.annatadetail.AnnAtaDetail;
import com.model.annatadetail.AnnAtaDetailService;
import com.model.announcementheader.AnnouncementHeader;
import com.model.announcementheader.AnnouncementHeaderService;
import com.model.userheader.UserHeader;
import com.websocket.WebSocketNewAnn;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
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
        String[] fileNames = req.getParameter("fileNames").split(",");
        boolean flag = true;
        try {
            //取得Service
            AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
            announcementHeader.setBegTime(announcementHeader.getBegTime().replace(",", " "));
            announcementHeader.setEndTime(announcementHeader.getEndTime().replace(",", " "));
            announcementHeader.setModUserID(LOGININFO.getUserID());
            announcementHeader.setModTime(sysTime);
            announcementHeader.setCreTime(sysTime);
            if (AnnAtaType.附件.getValue().equals(announcementHeader.getAtaType())
                    || AnnAtaType.連結.getValue().equals(announcementHeader.getAtaType())) {

                Integer id = announcementHeaderService.addAnnAndGetID(announcementHeader);

                if (AnnAtaType.附件.getValue().equals(announcementHeader.getAtaType())) {
                    //取得AP暫存檔路徑 
                    Path tempPath = new CreateFilePath().GetAPTempPath(
                            req.getSession().getServletContext().getRealPath("/resources/temp"),
                            LOGININFO, AtaType.系統公告, "", "");
                    //取得目錄底下所有檔案清單
                    File folder = new File(tempPath.toFile().getPath());
                    String[] list = folder.list();
                    //檢查AP暫存檔是否有附件
                    if (list != null && list.length < 1) {
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
                    doFileTransfer(LOGININFO, tempPath, list, id.toString());
                    //刪除暫存檔附件
                    for (File file : folder.listFiles()) {
                        file.delete();
                    }
                }

                if (id > 0) {
                    AnnAtaDetailService annAtaDetailService = (AnnAtaDetailService) ServiceFactory.getService("annAtaDetailService");
                    //新增 AnnAtaDetail
                    if (AnnAtaType.附件.getValue().equals(announcementHeader.getAtaType())) {
                        for (int i = 1; i <= fileNames.length; i++) {
                            AnnAtaDetail annAtaDetail = new AnnAtaDetail();
                            annAtaDetail.setAnnID(id);
                            annAtaDetail.setSeqNO(i);
                            annAtaDetail.setAtaFileName(fileNames[i - 1]);
                            annAtaDetail.setCretime(sysTime);
                            annAtaDetail.setModtime(sysTime);
                            annAtaDetail.setModUserID(LOGININFO.getUserID());
                            flag = annAtaDetailService.addAnnAtaDetail(annAtaDetail);
                        }
                    } else {
                        AnnAtaDetail annAtaDetail = new AnnAtaDetail();
                        annAtaDetail.setAnnID(id);
                        annAtaDetail.setSeqNO(1);
                        annAtaDetail.setAtaFileName(req.getParameter("fileName").trim());
                        annAtaDetail.setCretime(sysTime);
                        annAtaDetail.setModtime(sysTime);
                        annAtaDetail.setModUserID(LOGININFO.getUserID());
                        flag = annAtaDetailService.addAnnAtaDetail(annAtaDetail);
                    }
                    if (!flag) {
                        jsonObj.addProperty("fail", "系統公告附件存檔失敗。");
                        return jsonObj.toString();
                    }
                }
                if (id > 0) {
                    jsonObj.addProperty("success", "存檔成功。");
                } else {
                    jsonObj.addProperty("fail", "存檔失敗。");
                    return jsonObj.toString();
                }
            } else if (announcementHeaderService.addAnnouncementHeader(announcementHeader)) {
                jsonObj.addProperty("success", "存檔成功。");
                //將公告內容推播給所以使用者
                new WebSocketNewAnn().sendNewAnnMsgToAll(announcementHeader.getAnnouncementDesc());
            } else {
                jsonObj.addProperty("fail", "存檔失敗。");
                return jsonObj.toString();
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
            annObj.setAtaType(announcementHeader.getAtaType());
            annObj.setBegTime(announcementHeader.getBegTime().replace(",", " "));
            annObj.setEndTime(announcementHeader.getEndTime().replace(",", " "));
            annObj.setAnnouncementDesc(announcementHeader.getAnnouncementDesc());
            annObj.setModUserID(userID);
            annObj.setModTime(sysTime);
            if (announcementHeaderService.updateAnnouncementHeader(annObj)) {
//            if (announcementHeaderService.addAnnouncementHeader(annObj)) {
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
            List<AnnouncementHeader> list = announcementHeaderService.findAll();
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
     * 於維護作業時取得附件明細
     *
     * @param session
     * @param req
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAtaDetails", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAtaDetails(HttpSession session, HttpServletRequest req,
            @RequestParam("annid") Integer annID, @RequestParam("atatype") String ataType) throws Exception {
        JsonObject jsonTable = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        try {
            //取得Service
            AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
            AnnAtaDetailService annAtaDetailService = (AnnAtaDetailService) ServiceFactory.getService("annAtaDetailService");

            AnnouncementHeader announcementHeader = new AnnouncementHeader();
            announcementHeader.setAnnID(annID);
            announcementHeader = announcementHeaderService.findByOne(announcementHeader);

            AnnAtaDetail annDetail = new AnnAtaDetail();
            annDetail.setAnnID(annID);
            List<AnnAtaDetail> list = annAtaDetailService.findAnnAtaDetails(annDetail);
            if (null != list && !list.isEmpty()) {
                if (AnnAtaType.連結.getValue().equals(announcementHeader.getAtaType())) {
                    list.stream().map((AnnAtaDetail d) -> d.getAtaFileName()).forEach((String name) -> {
                        JsonObject jsonObj = new JsonObject();
                        if (!name.contains("http://")) {
                            name = "http://" + name;
                        }
                        jsonObj.addProperty("ataFileName", "<a href='" + name + "' target='_blank'>" + name + "</a>");
                        jsonArray.add(jsonObj);
                    });
                } else {
                    list.stream().map((AnnAtaDetail d) -> d.getAtaFileName()).forEach((String name) -> {
                        JsonObject jsonObj = new JsonObject();
                        jsonObj.addProperty("ataFileName", name);
                        jsonArray.add(jsonObj);
                    });
                }
            }
            jsonTable.addProperty("AtaType", announcementHeader.getAtaType());
            jsonTable.addProperty("JsonData", jsonArray.toString());
        } catch (Exception e) {
            throw e;
        }
        return jsonTable.toString();
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
                    LOGININFO, AtaType.系統公告, "", "");
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

    /**
     * 刪除已上傳附件
     *
     * @param session
     * @param req
     * @param rps
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delAtaFile", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String delAtaFile(HttpSession session, HttpServletRequest req, HttpServletResponse rps) throws Exception {
        //取得使用者資訊
        UserHeader LOGININFO = ((UserHeader) session.getAttribute("LoginInfo"));
        //檔名
        String fileName = req.getParameter("fileName");
        JsonObject jsonObj;
        jsonObj = new JsonObject();
        try {
            Path tempPath = new CreateFilePath().GetAPTempPath(
                    req.getSession().getServletContext().getRealPath("/resources/temp"),
                    LOGININFO, AtaType.系統公告, "", "");
            File file = new File(tempPath.toFile().getPath() + "/" + fileName);
            if (file.exists()) {
                if (file.delete()) {
                    jsonObj.addProperty("success", "附件刪除成功。");
                } else {
                    jsonObj.addProperty("fail", "附件刪除失敗。");
                }
            } else {
                jsonObj.addProperty("fail", "刪除失敗，附件不存在暫存檔中。");
            }
        } catch (Exception e) {
            throw e;
        }
        return jsonObj.toString();
    }

    @Test
    public void testDoModify() throws Exception {
        AnnouncementHeader announcementHeader = new AnnouncementHeader();
        try {
            //取得Service
            AnnouncementHeaderService announcementheaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");

            announcementHeader.setAnnID(40);
            announcementHeader = announcementheaderService.findByOne(announcementHeader);
        } catch (Exception e) {
            throw e;
        }
        assertEquals("Result:", announcementHeader);
        fail("The test case is a prototype.");
    }

    //附件搬檔至C:/MyProject/AtaFile
    private void doFileTransfer(UserHeader LOGININFO, Path tempPath, String[] list, String seqNO) throws IOException {
        byte[] data;
        Path inPath;
        Path outPath;
        //檢查資料夾是否存在
        outPath = new CreateFilePath().GetAtaFilePath(LOGININFO, AtaType.表單, seqNO, "");
        if (!outPath.toFile().exists()) {
            outPath.toFile().mkdirs();//建立資料夾
        }
        for (String fileName : list) {
            inPath = new File(tempPath.toFile().getPath() + "/" + fileName).toPath();
            data = Files.readAllBytes(inPath);
            outPath = new CreateFilePath().GetAtaFilePath(LOGININFO, AtaType.表單, seqNO, fileName);
            try (BufferedOutputStream bufOut = new BufferedOutputStream(new FileOutputStream(outPath.toFile().getPath()))) {
                bufOut.write(data);
                bufOut.flush();
            }
        }
    }
}
