<%-- 
    Document   : DepManager
    Created on : 2016/4/18, 下午 02:59:52
    Author     : Vance
--%>

<%@page import="com.enumset.UnitLevel"%>
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
        <title>主控台-單位設定</title>
    </head>
    <%final String contextP = request.getContextPath();%>
    <script>
        $(document).ready(function () {
        <%-- 套用驗證 --%>
            $("#formAddDep").validate();
        });
    </script>
    <body>
        <%-- jsp程式碼集中區 --%>
        <%
            DepHeader depHeader = (DepHeader) request.getAttribute("depHeader");
            UnitLevel[] unitLvl = DroupDownList.getUnitLevelList();
            int defUnitLvl = depHeader.getUnitLevel();
        %>
        <%!
            //下拉選單-單位
            StringBuilder GetUnitLevel(UnitLevel[] unitLvlList, int defUnitLvl) {
                StringBuilder htmlCode = new StringBuilder("");
                String name;
                int value;
                String selected;
                for (UnitLevel lvl : unitLvlList) {
                    name = lvl.getName();
                    value = lvl.getValue();
                    if (defUnitLvl == value) {
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
        %>
        <div>
            <form id="formAddDep" name="formAddDep" class="form-horizontal formDig" action="<%=contextP%>/System/DepManager?save" method="post">
                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">單位代碼</label>
                    <div class="col-md-5">
                        <label>${depHeader.getDepID()}</label>
                        <input type="hidden" name="depID" value="${depHeader.getDepID()}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="lastname" class="col-md-3 control-label">單位名稱</label>
                    <div class="col-md-5">
                        <input type="text" class="form-control required"  name="depName" value="${depHeader.getDepName()}" placeholder="請輸入名稱" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">單位層級</label>
                    <div class="col-md-5">
                        <select class="form-control" name="unitLevel">
                            <%=GetUnitLevel(unitLvl, defUnitLvl)%>
                        </select>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
