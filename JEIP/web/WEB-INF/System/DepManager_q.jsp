<%-- 
    Document   : DepManager
    Created on : 2016/4/18, 下午 02:59:52
    Author     : Vance
--%>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>主控台-單位設定</title>
    </head>
    <jsp:include page="../Layout.jsp"/>
    <%final String contextP = request.getContextPath();%>
    <script>
        $(document).ready(function () {
            getBrowse();
            $("#btnAddDep").click(addUser);
        });
        <%-- 瀏覽資料 --%>
        function getBrowse() {
            $.ajax({
                url: "<%=contextP%>/System/DepManager/browse",
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
                var depid;
                $('#form').myDataGrid(jsonText, {
                    "aoColumnDefs": [//欄位的預設值
                        {'bSortable': false, 'aTargets': [0]}//設定不具有排序功能的欄位
                    ],
                    "linkHandler": [{
                            "linkColumn": "DepName",
                            handler: function () {
                                depid = $(this).parent().parent().find("td").eq(1).text();
                                doModify(depid);
                            }
                        }],
                    "buttonHandler": [{
                            "label": "刪除",
                            "value": "DepID",
                            handler: function () {
                                doDel($(this).val());
                            }
                        }]
                });
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '讀取失敗，系統發生錯誤！',
                   message: $("<div/>").html(errorStatus.responseText)
                });
            });
        }
        <%-- 新增使用者畫面 --%>
        function addUser() {
            BootstrapDialog.show({
                title: '新&nbsp;增&nbsp;單&nbsp;位',
                message: $('<div></div>').load('<%=contextP%>/System/DepManager/new'),
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
        function doModify(depid) {
            BootstrapDialog.show({
                title: '編&nbsp;輯&nbsp;資&nbsp;料',
                message: $('<div></div>').load("<%=contextP%>/System/DepManager/modify?depid=" + depid),
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
            var frmValidate = $('#formAddDep').validate();
            if (!frmValidate.form()) {
                return false;
            }
            dig.close();
            $.ajax({
                url: "<%=contextP%>/System/DepManager?" + doWhat,
                type: "post",
                dataType: "json",
                data: $("#formAddDep").serialize(),
                beforeSend: function () {
                    $(window).block({message: "存檔中，請稍後..."});
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
                    title: '存檔失敗，系統發生錯誤！',
                    message: $("<div/>").html(errorStatus.responseText)
                });
            });
        }
        <%--刪除--%>
        function doDel(depid) {
            BootstrapDialog.show({
                title: '確&nbsp;定&nbsp;刪&nbsp;除',
                message: "確定刪除此筆資料?",
                buttons: [{
                        label: '確定',
                        cssClass: 'btn btn-primary',
                        action: function (dig) {
                            dig.close();
                            $.ajax({
                                url: "<%=contextP%>/System/DepManager?del",
                                type: "post",
                                dataType: "json",
                                data: {
                                    "depid": depid
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
    </script>
    <body>
        <div id="page-wrapper">
            <div class="col-md-12 nbar"><button id="btnAddDep" class="btn btn-primary btn-sm">新增單位</button></div>

            <div class="dataTable_wrapper">
                <table id="form" class="table table-striped table-bordered dt-responsive nowrap hover" cellspacing="0" width="100%">
                    <thead>
                        <tr>
                            <th style="width: 5%;">序</th>
                            <th>單位代碼</th>
                            <th>單位名稱</th>
                            <th>單位層級</th>
                            <th>刪除</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </body>
</html>
