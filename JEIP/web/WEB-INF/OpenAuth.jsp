<%@page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--content="0"，是表示過期的時間，如果不是0的話，就必須使用GMT的時間格式 --%>
        <meta HTTP-equiv="Expires" content="0">
        <%--不用緩存機制 --%>
        <meta HTTP-equiv="kiben" content="no-cache">
        <title>JEIP入口網站-會員驗證</title>
        <%final String contextPath = request.getContextPath();%>
        <script src="<%=contextPath%>/resources/js/jquery-1.12.0.min.js"></script>
        
        <link href="<%=contextPath%>/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
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
            setTimeout(function () {
                location.href = "./";
            }, 5000);
        });
    </script>

    <body>
        <div class="container"> 
            <div id="forgetbox" style="margin-top:50px" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <div>會&nbsp;員&nbsp;驗&nbsp;證</div>
                    </div>  
                    <div class="panel-body" >
                        <div class="form-group">
                            <div class="col-md-12">
                                <span><h4>會員開通成功，系統將於5秒後重新返回系統首頁。
                                        <span><h5><a href="./">或手動返回系統首頁</a></h5></span>
                                    </h4></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 
        </div>
    </body>
</html>
