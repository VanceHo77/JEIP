<%@page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>系統訊息畫面</title>
        <%final String contextPath = request.getContextPath();%>
        <script src="<%=contextPath%>/resources/js/jquery-1.12.0.min.js"></script>
        <script src="<%=contextPath%>/resources/js/jquery-ui.min.js"></script>
        <script src="<%=contextPath%>/resources/js/bootstrap.min.js"></script>
        <script src="<%=contextPath%>/resources/js/bootstrap-dialog.min.js"></script>

        <link href="<%=contextPath%>/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="<%=contextPath%>/resources/css/bootstrap-dialog.min.css" rel="stylesheet" type="text/css">
    </head>
    <style>
        .sysdiv{
            background-color: #f2dede;
            line-height:35px;
        }
        .usrdiv{
            background-color: #e9f4c2;
            line-height:35px;
        }
        .errtitle{
            font-family:微軟正黑體;
            font-size: 20px;
            font-weight: bold;
        }
        .errMsg{
            font-family:微軟正黑體;
            font-size: 16px;
            padding: 15px;
        }
        .bootstrap-dialog-footer-buttons{
            text-align:center;
        }
    </style>
    <script>
        $(document).ready(function () {
            var exName = "${ExceptionName}";
            var sysMsg = "${SysErrMessage}";
            var usrMsg = "${UserErrMessage}";
            var errMsg = '';
            var usrTitle = $("<div/>").addClass("usrdiv errtitle").text("使用者訊息：");
            var usrMsg = $("<div/>").addClass("errMsg").html(usrMsg);
            if (sysMsg == '') {
                errMsg = $("<div/>").append(usrTitle).append(usrMsg);
            } else {
                var sysTitle = $("<div/>").addClass("sysdiv errtitle").html("系統訊息：");
                var exName = $("<div/>").addClass("errMsg").html(exName);
                var sysMsg = $("<div/>").addClass("errMsg").html(sysMsg);

                var sysDiv = $("<div/>").append(sysTitle).append(exName).append(sysMsg);
                var usrDiv = $("<div/>").append(usrTitle).append(usrMsg);
                errMsg = $("<div/>").append(sysDiv).append(usrDiv);
            }
            BootstrapDialog.show({
                title: $("<font/>").addClass("errtitle").html("錯誤訊息"),
                message: errMsg,
                buttons: [{
                        label: '確定',
                        action: function (dialog) {
                            location.href = "${ReturnPath}";
                        }
                    }]
            });
            setTimeout(function () {
                $("button[class='btn btn-default']").prop("class", "btn btn-primary");
            }, 200);

            $(document).on("keyup", function (e) {
                var code = e.which;
                if (code == 13) {
                    location.href = "${ReturnPath}";
                }
            });

        });
    </script>
    <body>

    </body>
</html>
