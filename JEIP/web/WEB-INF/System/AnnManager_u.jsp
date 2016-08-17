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
        <%-- 附件瀏覽 --%>
            doAtaFileBrowse();
            var begtime = $("#hidBegTime").val();
            var endtime = $("#hidEndTime").val();
            if ($(this).val() == "F") {
                $("#divFile").show("slow");
                $("#divAta").show("slow");
                $("#divLink").hide("slow");
            } else if ($(this).val() == "L") {
                $("#divFile").show("slow");
                $("#divAta").hide("slow");
                $("#divLink").show("slow");
            } else {
                $("#divFile").hide("slow");
            }
        <%-- 套用驗證 --%>
            $("#formAddAnn").validate();
        <%-- 套用DatePicker --%>
            $('#txtBegTime').myDatePicker();
            $('#txtBegTime').val(begtime.split(" ")[0]);
            $('#txtEndTime').myDatePicker({add: 7});
            $('#txtEndTime').val(endtime.split(" ")[0]);
        <%-- 套用TimePicker --%>
            $('#txtSTime').myTimePicker();
            $('#txtSTime').val(begtime.split(" ")[1]);
            $('#txtETime').myTimePicker();
            $('#txtETime').val(endtime.split(" ")[1]);
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

        });//end_ready
        <%-- Show附件內容 --%>
        function browserAtaFile(jsonText) {
            $("#divAtaFileTable").show();//不能使用show(slow)，因為隱藏時寬度是由0至正常大小，會造成dataGrid元件誤判是mobile設備
            if (jsonText.AtaType == "L") {
                $('#ataFileTable').myDataGrid($.parseJSON(jsonText.JsonData), {
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
            } else {
                $('#ataFileTable').myDataGrid($.parseJSON(jsonText.JsonData), {
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
                        }],
                    "linkHandler": [{
                            "linkColumn": "ataFileName",
                            handler: function () {
                                $("#hidAtaFileName").val($(this).text());
                                $("#fromNewAnn").submit();
                            }
                        }]
                });
            }
        }
        <%-- 取得附件明細 --%>
        function doAtaFileBrowse() {
            $.ajax({
                url: "<%=contextP%>/System/AnnManager/getAtaDetails",
                type: "get",
                dataType: "json",
                data: {
                    atatype: $("#hidAtaType").val(),
                    annid: $("#hidAnnID").val()
                }
            }).done(function (jsonText) {
                if (jsonText.fail == null) {
                    browserAtaFile(jsonText);
                } else {
                    $.notify(jsonText.fail, {className: 'error'});
                }
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '取得附件明細過程中發生錯誤！',
                    message: errorStatus.responseText
                });
            });
        }
    </script>
    <body>
        <input type="hidden" id="hidBegTime" value="${annHeader.getBegTime()}"/>
        <input type="hidden" id="hidEndTime" value="${annHeader.getEndTime()}"/>
        <div>
            <form id="formAddAnn" name="formAddAnn" class="form-horizontal formDig" action="<%=contextP%>/System/AnnManager?update" method="post">
                <input type="hidden" id="hidAnnID" name="annID" value="${annHeader.getAnnID()}"/>
                <input type="hidden" id="hidAtaType" name="ataType" value="${annHeader.getAtaType()}"/>
                <div class="form-group">
                    <label class="col-xs-12 col-md-3 control-label">公告類型</label>
                    <div class="col-md-3">
                        <select class="form-control" id="sltAtaType" name="ataType">
                            <option value="" ${annHeader.getAtaType().isEmpty()?"selected":""}>文字</option>
                            <option value="F" ${annHeader.getAtaType().equals("F")?"selected":""}>附件</option>
                            <option value="L" ${annHeader.getAtaType().equals("L")?"selected":""}>連結</option>
                        </select>
                    </div>
                </div>
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
            <form id="formFile" name="formFile" class="form-horizontal formDig" 
                  action="<%=contextP%>/System/AnnManager/fileUploadAPTemp" method="post" enctype="multipart/form-data">
                <div class="form-group" id="divFile" style="display: none;">
                    <label class="col-xs-12 col-md-3 control-label">附件</label>
                    <div id="divAta" class="col-xs-12 col-md-2">
                        <label class="btn btn-primary btn-sm col-xs-12">
                            上傳 <input type="file" id="btnFileUpload" name="btnFileUpload"  multiple="multiple" style="display: none;"/>
                        </label>
                    </div>
                    <div id="divLink" class="col-xs-12 col-md-9" style="display: none;">
                        <input type="text" class="form-control" id="txtLink" name="txtLink" placeholder="請輸入網址URL" required/>
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
            <form id="fromNewAnn" name="fromNewAnn" class="form-horizontal" action="<%=contextP%>/file/doDownload" method="post">
                <input type="hidden" id="hidAnnID" name="annID" value="${annHeader.getAnnID()}"/>
                <input type="hidden" id="hidAtaFileName" name="fileName" />
                <input type="hidden" id="hidAtaType" name="ataType" value="系統公告"/>
            </form>
        </div>
    </body>
</html>
