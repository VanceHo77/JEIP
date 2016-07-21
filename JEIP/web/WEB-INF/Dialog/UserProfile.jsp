<%-- 
    Document   : UserProfile
    Created on : 2016/4/1, 下午 10:14:16
    Author     : Vance
--%>

<%@page import="com.enumset.UserLevel"%>
<%@page import="com.model.userheader.UserHeader"%>
<%@page import="com.model.depheader.DepHeader"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UserProfile</title>
    </head>
    <%final String contextPath = request.getContextPath();%>
    <style>
        #imgUserPhoto{
            background-repeat:no-repeat;
            background-size:200px 190px;
            cursor: pointer;
            height:200px;
            width:190px;
        }
    </style>
    <script>
        $(document).ready(function () {
            var isShowPWD = "close";
            $("#fileUpload").change(function () {
                ReadURL(this);
            });
            $("#hrefChangePWD").click(function () {
                isShowPWD = chkPWD(isShowPWD);
            });
            $("#txtOldPassword").change(function () {
                chkOldPwd($.trim($(this).val()));
            });
        <%-- 檢查是否有輸入相同的密碼 --%>
            $("#txtPasswdAgain,#txtPasswd").change(function () {
                var pwd01 = $("#txtPasswd").val();
                var pwd02 = $("#txtPasswdAgain").val();
                $("#divChkPwdErr").empty();
                if (pwd01 != pwd02) {
                    var fail = $("<p/>").attr("style", "color:red;").text("密碼不符。");
                    $("#divChkPwdErr").append(fail);
                } else {
                    var msg = $("<span/>").attr({
                        "style": "color:green;",
                        "class": "glyphicon glyphicon-ok"
                    });
                    $("#divChkPwdErr").append(msg);
                }
            });
        <%-- 檢查名稱是否已被使用 --%>
            $("#txtName").change(function () {
                chkUserName($.trim($(this).val()));
            });
        <%-- 檢查信箱是否已被使用 --%>
            $("#txtEmail").change(function () {
                chkEmail($.trim($(this).val()));
            });
        });
        <%-- 預覽上傳圖片 --%>
        function ReadURL($this) {
            chkBrowserSupFileAPI();

            if ($this.files && $this.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#imgUserPhoto').attr('src', e.target.result);
                };
                reader.readAsDataURL($this.files[0]);
            }
        }
        <%-- 修改密碼 --%>
        function chkPWD(isShowPWD) {
            $("#divOldPwdErr").empty();
            if (isShowPWD == "close") {
                isShowPWD = "open";
                $("#hidIsShowPWD").val("open");
                $("#divPwd").show(1000);
            } else {
                isShowPWD = "close";
                $("#divPwd").hide(1000);
            }
            return isShowPWD;
        }
        <%-- 檢查舊密碼輸入是否正確 --%>
        function chkOldPwd(value) {
            if (value == "") {
                return false;
            }
            $.ajax({
                url: "<%=contextPath%>/Dialog/UserProfile/ChkOldPwd",
                type: "get",
                dataType: "json",
                data: {
                    oldPwd: value
                }
            }).done(function (jsonText) {
                $("#divOldPwdErr").empty();
                if (jsonText.fail == null) {
                    var msg = $("<span/>").attr({
                        "style": "color:green;",
                        "class": "glyphicon glyphicon-ok"
                    });
                    $("#divOldPwdErr").append(msg);
                } else {
                    var fail = $("<p/>").attr("style", "color:red;").text(jsonText.fail);
                    $("#divOldPwdErr").append(fail);
                    $("#txtOldPassword").val("");
                }
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '檢查舊密碼時，系統發生錯誤！',
                    message: errorStatus.responseText
                });
            });
        }
        <%-- 檢查名稱是否已被使用 --%>
        function chkUserName(value) {
            if (value == "") {
                return false;
            }
            $.ajax({
                url: "./ChkUserName",
                type: "get",
                dataType: "json",
                data: {
                    name: value
                }
            }).done(function (jsonText) {
                $("#divNameErr").empty();
                if (jsonText.fail == null) {
                    var msg = $("<span/>").attr({
                        "style": "color:green;",
                        "class": "glyphicon glyphicon-ok"
                    });
                    $("#divNameErr").append(msg);
                } else {
                    var fail = $("<p/>").attr("style", "color:red;").text(jsonText.fail);
                    $("#divNameErr").append(fail);
                    $("#txtName").val("");
                }
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '檢查名稱是否已被使用失敗，系統發生錯誤！',
                    message: errorStatus.responseText
                });
            });
        }
        <%-- 檢查信箱是否已被使用 --%>
        function chkEmail(value) {
            if (value == "") {
                return false;
            }
            $.ajax({
                url: "./ChkEmail",
                type: "get",
                dataType: "json",
                data: {
                    email: value
                }
            }).done(function (jsonText) {
                $("#divEmailErr").empty();
                if (jsonText.fail == null) {
                    var msg = $("<span/>").attr({
                        "style": "color:green;",
                        "class": "glyphicon glyphicon-ok"
                    });
                    $("#divEmailErr").append(msg);
                } else {
                    var fail = $("<p/>").attr("style", "color:red;").text(jsonText.fail);
                    $("#divEmailErr").append(fail);
                    $("#txtEmail").val("");
                }
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '檢查信箱是否已被使用失敗，系統發生錯誤！',
                    message: errorStatus.responseText
                });
            });
        }
    </script>
    <body>
        <%
            UserHeader userHeader = (UserHeader) request.getSession().getAttribute("LoginInfo");
            String depName = userHeader.getDepHeader() == null ? "" : userHeader.getDepHeader().getDepName();
        %>
        <div class="">
            <form id="fromUserProfile" name="fromUserProfile" class="form-horizontal" action="<%=contextPath%>/Dialog/UserProfile?Save" method="post" enctype="multipart/form-data">
                <div class="form-group formDig">
                    <div class="col-md-8">
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">帳號</label>
                            <div class="col-xs-9 col-md-5 from-control ">
                                <span><%=userHeader.getAccount()%></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">單位</label>
                            <div class="col-xs-9 col-md-5 from-control ">
                                <span><%=depName%></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">身分</label>
                            <div class="col-xs-9 col-md-5 from-control ">
                                <span><%=UserLevel.getName(userHeader.getUserLevel())%></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lastname" class="col-xs-3 col-md-3  control-label">名稱</label>
                            <div class="col-xs-9 col-md-5 ">
                                <input type="text" class="form-control required" id="txtName" name="userName"  placeholder="請輸入名稱" value="<%=userHeader.getUserName()%>" required>
                            </div>
                            <div class="col-xs-12 col-xs-offset-3 col-md-12 col-md-offset-3" id="divNameErr"></div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3  control-label">Email</label>
                            <div class="col-xs-9 col-md-8 ">
                                <input type="email" class="form-control email" id="txtEmail" name="email" value="<%=userHeader.getEmail()%>" placeholder="請輸入Email" required>
                            </div>
                            <div class="col-xs-12 col-md-12 col-md-offset-3 col-xs-offset-3" id="divEmailErr"></div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3  control-label">密碼</label>
                            <div class="col-xs-8 col-md-8 " style="line-height: 35px;">
                                <a id="hrefChangePWD" href="#">變更密碼</a>
                            </div>
                        </div>
                        <div id="divPwd" style="display:none;">
                            <div class="form-group">
                                <label for="password" class="col-xs-3 col-md-3  control-label">舊密碼</label>
                                <div class="col-xs-9 col-md-5 ">
                                    <input type="password" class="form-control required" id="txtOldPassword" name="txtOldPassword" placeholder="請輸入舊密碼" required>
                                </div>
                                <div class="col-xs-12 col-md-4 col-md-offset-3 col-xs-offset-3" id="divOldPwdErr"></div>
                            </div>
                            <div class="form-group">
                                <label for="password" class="col-xs-3 col-md-3  control-label">新密碼</label>
                                <div class="col-xs-9 col-md-5 ">
                                    <input type="password" class="form-control required" id="txtNewPassword" name="txtNewPassword" placeholder="請輸入新密碼" required>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%-- 使用者照片 --%>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="col-xs-3 col-md-12 control-label" style="text-align:left;">個人頭像</label>
                            <div class="col-xs-12 col-md-12" style="margin-top:5px;padding:0px;">
                                <label for="fileUpload">
                                    <img id="imgUserPhoto" style="border: 3px solid gray;" src="<%=contextPath%>/Dialog/UserProfile/GetPhoto" alt="無法顯示圖片" />
                                </label>
                                <input type="file" id="fileUpload" name="fileUpload" accept="image/*" style="display: none;"/>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
