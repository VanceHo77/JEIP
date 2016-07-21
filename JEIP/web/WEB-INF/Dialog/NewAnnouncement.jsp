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
        <title>NewAnnMsg</title>
    </head>
    <%final String contextPath = request.getContextPath();%>
    <script>
        $(document).ready(function () {

        });
    </script>
    <body>
        <div class="">
            <form id="fromNewAnn" name="fromNewAnn" class="form-horizontal" action="<%=contextPath%>/Dialog/NewAnnouncement?doDownLoad" method="post" enctype="multipart/form-data">
                <div class="form-group formDig" >
                    <div class="col-md-12">
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">開始日期</label>
                            <div class="col-xs-9 col-md-9 from-control ">
                                <span>${BegTime}</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">結束日期</label>
                            <div class="col-xs-9 col-md-9 from-control ">
                                <span>${EndTime}</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">公告內容</label>
                            <div class="col-xs-9 col-md-9 from-control ">
                                <span>${Desc}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
