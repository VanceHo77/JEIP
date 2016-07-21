<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--content="0"，是表示過期的時間，如果不是0的話，就必須使用GMT的時間格式 --%>
        <meta HTTP-equiv="Expires" content="0">
        <%--不用緩存機制 --%>
        <meta HTTP-equiv="kiben" content="no-cache">
        <title>JEIP入口網站-登入</title>
        <%final String contextPath = request.getContextPath();%>
        <script src="<%=contextPath%>/resources/js/jquery-1.12.0.min.js"></script>
        <%-- Bootstrap Core JavaScript --%>
        <script src="<%=contextPath%>/resources/js/bootstrap.min.js"></script>
        <%-- jQueryUI --%>
        <script src="<%=contextPath%>/resources/js/jquery-ui.min.js"></script>
        <%-- 遮罩 --%>
        <script src="<%=contextPath%>/resources/js/blockUI.js"></script>
        <%-- 對話框 --%>
        <script src="<%=contextPath%>/resources/js/bootstrap-dialog.min.js"></script>
        <%-- 表單驗證 --%>
        <script src="<%=contextPath%>/resources/js/jquery.validate.js"></script>

        <%-- Bootstrap Core CSS --%>
        <link href="<%=contextPath%>/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <%-- Bootstrap 對話框 CSS --%>
        <link href="<%=contextPath%>/resources/css/bootstrap-dialog.min.css" rel="stylesheet" type="text/css">
        <style>
            body {
                margin:0;
                padding:0;
                -moz-background-size: cover;
                background-size: cover;
                font-family: 微軟正黑體;
                font-size: large;
            }
        </style>
    </head>
    <script>
        $(document).ready(function () {
        <%--註冊畫面套用驗證 --%>
            $("#signupform").validate();
        <%--記住帳號 --%>
            getUserAccount();
        <%--登入 --%>
            $("#loginform").submit(function () {
                setCookie();
            });
        <%--前往註冊 --%>
            $("#hrefSignup").click(function () {
                $('#loginbox').hide();
                $('#signupbox').show();
            });
        <%-- 回登入畫面 --%>
            $("a[name='hrefBackSingin']").click(function () {
                $('#signupbox').hide();
                $("#forgetbox").hide();
                $('#loginbox').show();
            });
        <%-- 忘記密碼 --%>
            $("#hrefForget").click(function () {
                $('#loginbox').hide();
                $('#forgetbox').show();
            });

        <%-- 註冊會員 --%>
            $("#btnSignup").click(function () {
                var frmValidate = $('#signupform').validate();
                if (!frmValidate.form()) {
                    return false;
                }
                $.ajax({
                    url: "./Signup",
                    type: "get",
                    dataType: "json",
                    data: $("#signupform").serialize(),
                    beforeSend: function () {
                        $(window).block({message: "會員註冊及寄送驗證信中，請稍後..."});
                    }
                }).done(function (jsonText) {
                    setTimeout(function () {
                        if (jsonText.fail == null) {
                            $(window).block({message: "註冊成功，請至註冊信箱中收取帳號開通驗證信。"});

                            setTimeout(function () {
                                location.reload();
                            }, 5000);
                        } else {
                            $(window).unblock();
                            BootstrapDialog.show({
                                title: '註冊失敗',
                                message: jsonText.fail
                            });
                        }
                    }, 2000);
                }).fail(function (errorStatus) {
                    $(window).unblock();
                    BootstrapDialog.show({
                        title: '註冊失敗，系統發生錯誤！',
                        message: errorStatus.responseText
                    });
                });
            });
        <%-- 忘記密碼 --%>
            $("#btnForget").click(function () {
                var frmValidate = $('#forgetform').validate();
                if (!frmValidate.form()) {
                    return false;
                }
                $.ajax({
                    url: "./ForgetPwd",
                    type: "get",
                    dataType: "json",
                    data: $("#forgetform").serialize(),
                    beforeSend: function () {
                        $(window).block({message: "會員密碼寄出中，請稍後..."});
                    }
                }).done(function (jsonText) {
                    setTimeout(function () {
                        if (jsonText.fail == null) {
                            $(window).block({message: "寄出成功，請至註冊信箱中收取會員密碼。"});

                            setTimeout(function () {
                                location.reload();
                            }, 5000);
                        } else {
                            $(window).unblock();
                            BootstrapDialog.show({
                                title: '寄出失敗',
                                message: jsonText.fail
                            });
                        }
                    }, 2000);
                }).fail(function (errorStatus) {
                    BootstrapDialog.show({
                        title: '寄出失敗，系統發生錯誤！',
                        message: errorStatus.responseText
                    });
                });
            });
        <%-- FB登入 --%>
            $("#btnFBLogin").click(function () {
                FB.login(function (response) {
                    if (response.authResponse) {
                        var email;
                        FB.api('/me?fields=id,name,email', function (response) {
                            email = response.email;
                            FBLogin(email);
                        });
                    }
                }, {
                    scope: 'email'
                });
            });
        <%-- 檢查帳號是否已被使用 --%>
            $("#txtUserAccount").change(function () {
                var account = $.trim($(this).val());
                if (account == "") {
                    return false;
                }
                $.ajax({
                    url: "./ChkAccount",
                    type: "get",
                    dataType: "json",
                    data: {
                        account: account
                    }
                }).done(function (jsonText) {
                    $("#divAccountErr").empty();
                    if (jsonText.fail == null) {
                        var msg = $("<span/>").attr({
                            "style": "color:green;",
                            "class": "glyphicon glyphicon-ok"
                        });
                        $("#divAccountErr").append(msg);
                    } else {
                        var fail = $("<p/>").attr("style", "color:red;").text(jsonText.fail);
                        $("#divAccountErr").append(fail);
                        $("#txtUserAccount").val("");
                    }
                }).fail(function (errorStatus) {
                    BootstrapDialog.show({
                        title: '檢查帳號是否已被使用失敗，系統發生錯誤！',
                        message: errorStatus.responseText
                    });
                });
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
                var name = $.trim($(this).val());
                if (name == "") {
                    return false;
                }
                $.ajax({
                    url: "./ChkUserName",
                    type: "get",
                    dataType: "json",
                    data: {
                        name: name
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
            });
        <%-- 檢查信箱是否已被使用 --%>
            $("#txtEmail").change(function () {
                var email = $.trim($(this).val());
                if (email == "") {
                    return false;
                }
                $.ajax({
                    url: "./ChkEmail",
                    type: "get",
                    dataType: "json",
                    data: {
                        email: email
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
            });
        });

        function getUserAccount() {
            var cookies = document.cookie;
            if (cookies != "") {
                var cookies = cookies.split(";");
                $.each(cookies, function (key, value) {
                    if (value.indexOf("UserAccount") >= 0) {
                        $("#txtAccount").val(value.split("=")[1]);
                        $("#btnRemember").attr({checked: true});
                    }
                });
            }
        }
        function setCookie() {
            if ($("#btnRemember").is(":checked")) {
                var userid = $.trim($("#txtAccount").val());
                if (userid != "") {
                    document.cookie = "UserAccount=" + userid + "; expires=Thu, 18 Dec 3000 12:00:00 UTC";
                }
            } else {
                document.cookie = "UserAccount=; expires=Mon, 01 Jan 0000 00:00:00 UTC";
            }
        }
        function FBLogin(email) {
            $("#hidEmail").val(email);
            $("#loginform").attr("action", "<%=contextPath%>/FBLogin");
            $("#loginform").submit();
        }
    </script>
    <script>
        window.fbAsyncInit = function () {
            FB.init({
                appId: '1524381931200507',
                xfbml: true,
                version: 'v2.5'
            });
        };

        (function (d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) {
                return;
            }
            js = d.createElement(s);
            js.id = id;
            js.src = "//connect.facebook.net/en_US/sdk.js";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));

        function checkLoginState() {
            FB.login(function (response) {
                if (response.authResponse) {
//                    access_token = response.authResponse.accessToken; //get access token
//                    user_id = response.authResponse.userID; //get FB UID
                    GraphAPI();
                }
            }, {
                scope: 'email'
            });
        }

        function GraphAPI() {
            var name, email;
            FB.api('/me?fields=id,name,email', function (response) {
                name = response.name;
                email = response.email;
                FBsignup(name, email);
            });
        }

        function FBsignup(name, email) {
            $.ajax({
                url: "./FBsignup",
                type: "get",
                dataType: "json",
                data: {
                    name: name,
                    email: email
                },
                beforeSend: function () {
                    $(window).block({message: "會員註冊中，請稍後..."});
                }
            }).done(function (jsonText) {
                setTimeout(function () {
                    if (jsonText.fail == null) {
                        $(window).block({message: "註冊成功，5秒後頁面將重新導回登入畫面。"});
                        setTimeout(function () {
                            location.reload();
                        }, 5000);
                    } else {
                        $(window).unblock();
                        BootstrapDialog.show({
                            title: '註冊失敗',
                            message: jsonText.fail
                        });
                        setTimeout(function () {
                            location.reload();
                        }, 5000);
                    }
                }, 2000);
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '使用FB帳號註冊失敗，系統發生錯誤！',
                    message: errorStatus.responseText
                });
            });
        }
    </script>
    <body>
        <div class="container">    
            <div id="loginbox" style="margin-top:50px;" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">                    
                <div class="panel panel-info" >
                    <div class="panel-heading">
                        <div>J&nbsp;E&nbsp;I&nbsp;P&nbsp;入&nbsp;口&nbsp;網&nbsp;站&nbsp;登&nbsp;入</div>
                        <div style="float:right; font-size: 80%; position: relative; top:-10px"><a href="#" id="hrefForget">忘記密碼?</a></div>
                    </div>     

                    <div style="padding-top:30px" class="panel-body" >
                        <div style="display:none" id="login-alert" class="alert alert-danger col-sm-12"></div>

                        <form id="loginform" class="form-horizontal" role="form" action="<%=contextPath%>/Login" method="post">
                            <input type="hidden" id="hidEmail" name="hidEmail"/>
                            <div style="margin-bottom: 25px" class="input-group">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                                <input id="txtAccount" type="text" class="form-control" name="UserAccount" value=""  placeholder="帳號" required>                                        
                            </div>
                            <div style="margin-bottom: 25px" class="input-group">
                                <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                                <input id="login-password" type="password" class="form-control" name="Password" value="" placeholder="密碼" required>
                            </div>
                            <div class="input-group">
                                <div class="checkbox">
                                    <label>
                                        <input id="btnRemember" type="checkbox"/> 記住帳號
                                    </label>
                                </div>
                            </div>

                            <div style="margin-top:10px" class="form-group">
                                <div class="col-sm-12 controls">
                                    <button type="submit" name="btnLogin" class="btn btn-success">登入</button>
                                    <input type="button" id="btnFBLogin" class="btn btn-primary" value="使用Facebook登入"/>
                                </div>
                            </div>
                        </form>
                        <div class="form-group">
                            <div class="col-md-12 control">
                                <div style="border-top: 1px solid#888; padding-top:15px; font-size:85%" >
                                    沒有帳號嗎? 
                                    <a href="#" id="hrefSignup">
                                        註冊
                                    </a>
                                </div>
                            </div>
                        </div>  
                    </div>                     
                </div>  
            </div>
            <div id="signupbox" style="display:none; margin-top:50px" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <div>註冊</div>
                        <div style="float:right; font-size: 85%; position: relative; top:-10px"><a name="hrefBackSingin" href="#">回登入畫面</a></div>
                    </div>  
                    <div class="panel-body" >
                        <form id="signupform" class="form-horizontal" role="form" action="<%=contextPath%>/Signup" method="post">

                            <div class="form-group">
                                <label for="email" class="col-md-3 control-label">帳號</label>
                                <div class="col-md-5">
                                    <input type="text" class="form-control required" id="txtUserAccount" name="account" placeholder="請輸入帳號" value="vance" required>
                                </div>
                                <div class="col-md-4" id="divAccountErr"></div>
                            </div>

                            <div class="form-group">
                                <label for="password" class="col-md-3 control-label">密碼</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control required" id="txtPasswd" name="password" value="123" placeholder="請輸入密碼" required>
                                </div>
                                <div class="col-md-4" id="divPwdErr"></div>
                            </div>

                            <div class="form-group">
                                <label for="password" class="col-md-3 control-label">確認密碼</label>
                                <div class="col-md-5">
                                    <input type="password" class="form-control required" id="txtPasswdAgain" name="txtPasswdAgain" value="123" placeholder="請再輸入一次密碼" required>
                                </div>
                                <div class="col-md-4" id="divChkPwdErr"></div>
                            </div>

                            <div class="form-group">
                                <label for="lastname" class="col-md-3 control-label">名稱</label>
                                <div class="col-md-5">
                                    <input type="text" class="form-control required" id="txtName" name="userName" value="vance" placeholder="請輸入名稱" required>
                                </div>
                                <div class="col-md-4" id="divNameErr"></div>
                            </div>

                            <div class="form-group">
                                <label for="email" class="col-md-3 control-label">Email</label>
                                <div class="col-md-5">
                                    <input type="email" class="form-control email" id="txtEmail" name="email" value="gn770612@yahoo.com.tw" placeholder="請輸入Email" required>
                                </div>
                                <div class="col-md-4" id="divEmailErr"></div>
                            </div>

                            <div class="form-group">                                     
                                <div class="col-md-offset-3 col-md-9">
                                    <button id="btnSignup" type="button" class="btn btn-info"><i class="icon-hand-right"></i> &nbsp 註冊</button>
                                    <span style="margin-left:8px;">or</span>  
                                </div>
                            </div>
                        </form>
                        <div style="border-top: 1px solid #999; padding-top:20px"  class="form-group">
                            <div class="col-md-offset-3 col-md-9">
                                <div class="fb-login-button" data-size="large" data-show-faces="false" data-auto-logout-link="false" onlogin="checkLoginState();">使用Facebook註冊</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="forgetbox" style="display:none; margin-top:50px" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <div>忘&nbsp;記&nbsp;密&nbsp;碼</div>
                        <div style="float:right; font-size: 85%; position: relative; top:-10px"><a name="hrefBackSingin" href="#">回登入畫面</a></div>
                    </div>  
                    <div class="panel-body" >
                        <form id="forgetform" class="form-horizontal" role="form" action="<%=contextPath%>/ForgetPwd" method="post">
                            <div class="form-group">
                                <label for="email" class="col-md-3 control-label">Email</label>
                                <div class="col-md-9">
                                    <input type="email" class="form-control email" name="txtEmail" value="gn770612@yahoo.com.tw" placeholder="請輸入Email" required>
                                </div>
                            </div>
                        </form>
                        <div class="form-group">                                       
                            <div class="col-md-offset-3 col-md-9">
                                <button id="btnForget" type="button" class="btn btn-success"><i class="icon-hand-right"></i>寄&nbsp;出</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div> 
        </div>

    </body>
</html>
