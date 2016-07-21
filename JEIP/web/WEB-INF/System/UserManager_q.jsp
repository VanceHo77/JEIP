<%-- 
    Document   : UserManager
    Created on : 2016/4/18, 下午 02:59:52
    Author     : Vance
--%>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>主控台-使用者設定</title>
    </head>
    <jsp:include page="../Layout.jsp"/>
    <%final String contextP = request.getContextPath();%>
    <style>

        .paging_full_numbers a.paginate_button {
            color: #fff !important;
        }
        .paging_full_numbers a.paginate_active {
            color: #fff !important;
        }
    </style>
    <script>
        $(document).ready(function () {
            getBrowse();
            $("#btnAddUser").click(addUser);
        });
        <%-- 瀏覽資料 --%>
        function getBrowse() {
            $.ajax({
                url: "<%=contextP%>/System/UserManager/browse",
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
                var account;
                $('#form').myDataGrid(jsonText, {
                    "aoColumnDefs": [//欄位的預設值
                        {'bSortable': false, 'aTargets': [0, 6]}//設定不具有排序功能的欄位
                    ],
                    "linkHandler": [{
                            "linkColumn": "Account",
                            handler: function () {
                                account = $(this).parent().parent().find("td").eq(1).text();
                                doModify(account);
                            }
                        }],
                    "buttonHandler": [{
                            "label": "刪除",
                            "value": "Account",
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
                title: '新&nbsp;增&nbsp;使&nbsp;用&nbsp;者',
                message: $('<div></div>').load('<%=contextP%>/System/UserManager/new'),
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
        function doModify(account) {
            BootstrapDialog.show({
                title: '編&nbsp;輯&nbsp;資&nbsp;料',
                message: $('<div></div>').load("<%=contextP%>/System/UserManager/modify?account=" + account),
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
            var frmValidate = $('#formAddUser').validate();
            if (!frmValidate.form()) {
                return false;
            }
            dig.close();
            $.ajax({
                url: "<%=contextP%>/System/UserManager?" + doWhat,
                type: "post",
                dataType: "json",
                data: $("#formAddUser").serialize(),
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
        function doDel(account) {
            BootstrapDialog.show({
                title: '確&nbsp;定&nbsp;刪&nbsp;除',
                message: "確定刪除此筆資料?",
                buttons: [{
                        label: '確定',
                        cssClass: 'btn btn-primary',
                        action: function (dig) {
                            dig.close();
                            $.ajax({
                                url: "<%=contextP%>/System/UserManager?del",
                                type: "post",
                                dataType: "json",
                                data: {
                                    "account": account
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
            <div class="col-md-12 nbar"><button id="btnAddUser" class="btn btn-primary btn-sm">新增使用者</button></div>

            <div class="dataTable_wrapper">
                <table id="form" class="table table-striped table-bordered dt-responsive nowrap hover"  width="100%">
                    <thead>
                        <tr>
                            <th style="width: 5%;">序</th>
                            <th>帳號</th>
                            <th>使用者名稱</th>
                            <th>Email</th>
                            <th>單位</th>
                            <th>使用者權限</th>
                            <th>刪除</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </body>
</html>
