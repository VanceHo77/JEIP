/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dialog;

import com.utility.ServiceFactory;
import com.model.annatadetail.AnnAtaDetail;
import com.model.annatadetail.AnnAtaDetailService;
import com.model.announcementheader.AnnouncementHeader;
import com.model.announcementheader.AnnouncementHeaderService;
import java.io.IOException;
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
     * @param annID
     * @return
     * @throws IOException
     */
    @RequestMapping(method = RequestMethod.GET)
    protected String PageLoad(HttpSession session, Model model,
            @RequestParam("annid") String annID) throws Exception {
        //取得Service
        AnnouncementHeaderService announcementHeaderService = (AnnouncementHeaderService) ServiceFactory.getService("announcementHeaderService");
        AnnouncementHeader annHeader = new AnnouncementHeader();
        annHeader.setAnnID(Integer.parseInt(annID));
        model.addAttribute("annHeader", announcementHeaderService.findByOne(annHeader));
        AnnAtaDetailService annAtaDetailService = (AnnAtaDetailService) ServiceFactory.getService("annAtaDetailService");
        AnnAtaDetail annDetail = new AnnAtaDetail();
        annDetail.setAnnID(Integer.parseInt(annID));
        model.addAttribute("annAtaDetail", annAtaDetailService.findAnnAtaDetails(annDetail));

        return "Dialog/NewAnnouncement";
    }

}
