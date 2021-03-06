<%-- 
    Document   : UserManager
    Created on : 2016/4/18, 下午 02:59:52
    Author     : Vance
--%>

<%@page import="com.utility.DroupDownList"%>
<%@page import="com.model.depheader.DepHeader"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.enumset.UserLevel"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>主控台-使用者設定</title>
    </head>
    <%final String contextP = request.getContextPath();%>
    <script>
        $(document).ready(function () {
        <%-- 套用驗證 --%>
            $("#formAddUser").validate();
        });
    </script>
    <body>
        <%-- jsp程式碼集中區 --%>
        <%
            UserLevel[] userLevel = DroupDownList.getUserLvlList();
            List<DepHeader> lstDep = DroupDownList.getDepList();
        %>
        <%!
            //下拉選單-身分
            StringBuilder GetUserLvlList(UserLevel[] UserLevel) {
                StringBuilder htmlCode = new StringBuilder("");
                String name, value;
                String selected;
                for (UserLevel lvlObj : UserLevel) {
                    name = lvlObj.getName();
                    value = lvlObj.getValue();
                    if ("一般職員".equals(name)) {
                        selected = "selected='selected'";
                    } else {
                        selected = "";
                    }
                    htmlCode.append("<option value='" + value + "'").append(selected).append(">")
                            .append(name)
                            .append("</option>");
                }
                return htmlCode;
            }

            //下拉選單-單位
            StringBuilder GetDepList(List<DepHeader> lstDep) {
                StringBuilder htmlCode = new StringBuilder("");
                String name, value;
                for (DepHeader dep : lstDep) {
                    name = dep.getDepName();
                    value = dep.getDepID();
                    htmlCode.append("<option value='" + value + "'").append(">")
                            .append(name)
                            .append("</option>");
                }
                return htmlCode;
            }
        %>
        <div>
            <form id="formAddUser" name="formAddUser" class="form-horizontal formDig" action="<%=contextP%>/System/UserManager?save" method="post">
                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">帳號</label>
                    <div class="col-md-5">
                        <label>${defAccount}</label>
                        <input type="hidden" name="account" value="${defAccount}"/>
                    </div>
                    <div class="col-md-4" id="divAccountErr"></div>
                </div>

                <div class="form-group">
                    <label for="password" class="col-md-3 control-label">預設密碼</label>
                    <div class="col-md-5">
                        <label style="color:gray;">(同帳號)</label>
                    </div>
                </div>

                <div class="form-group">
                    <label for="lastname" class="col-md-3 control-label">名稱</label>
                    <div class="col-md-5">
                        <input type="text" class="form-control required" id="txtName" name="userName"  placeholder="請輸入名稱" required>
                    </div>
                    <div class="col-md-4" id="divNameErr"></div>
                </div>

                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">Email</label>
                    <div class="col-md-5">
                        <input type="email" class="form-control email" id="txtEmail" name="email"  placeholder="請輸入Email" required>
                    </div>
                    <div class="col-md-4" id="divEmailErr"></div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">所屬單位</label>
                    <div class="col-md-5">
                        <select class="form-control" id="lstDepID" name="depID">
                            <option value=""></option>
                            <%=GetDepList(lstDep)%>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">使用者身分</label>
                    <div class="col-md-5">
                        <select class="form-control" name="userLevel">
                            <%=GetUserLvlList(userLevel)%>
                        </select>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
