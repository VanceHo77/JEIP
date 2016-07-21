<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--content="0"，是表示過期的時間，如果不是0的話，就必須使用GMT的時間格式 --%>
        <meta HTTP-equiv="Expires" content="0">
        <%--不用緩存機制 --%>
        <meta HTTP-equiv="kiben" content="no-cache">
        <title>主控台</title>
    </head>
    <%final String contextP = request.getContextPath();%>
    <jsp:include page="Layout.jsp"/>
    <style>
        .huge{
            font-size: 24px;
        }
    </style>
    <script>
        $(document).ready(function () {

        });

        function showNewMsgDetail(desc, begTime, endTime) {
            desc = escape(encodeURIComponent(desc));
            begTime = escape(encodeURIComponent(begTime));
            endTime = escape(encodeURIComponent(endTime));
            BootstrapDialog.show({
                title: '最&nbsp;新&nbsp;公&nbsp;告',
                message: $('<div></div>').load('<%=contextP%>/Dialog/NewAnnouncement?desc=' + desc + "&begTime=" + begTime + "&endTime=" + endTime)
            });
        }
    </script>
    <body>
        <div id="page-wrapper">
            <div class="row" style="padding-top: 20px;">
                <div class="col-lg-12 col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3" style="text-align:left;">
                                    <i class="fa fa-flash fa-4x"></i>
                                </div>
                                <div class="col-xs-9 text-right" style="cursor: default;">
                                    <div class="huge">${NewAnnMsgTotal}</div>
                                    <div class="huge">最新公告</div>
                                </div>
                            </div>
                        </div>
                        <a href="#" >
                            <div class="panel-body" style="padding: 10px;color:#337ab7;">
                                <c:forEach var="newAnnMsg" items="${NewAnnMsg}" varStatus="loop"> 
                                    <div class="col-md-1" style="width:1%;"><span class="glyphicon glyphicon-star pull-left" aria-hidden="true" style="padding-top:3px;"></span></div><%-- 公告訊息前面的icon --%>
                                    <div class="col-md-11">
                                        <c:if test="${fn:length(newAnnMsg.announcementDesc)>27}" ><%-- 公告內容長度>38個字，則隱藏後面的訊息 --%>
                                            <a href="#" onclick="showNewMsgDetail('${newAnnMsg.announcementDesc}', '${newAnnMsg.begTime}', '${newAnnMsg.endTime}')">
                                                ${newAnnMsg.begTime.substring(0,9)}&nbsp;&nbsp;${newAnnMsg.announcementDesc.substring(0,27)}......more</a>
                                            </c:if>
                                            <c:if test="${fn:length(newAnnMsg.announcementDesc)<=27}" ><%-- 公告內容長度<=38個字 --%>
                                            <a  href="#" onclick="showNewMsgDetail('${newAnnMsg.announcementDesc}', '${newAnnMsg.begTime}', '${newAnnMsg.endTime}')">
                                                ${newAnnMsg.begTime.substring(0,9)}&nbsp;&nbsp;${newAnnMsg.announcementDesc}</a>
                                            </c:if>
                                    </div>
                                    <c:if test="${!loop.last}" ><%-- 不等於最後一筆則加入以下的div分隔線 --%>
                                        <div class="clearfix" style="border-bottom-style:dotted;color:#DDDDDD;margin-top:10px;margin-bottom:10px;padding-bottom: 10px;"></div></c:if>
                                </c:forEach>
                            </div>
                        </a>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-right" algin="right">查看更多<i class="fa fa-arrow-circle-right" style="padding-left:5px;"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3" style="text-align:left;">
                                    <i class="fa fa-file-text fa-4x"></i>
                                </div>
                                <div class="col-xs-9 text-right" style="cursor: default;">
                                    <div class="huge">12</div>
                                    <div class="huge">待簽表單</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6">
                    <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3" style="text-align:left;">
                                    <i class="fa fa-question fa-4x"></i>
                                </div>
                                <div class="col-xs-9 text-right" style="cursor: default;">
                                    <div class="huge">124</div>
                                    <div class="huge">最新問卷</div>
                                </div>
                            </div>
                        </div>
                        <a href="#">
                            <div class="panel-footer">
                                <span class="pull-left">View Details</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
