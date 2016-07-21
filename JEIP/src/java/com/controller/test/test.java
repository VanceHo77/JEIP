/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.test;

import com.configuration.SpringContextUtil;
import com.model.testtable.TestTable;
import com.model.testtable.TestTableService;
import com.model.userheader.UserHeaderService;
import com.model.userheader.UserHeader;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Vance
 */
@Controller

public class test extends HttpServlet {

    /**
     *
     * @param req
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String helloWorld(HttpServletRequest req, Model model) {
        String message = "GGG";
         System.out.println("=================================================");
         System.out.println("中文參數測試" + req.getParameter("textField"));
        //<editor-fold defaultstate="collapsed" desc="使用XML讀取Bean，已註解不使用此段程式">
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
//        UserHeaderService userHeaderService = (UserHeaderService) context.getBean("userHeaderService");
//        UserHeaderVO user = userHeaderService.findByKeyUserHeader(1);
//        message = user.getUserName();
        //</editor-fold>
        //使用javaCode讀取Bean
        ApplicationContext appContext = SpringContextUtil.getApplicationContext();
        UserHeaderService userHeaderService = (UserHeaderService) appContext.getBean("userHeaderService");
//        UserHeader userVO = userHeaderService.findByKeyUserHeader(1);
//        message = userVO.getUserName();
//        message += "  " + userVO.getPassword();
            //TestTable Join UserHeader
//        List<TestTable> userHeaderList = userHeaderService.findAll();
    
//        for (TestTable objArray : userHeaderList) {

//            message += "<br/>Account:" + objArray[0].getAccount() + "<br/>";
//            message += "Password:" + objArray.getPassword() + "<br/>";
//            message += "UserName:" + objArray.getUserName() + "<br/>";
//            message += "Email:" + objArray.getEmail() + "<br/>";
//            message += "TestTable.ID:" + ((TestTable) objArray[1]).getTestID() + "<br/>";
//            message += "TestTable.TestName:" + ((TestTable) objArray[1]).getTestNam() + "<br/>";
//        }
        
        
        UserHeader userHeader = new UserHeader();
        userHeader.setUserID(2);
        userHeader.setUserName("堅皓 02/24 15:17");
        userHeader.setAccount("JianAccount");
        userHeader.setPassword("123pwd");
        userHeader.setUserLevel("2");
        userHeader.setEmail("123@hotmail");
        Timestamp t = new Timestamp(System.currentTimeMillis());
        userHeader.setCreTime(t);
        //更新資料
        userHeaderService.updateUserHeader(userHeader);

//        message += "I am UserHeade.UserName[2]: " + userHeaderService.findByKeyUserHeader(2).getUserName() + "<br/>";
        model.addAttribute("greeting", "<h1>Hello，" + req.getParameter("textField") + "</h1>");
        return "Login/default";
    }
}
