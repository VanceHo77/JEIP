<%-- 
    Document   : AnnManager
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
        <title>主控台-系統公告設定</title>
    </head>
    <%final String contextP = request.getContextPath();%>

    <script>
        $(document).ready(function () {
        <%-- 存json供附件新增用 --%>
            jsonAry = [];
        <%-- 套用驗證 --%>
            $("#formAddAnn").validate();
        <%-- 套用DatePicker --%>
            $('#txtBegTime').myDatePicker();
            $('#txtEndTime').myDatePicker({add: 7});
        <%-- 套用TimePicker --%>
            $('#txtSTime').myTimePicker();
            $('#txtETime').myTimePicker();

        <%-- 附件類型事件 --%>
            $("#sltAtaType").change(function () {
                if ($(this).val() == "F") {
                    $("#divFile").show("slow");
                    $("#divLink").hide("slow");
                } else if ($(this).val() == "L") {
                    $("#divFile").hide("slow");
                    $("#divLink").show("slow");
                } else {
                    $("#divFile").hide("slow");
                    $("#divLink").hide("slow");
                }
            });
        <%-- 附件上傳至AP暫存檔 --%>
            $("#btnFileUpload").change(function (event) {
                var $this_prop = $(this).prop("files");
                var fileName;
                var file_data;
        <%-- 建立一個用於放資料的物件 --%>
                var form_data = new FormData();
        <%-- 先判斷檔名是否合法 --%>
                for (var i = 0; i < event.target.files.length; i++) {
                    fileName = event.target.files[i].name;
        <%-- 檔名判斷 --%>
                    if (!boolFileSpecial(fileName))
                    {
                        $.notify(fileName + "」檔名含有特殊字元或空白，該修改檔名。", {className: 'error'});
                        return false;
                    }
                }
        <%-- 再存入資料 --%>
                for (var i = 0; i < event.target.files.length; i++) {
                    fileName = event.target.files[i].name;
                    file_data = $this_prop[i];
                    form_data.append("file", file_data);
                    form_data.append("fileName", fileName);
                    fileNameAry[fileNameAry.length] = fileName;
                }
                $.ajax({
                    url: "<%=contextP%>/System/AnnManager/fileUploadAPTemp",
                    type: "post",
                    dataType: "json",
                    data: form_data,
                    cache: false,
                    contentType: false,
                    processData: false //不加這行無法將檔案傳入後端
                }).done(function (jsonText) {
                    if (jsonText.fail == null) {
                        $.notify("附件上傳成功。");
                        if ($("#ataFileTable tbody tr").length == 0) {
                            browserAtaFile(jsonText);
                            for (var i = 0; i < jsonText.length; i++) {
                                jsonAry[i] = jsonText[i];
                            }
                        } else {
                            var j = 0;
                            for (var i = 0; i < jsonText.length; i++) {
                                jsonAry[jsonAry.length] = jsonText[j];
                                j++;
                            }
                            browserAtaFile(jsonAry);
                        }
                    } else {
                        $.notify(jsonText.fail, {className: 'error'});
                    }
                }).fail(function (errorStatus) {
                    BootstrapDialog.show({
                        title: '上傳檔案至AP暫存過程中發生錯誤！',
                        message: errorStatus.responseText
                    });
                });
            });
        });
        <%-- 附件瀏覽 --%>
        function browserAtaFile(jsonText) {
            $("#divAtaFileTable").show();//不能使用show(slow)，因為隱藏時寬度是由0至正常大小，會造成dataGrid元件誤判是mobile設備
            $('#ataFileTable').myDataGrid(jsonText, {
                "aoColumnDefs": [//欄位的預設值
                    {'bSortable': false, 'aTargets': [0]}, //設定不具有排序功能的欄位
                    {'bSortable': false, 'aTargets': [2]}
                ],
                "buttonHandler": [{
                        "label": "刪除",
                        "value": "filename",
                        handler: function () {
                            doDel($(this).val());
                        }
                    }]
            });
        }
        <%--刪除--%>
        function doDel(fileName) {
            BootstrapDialog.show({
                title: '確&nbsp;定&nbsp;刪&nbsp;除',
                message: "確定刪除此筆附件?",
                buttons: [{
                        label: '確定',
                        cssClass: 'btn btn-primary',
                        action: function (dig) {
                            dig.close();
                            $.ajax({
                                url: "<%=contextP%>/System/AnnManager/delAtaFile",
                                type: "post",
                                dataType: "json",
                                data: {
                                    "fileName": fileName
                                },
                                beforeSend: function () {
                                    $(window).block({message: "附件刪除中，請稍後..."});
                                },
                                complete: function () {
                                    $(window).unblock();
                                }
                            }).done(function (jsonText) {
                                console.log(jsonText.fail);
                                if (jsonText.fail == null) {
                                    $.notify(jsonText.success);
                                    var tmpAry = [];
                                    var i = 0;
                                    $.each(jsonAry, function (k, v) {
                                        if (v.filename != fileName) {
                                            tmpAry[tmpAry.length] = jsonAry[i];
                                        }
                                        i++;
                                    });
                                    jsonAry = tmpAry;
                                    browserAtaFile(jsonAry);
                                } else {
                                    $.notify(jsonText.fail, {className: 'error'});
                                }
                            }).fail(function (errorStatus) {
                                BootstrapDialog.show({
                                    title: '附件刪除失敗，系統發生錯誤！',
                                    message: $("<div/>").html(errorStatus.responseText)
                                });
                            });
                        }
                    }]
            });
        }
    </script>
    <body>
        <div>
            <form id="formAddAnn" name="formAddAnn" class="form-horizontal formDig" action="<%=contextP%>/System/AnnManager?save" method="post">
                <div class="form-group">
                    <label class="col-xs-12 col-md-3 control-label">公告類型</label>
                    <div class="col-md-3">
                        <select class="form-control" id="sltAtaType" name="ataType">
                            <option value="">文字</option>
                            <option value="F">附件</option>
                            <option value="L">連結</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-12 col-md-3 control-label">公告時間(起)</label>
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
                    <label class="col-xs-12 col-md-3 control-label">公告時間(訖)</label>
                    <div class="col-md-4">
                        <div class="input-group date">
                            <input type="text" class="form-control datepicker required" id="txtEndTime" name="endTime" placeholder="日期(訖)" maxlength="18" required/>
                            <div class="input-group-addon">
                                <span class="glyphicon glyphicon-th"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-md-3">
                        <div class="input-group clockpicker">
                            <input type="text" class="form-control" id="txtETime" name="endTime" placeholder="時間(訖)" maxlength="5"  required/>
                            <span class="input-group-addon" style="cursor: default;">
                                <span class="glyphicon glyphicon-time"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-12 col-md-3 control-label">公告內容</label>
                    <div class="col-md-9">
                        <textarea class="form-control" name="announcementDesc" rows="4" maxlength="500" placeholder="最多輸入500字"></textarea>
                    </div>
                </div>
                <div class="form-group"  id="divLink" style="display: none;">
                    <label class="col-xs-12 col-md-3 control-label">附件</label>
                    <div class="col-xs-12 col-md-9">
                        <input type="text" class="form-control" id="txtLink" name="fileName" placeholder="請輸入網址URL" required/>
                    </div>
                </div>
            </form>
            <form id="formFile" name="formFile" class="form-horizontal formDig" 
                  action="<%=contextP%>/System/AnnManager/fileUploadAPTemp" method="post" enctype="multipart/form-data">
                <div class="form-group" id="divFile" style="display: none;">
                    <label class="col-xs-12 col-md-3 control-label">附件</label>
                    <div class="col-xs-12 col-md-2">
                        <label class="btn btn-primary btn-sm col-xs-12">
                            上傳 <input type="file" id="btnFileUpload" name="btnFileUpload"  multiple="multiple" style="display: none;"/>
                        </label>
                    </div>
                </div>
                <div class="form-group" id="divAtaFileTable" style="display: none;">
                    <div class="dataTable_wrapper">
                        <table id="ataFileTable" class="table table-striped table-bordered dt-responsive nowrap hover" cellspacing="0" width="100%">
                            <thead>
                                <tr>
                                    <th style="width: 5%">序</th>
                                    <th style="width: 75%">附件名稱</th>
                                    <th style="width: 20%">刪除</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
