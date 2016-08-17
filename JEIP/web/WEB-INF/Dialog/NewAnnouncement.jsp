<%-- 
    Document   : UserProfile
    Created on : 2016/4/1, 下午 10:14:16
    Author     : Vance
--%>

<%@page import="com.enumset.UserLevel"%>
<%@page import="com.model.userheader.UserHeader"%>
<%@page import="com.model.depheader.DepHeader"%>
<%@page import="com.model.announcementheader.AnnouncementHeader"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NewAnnMsg</title>
    </head>
    <%final String contextPath = request.getContextPath();%>
    <script>
        $(document).ready(function () {
            doAtaFileBrowse();
        });
        <%-- 取得附件明細 --%>
        function doAtaFileBrowse() {
            $.ajax({
                url: "<%=contextPath%>/System/AnnManager/getAtaDetails",
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
        <%-- Show附件內容 --%>
        function browserAtaFile(jsonText) {
            $("#divAtaFileTable").show(); //不能使用show(slow)，因為隱藏時寬度是由0至正常大小，會造成dataGrid元件誤判是mobile設備
            if (jsonText.AtaType == "L") {
                $('#ataFileTable').myDataGrid($.parseJSON(jsonText.JsonData), {
                    "aoColumnDefs": [//欄位的預設值
                        {'bSortable': false, 'aTargets': [0]}, //設定不具有排序功能的欄位
                        {'bSortable': false, 'aTargets': [2]}
                    ]
                });
            } else {
                $('#ataFileTable').myDataGrid($.parseJSON(jsonText.JsonData), {
                    "aoColumnDefs": [//欄位的預設值
                        {'bSortable': false, 'aTargets': [0]}, //設定不具有排序功能的欄位
                        {'bSortable': false, 'aTargets': [2]}
                    ],
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
    </script>
    <body>
        <div class="">
            <form id="fromNewAnn" name="fromNewAnn" class="form-horizontal" action="<%=contextPath%>/file/doDownload" method="post" enctype="multipart/form-data">
                <input type="hidden" id="hidAnnID" name="annID" value="${annHeader.getAnnID()}"/>
                <input type="hidden" id="hidAtaFileName" name="fileName" />
                <input type="hidden" id="hidAtaType" name="ataType" value="${annHeader.getAtaType()}"/>
                <div class="form-group formDig" >
                    <div class="col-md-12">
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">開始日期</label>
                            <div class="col-xs-9 col-md-9 from-control ">
                                <span>${annHeader.getBegTime()}</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">結束日期</label>
                            <div class="col-xs-9 col-md-9 from-control ">
                                <span>${annHeader.getEndTime()}</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">公告內容</label>
                            <div class="col-xs-9 col-md-9 from-control ">
                                <span>${annHeader.getAnnouncementDesc()}</span>
                            </div>
                        </div>
                        <div class="form-group" id="divAtaFileTable" style="display: none;">
                            <div class="dataTable_wrapper">
                                <table id="ataFileTable" class="table table-striped table-bordered dt-responsive nowrap hover" cellspacing="0" width="100%">
                                    <thead>
                                        <tr>
                                            <th style="width: 5%">序</th>
                                            <th style="width: 75%">附件名稱</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
