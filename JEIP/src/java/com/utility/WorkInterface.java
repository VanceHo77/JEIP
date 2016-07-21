/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 作業共同介面
 * @author Vance
 */
public interface WorkInterface {
    
    @RequestMapping(method = RequestMethod.GET)
    public String PageLoad(HttpServletRequest req, Model model) throws Exception ;
 
    @RequestMapping(params = {"new"})
    public String doNew(HttpServletRequest req, Model model) throws Exception;
    
    @RequestMapping(params = {"modify"})
    public String doModify(HttpServletRequest req, Model model) throws Exception;
        
    @RequestMapping(params = {"save"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody 
    String doSave(HttpServletRequest req, HttpServletResponse rps) throws Exception;
    
    @RequestMapping(params = {"update"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String doUpdate(HttpServletRequest req, HttpServletResponse rps) throws Exception;
    
    @RequestMapping(params = {"del"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String doDel(HttpServletRequest req, HttpServletResponse rps) throws Exception;

    @RequestMapping(value = {"/browse"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getBrowse(HttpServletRequest req, HttpServletResponse rps) throws  Exception;

    @RequestMapping(value = {"/query"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getSearch(HttpServletRequest req, HttpServletResponse rps) throws  Exception;
}
