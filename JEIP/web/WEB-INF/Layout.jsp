<%@page import="com.enumset.UserLevel"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html style="background-color: #D6D6D6;">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--content="0"，是表示過期的時間，如果不是0的話，就必須使用GMT的時間格式 --%>
        <meta HTTP-equiv="Expires" content="0">
        <%--不用緩存機制 --%>
        <meta HTTP-equiv="kiben" content="no-cache">
        <title>主控台</title>
        <%final String contextP = request.getContextPath();%>
        <%-- jQuery --%>
        <script src="<%=contextP%>/resources/js/jquery-1.12.0.min.js"></script>
        <%-- Bootstrap Core JavaScript --%>
        <script src="<%=contextP%>/resources/js/bootstrap.min.js"></script>
        <%-- jQueryUI --%>
        <script src="<%=contextP%>/resources/js/jquery-ui.min.js"></script>
        <%-- 遮罩 --%>
        <script src="<%=contextP%>/resources/js/blockUI.js"></script>
        <%-- 對話框 --%>
        <script src="<%=contextP%>/resources/js/bootstrap-dialog.min.js"></script>
        <%-- 表單驗證 --%>
        <script src="<%=contextP%>/resources/js/jquery.validate.js"></script>
        <%-- Metis Menu Plugin JavaScript --%>
        <script src="<%=contextP%>/resources/template/bower_components/metisMenu/dist/metisMenu.min.js"></script>
        <%-- DataTables JavaScript --%>
        <script src="<%=contextP%>/resources/template/bower_components/datatables/media/js/jquery.dataTables.min.js"></script>
        <script src="<%=contextP%>/resources/template/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.min.js"></script>
        <script src="<%=contextP%>/resources/template/bower_components/responsive/js/dataTables.responsive.min.js"></script>
        <!--<script src="<%=contextP%>/resources/template/bower_components/responsive/js/responsive.bootstrap.min.js"></script>-->
        <%-- Custom Theme JavaScript --%>
        <script src="<%=contextP%>/resources/template/dist/js/sb-admin-2.js"></script>
        <%-- Notification JavaScript --%>
        <script src="<%=contextP%>/resources/js/notify.min.js"></script>
        <%-- MyPlugin JavaScript --%>
        <script src="<%=contextP%>/resources/js/myPlugin.js"></script>
        <%-- DatePicker JavaScript --%>
        <script src="<%=contextP%>/resources/datepicker/js/bootstrap-datepicker.js"></script>
        <script src="<%=contextP%>/resources/datepicker/js/bootstrap-datepicker.zh-TW.js"></script>
        <%-- TimePicker JavaScript --%>
        <script src='<%=contextP%>/resources/timepicker/js/bootstrap-clockpicker.min.js'></script>
        <!--<script src='<%=contextP%>/resources/timepicker/js/jquery-clockpicker.min.js'></script>-->

        <%-- Bootstrap Core CSS --%>
        <link href="<%=contextP%>/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <%-- Bootstrap 對話框 CSS --%>
        <link href="<%=contextP%>/resources/css/bootstrap-dialog.min.css" rel="stylesheet" type="text/css">
        <%-- MetisMenu CSS --%>
        <link href="<%=contextP%>/resources/template/bower_components/metisMenu/dist/metisMenu.min.css" rel="stylesheet" type="text/css">
        <%-- Timeline CSS--%>
        <link href="<%=contextP%>/resources/template/dist/css/timeline.css" rel="stylesheet" type="text/css">
        <%-- Custom CSS --%>
        <link href="<%=contextP%>/resources/template/dist/css/sb-admin-2.css" rel="stylesheet" type="text/css">
        <%-- Custom Fonts --%>
        <link href="<%=contextP%>/resources/template/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <%-- DataTables CSS --%>
        <link href="<%=contextP%>/resources/css/jquery.dataTables.min.css" rel="stylesheet" type="text/css">
        <link href="<%=contextP%>/resources/template/bower_components/datatables-plugins/integration/bootstrap/3/dataTables.bootstrap.css" rel="stylesheet" type="text/css">
        <link href="<%=contextP%>/resources/template/bower_components/responsive/css/responsive.bootstrap.min.css" rel="stylesheet" type="text/css">
        <%-- DatePicker CSS --%>
        <link href="<%=contextP%>/resources/datepicker/css/datepicker.css" rel="stylesheet" type="text/css">
        <%-- TimePicker CSS --%>
        <link href="<%=contextP%>/resources/timepicker/css/bootstrap-clockpicker.min.css" rel="stylesheet" type='text/css'/>
        <!--<link href="<%=contextP%>/resources/timepicker/css/jquery-clockpicker.min.css" rel="stylesheet" type='text/css'/>-->
    </head>
    <style>
        <%-- Dialog按鈕置中 --%>
        .bootstrap-dialog-footer-buttons{
            text-align:center;
        }
        <%-- Dialog字體 --%>
        .modal-content{
            font-family: 微軟正黑體;
        }
        <%-- 網頁內容CSS --%>
        body {
            margin:0;
            padding:0;
            -moz-background-size: cover;
            background-size: cover;
            font-size:16px;
            font-family: 微軟正黑體;
        }
        <%-- 作業內容最上方的功能列 --%>
        .nbar{
            text-align:right;
            padding-top:10px;
            padding-bottom:13px;
            padding-right:0px;
        }
        <%-- DataTable欄位名稱字體大小 --%>
        thead{
            font-size: 16px;
        }
        <%-- 修改DataTable分頁按鈕多餘的空白間距 --%>
        .dataTables_wrapper .dataTables_paginate .paginate_button {
            padding : 0px;
            margin-left: 0px;
            display: inline;
            border: 0px;
        }
        .dataTables_wrapper .dataTables_paginate .paginate_button:hover {
            border: 0px;
        }
        <%-- 欄位名稱(control-label)的背景顏色 --%>
        .control-label{
            background-color:#BFD6DE;
        }
        <%-- 文字置右 --%>
        .col-xs-3{
            text-align: right;
        }
        <%-- 對話框Dialog的內容訊息 --%>
        .formDig{
            padding-left: 10px;
            padding-right: 10px;
        }
        <%-- 檔案上傳的按鈕樣式 --%>
        .btn-file {
            position: relative;
            overflow: hidden;
        }
        .btn-file input[type=file] {
            position: absolute;
            top: 0;
            right: 0;
            min-width: 100%;
            min-height: 100%;
            font-size: 100px;
            text-align: right;
            filter: alpha(opacity=0);
            opacity: 0;
            outline: none;
            background: white;
            cursor: inherit;
            display: block;
        }
    </style>
    <script>
        $(document).ready(function () {
        <%-- Notification訊息框預設值，參考網址https://notifyjs.com --%>
            $.notify.defaults({className: "success", autoHideDelay: 5000, position: "top center"});

        <%-- 顯示個人資料存檔結果 --%>
        <%
            final HashMap userProfileSaveRS = (HashMap) request.getSession().getAttribute("UserProfileSaveRS");
            if (userProfileSaveRS != null) {
                String msg = "";
                if (userProfileSaveRS.get("fail") == null) {
                    msg = userProfileSaveRS.get("success").toString();
                    out.println("$.notify('" + msg + "');");
                } else {
                    msg = userProfileSaveRS.get("fail").toString();
                    out.println("$.notify('" + msg + "',{className:'error'});");
                }
                //清掉
                request.getSession().setAttribute("UserProfileSaveRS", null);
            }
        %>
        <%-- 個人資料 --%>
            $("#hrefUserProfile").click(function () {
                BootstrapDialog.show({
                    title: '個&nbsp;人&nbsp;資&nbsp;料',
                    message: $('<div></div>').load('<%=contextP%>/Dialog/UserProfile'),
                    buttons: [{
                            label: '存檔',
                            cssClass: 'btn btn-primary',
                            icon: 'glyphicon glyphicon-check',
                            action: function (dig) {
                                var frmValidate = $('#fromUserProfile').validate();
                                if (!frmValidate.form()) {
                                    return false;
                                }
                                $("#fromUserProfile").submit();
                            }
                        }]
                });
            });
        <%-- 登出 --%>
            $("#hrefLogOut").click(function () {
                BootstrapDialog.show({
                    title: '系統登出',
                    message: '確定登出系統?',
                    buttons: [{
                            label: '確定',
                            cssClass: 'btn btn-primary',
                            action: function (dig) {
                                location.href = "<%=contextP%>/Default/Logout";
                            }
                        }]
                });
            });
        });
    </script>
    <body class=" col-xs-offset-0 col-xs-12  col-md-offset-1 col-md-10" style="border-left-style:groove;border-right-style:groove;border-color:#def;padding-bottom:15px;">
        <%-- jsp程式碼集中區 --%>
        <%
            String menu = (String) request.getSession().getAttribute("Menu");
        %>
        <%!
            StringBuilder MenuCtrl(String menu, String contextP) {
                StringBuilder htmlCode = new StringBuilder("");
                if (menu.equals(UserLevel.系統管理員.getValue())) {
                    htmlCode.append("<a href='#'><i class='fa fa-wrench fa-fw'></i> 系統管理<span class='fa arrow'></span></a>")
                            .append("<ul class='nav nav-second-level collapse in' aria-expanded='true'>")
                            .append("<li> <a href='" + contextP + "/System/UserManager'>使用者設定</a></li>")
                            .append("<li> <a href='" + contextP + "/System/DepManager'>單位設定</a></li>")
                            .append("<li> <a href='" + contextP + "/System/AnnManager'>系統公告設定</a></li>")
                            .append("</ul>");
                }
                return htmlCode;
            }
        %>
        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="<%=contextP%>\Default" style="color:#337ab7;font-size:24pt;"><b>JEIP入口網站</b></a>
            </div>
            <!-- /.navbar-header -->

            <ul class="nav navbar-top-links navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="fa fa-bell fa-fw"></i>  <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-alerts">
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-flash fa-fw"></i> 最新公告
                                    <span class="pull-right text-muted small">有四則最新公告</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small">12 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-envelope fa-fw"></i> Message Sent
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-tasks fa-fw"></i> New Task
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#">
                                <div>
                                    <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small">4 minutes ago</span>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a class="text-center" href="#">
                                <strong>See All Alerts</strong>
                                <i class="fa fa-angle-right"></i>
                            </a>
                        </li>
                    </ul>
                </li>
                <%-- 個人資料 --%>
                <li class="dropdown">
                    <a id="hrefUserProfile" href="#" title="個人資料"><i class="fa fa-user fa-fw"></i></a>
                </li>
                <%-- 登出 --%>
                <li class="dropdown">
                    <a id="hrefLogOut" href="#" title="登出"><i class="glyphicon glyphicon-log-out"></i></a>
                </li>
            </ul>
            <!-- /.navbar-top-links -->

            <div class="navbar-default sidebar" role="navigation">
                <div class="sidebar-nav navbar-collapse">
                    <ul class="nav" id="side-menu">
                        <li class="sidebar-search">
                            <div class="input-group custom-search-form">
                                <input type="text" class="form-control" placeholder="Search...">
                                <span class="input-group-btn">
                                    <button class="btn btn-default" type="button" style="height:34px">
                                        <i class="fa fa-search"></i>
                                    </button>
                                </span>
                            </div>
                            <!-- /input-group -->
                        </li>
                        <li>
                            <a href="tables.html"><i class="fa fa-table fa-fw"></i> Tables</a>
                        </li>
                        <li>
                            <%=MenuCtrl(menu, contextP)%>
                        </li>
                    </ul>
                </div>
                <!-- /.sidebar-collapse -->
            </div>
            <!-- /.navbar-static-side -->
        </nav>
        <!-- /#wrapper -->
    </body>
</html>
