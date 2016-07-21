<%-- 
    Document   : AnnManager
    Created on : 2016/4/18, 下午 02:59:52
    Author     : Vance
--%>

<%@page import="com.model.announcementheader.AnnouncementHeader"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>主控台-系統公告設定</title>
    </head>
    <%final String contextP = request.getContextPath();%>

    <script>
        $(document).ready(function () {
            var begtime = $("#hidBegTime").val();
            var endtime = $("#hidEndTime").val();
        <%-- 套用驗證 --%>
            $("#formAddAnn").validate();
        <%-- 套用DatePicker --%>
            $('#txtBegTime').myDatePicker();
            $('#txtBegTime').val(begtime.split(" ")[0]);
            $('#txtEndTime').myDatePicker({add: 7});
            $('#txtEndTime').val(endtime.split(" ")[0]);
        <%-- 套用TimePicker --%>
            $('#txtSTime').myTimePicker();
            $('#txtSTime').val(begtime.split(" ")[1])
            $('#txtETime').myTimePicker();
            $('#txtETime').val(endtime.split(" ")[1]);
        });
    </script>
    <body>
        <input type="hidden" id="hidBegTime" value="${annHeader.getBegTime()}"/>
        <input type="hidden" id="hidEndTime" value="${annHeader.getEndTime()}"/>
        <div>
            <form id="formAddAnn" name="formAddAnn" class="form-horizontal formDig" action="<%=contextP%>/System/AnnManager?update" method="post">
                <input type="hidden" name="annID" value="${annHeader.getAnnID()}"/>
                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">公告時間(起)</label>
                    <div class="col-md-4">
                        <div class="input-group date">
                            <input type="text" class="form-control datepicker required"  id="txtBegTime" name="begTime" placeholder="日期(起)" maxlength="9"  required/>
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-th"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="input-group clockpicker">
                            <input type="text" class="form-control" id="txtSTime" name="begTime" placeholder="時間(起)" maxlength="5"  required/>
                            <span class="input-group-addon" style="cursor: default;">
                                <span class="glyphicon glyphicon-time"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="lastname" class="col-md-3 control-label">公告時間(訖)</label>
                    <div class="col-md-4">
                        <div class="input-group date">
                            <input type="text" class="form-control datepicker required" id="txtEndTime" name="endTime" placeholder="日期(訖)" maxlength="18" required/>
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-th"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="input-group clockpicker">
                            <input type="text" class="form-control" id="txtETime" name="endTime" placeholder="時間(訖)" maxlength="5"  required/>
                            <span class="input-group-addon" style="cursor: default;">
                                <span class="glyphicon glyphicon-time"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-md-3 control-label">公告內容</label>
                    <div class="col-md-9">
                        <textarea class="form-control" name="announcementDesc" rows="4" maxlength="500" placeholder="最多輸入500字">${annHeader.getAnnouncementDesc()}</textarea>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
