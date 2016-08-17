<%-- 
    Document   : AnnManager
    Created on : 2016/4/18, 下午 02:59:52
    Author     : Vance
--%>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>主控台-系統公告設定</title>
    </head>
    <jsp:include page="../Layout.jsp"/>
    <%final String contextP = request.getContextPath();%>
    <script>
        $(document).ready(function () {
            fileNameAry = [];//存檔名
            getBrowse();
            $("#btnAddAnn").click(addAnn);
        });
        <%-- 瀏覽資料 --%>
        function getBrowse() {
            $.ajax({
                url: "<%=contextP%>/System/AnnManager/browse",
                type: "post",
                dataType: "json",
                async: false,
                beforeSend: function () {
                    $(window).block({message: "資料讀取中，請稍後..."});
                },
                complete: function () {
                    $(window).unblock();
                }
            }).done(function (jsonText) {
                var annid;
                $('#form').myDataGrid(jsonText, {
                    "aoColumnDefs": [//欄位的預設值
                        {'bSortable': false, 'aTargets': [0, 5]}, //設定不具有排序功能的欄位
                        {"bVisible": false, "aTargets": [1]}, //設定隱藏欄位
                        {"width": "5%", "targets": [0]},
                        {"width": "60%", "targets": [2]}
                    ],
                    "linkHandler": [{
                            "linkColumn": "AnnouncementDesc",
                            handler: function () {
                                annid = $(this).parents("tr").find("#hidAnnID").val();
                                doModify(annid);
                            }
                        }],
                    "buttonHandler": [{
                            "label": "刪除",
                            "value": "AnnID",
                            handler: function () {
                                doDel($(this).val());
                            }
                        }]
                });
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '讀取失敗，系統發生錯誤！',
                    message: errorStatus.responseText
                });
            });
        }
        <%-- 新增使用者畫面 --%>
        function addAnn() {
            BootstrapDialog.show({
                title: '新&nbsp;增&nbsp;系&nbsp;統&nbsp;公&nbsp;告',
                message: $('<div></div>').load('<%=contextP%>/System/AnnManager/new'),
                buttons: [{
                        label: '存檔',
                        cssClass: 'btn btn-primary',
                        icon: 'glyphicon glyphicon-check',
                        action: function (dig) {
                            doSaveOrUpdate(dig, "save");
                        }
                    }]
            });
        }
        <%-- 維護畫面 --%>
        function doModify(annid) {
            BootstrapDialog.show({
                title: '編&nbsp;輯&nbsp;資&nbsp;料',
                message: $('<div></div>').load("<%=contextP%>/System/AnnManager/modify?annid=" + annid),
                buttons: [{
                        label: '存檔',
                        cssClass: 'btn btn-primary',
                        icon: 'glyphicon glyphicon-check',
                        action: function (dig) {
                            doSaveOrUpdate(dig, "update");
                        }
                    }]
            });
        }
        <%-- 存檔或更新 --%>
        function doSaveOrUpdate(dig, doWhat) {
            var frmValidate = $('#formAddAnn').validate();
            if (!frmValidate.form()) {
                return false;
            }
            dig.close();
        <%-- 有上傳附件才檢查AP暫存檔 --%>
            if ($("#sltAtaType").val() == "F" && $("#ataFileTable tbody tr").length != 0) {
                try {
                    chkAtaFile();
                } catch (errorStatus) {
                    BootstrapDialog.show({
                        title: '存檔失敗，系統發生錯誤！',
                        message: $("<div/>").html(errorStatus.responseText)
                    });
                    return false;
                }
            }
            $.ajax({
                url: "<%=contextP%>/System/AnnManager?" + doWhat,
                type: "post",
                dataType: "json",
                data: $("#formAddAnn").serialize() + "&fileNames=" + fileNameAry,
                beforeSend: function () {
                    $(window).block({message: "存檔中，請稍後..."});
                },
                complete: function () {
                    $(window).unblock();
                }
            }).done(function (jsonText) {
                if (jsonText.fail == null) {
                    $.notify(jsonText.success);
                    setTimeout(function () {
                        location.href = "<%=contextP%>/System/AnnManager";
                    }, 2000);
                } else {
                    $.notify(jsonText.fail, {className: 'error'});
                }
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '存檔失敗，系統發生錯誤！',
                    message: $("<div/>").html(errorStatus.responseText)
                });
            });

        }
        <%--刪除--%>
        function doDel(annid) {
            BootstrapDialog.show({
                title: '確&nbsp;定&nbsp;刪&nbsp;除',
                message: "確定刪除此筆資料?",
                buttons: [{
                        label: '確定',
                        cssClass: 'btn btn-primary',
                        action: function (dig) {
                            dig.close();
                            $.ajax({
                                url: "<%=contextP%>/System/AnnManager?del",
                                type: "post",
                                dataType: "json",
                                data: {
                                    "annid": annid
                                },
                                beforeSend: function () {
                                    $(window).block({message: "刪除中，請稍後..."});
                                },
                                complete: function () {
                                    $(window).unblock();
                                }
                            }).done(function (jsonText) {
                                if (jsonText.fail == null) {
                                    $.notify(jsonText.success);
                                    getBrowse();
                                } else {
                                    $.notify(jsonText.fail, {className: 'error'});
                                }
                            }).fail(function (errorStatus) {
                                BootstrapDialog.show({
                                    title: '刪除失敗，系統發生錯誤！',
                                    message: $("<div/>").html(errorStatus.responseText)
                                });
                            });
                        }
                    }]
            });
        }
        <%-- 檢查附件是否存在 --%>
        function chkAtaFile() {
            var rtnMsg = false;
            $.ajax({
                url: "<%=contextP%>/file/isAPTempFileExists",
                type: "get",
                dataType: "json",
                data: {
                    "ataType": "系統公告"
                },
                async: false
            }).done(function (jsonText) {
                if (jsonText.fail == null) {
                    rtnMsg = true;
                }
            }).fail(function (errorStatus) {
                throw errorStatus;
            });
            return rtnMsg;
        }
    </script>
    <body>
        <div id="page-wrapper">
            <div class="col-md-12 nbar"><button id="btnAddAnn" class="btn btn-primary btn-sm">新增系統公告</button></div>

            <div class="dataTable_wrapper">
                <table id="form" class="table table-striped table-bordered dt-responsive nowrap hover" cellspacing="0" width="100%">
                    <thead>
                        <tr>
                            <th>序</th>
                            <th>公告代碼</th>
                            <th>公告內容</th>
                            <th>開始日期</th>
                            <th>結束日期</th>
                            <th>刪除</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </body>
</html>
