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
        <title>主控台-使用者設定</title>
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
            UnitLevel[] unitLvlList = DroupDownList.getUnitLevelList();
        %>
        <%!
            //下拉選單-單位層級
            StringBuilder GetUnitLevel(UnitLevel[] unitLvlList) {
                StringBuilder htmlCode = new StringBuilder("");
                String name;
                int value;
                for (UnitLevel lvl : unitLvlList) {
                    name = lvl.getName();
                    value = lvl.getValue();
                    htmlCode.append("<option value='" + value + "'").append(">")
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
                        <input type="text" class="form-control required" name="depID" maxlength="2"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="lastname" class="col-md-3 control-label">單位名稱</label>
                    <div class="col-md-5">
                        <input type="text" class="form-control required" name="depName"  placeholder="請輸入單位名稱" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">使用者身分</label>
                    <div class="col-md-5">
                        <select class="form-control" name="unitLevel">
                            <%=GetUnitLevel(unitLvlList)%>
                        </select>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
