<%-- 
    Document   : ShowNewAnnDetail
    Created on : 2016/8/25, 下午 08:28:36
    Author     : Vance
--%>
<%@page import="com.model.announcementheader.AnnouncementHeader"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ShowNewAnnDetail</title>
    </head>
    <%final String contextP = request.getContextPath();%>
    <script>
        $(document).ready(function () {
            queryList();
            $("#btnQuery").click(queryList);
        });//end_ready

        function queryList() {
            $.ajax({
                url: "<%=contextP%>/Dialog/ShowNewAnnDetail?query",
                type: "post",
                dataType: "json",
                data: {
                    queryStr: $("#txtQuery").val()
                }
            }).done(function (jsonText) {
                if (jsonText.fail != null) {
                    BootstrapDialog.show({
                        title: '查詢結果',
                        message: jsonText.fail
                    });
                    return false;
                }
                var annid;
                $('#table1').myDataGrid($.parseJSON(jsonText.success), {
                    "aoColumnDefs": [//欄位的預設值
                        {"width": "5%", "targets": [0]},
                        {"width": "60%", "targets": [1]}
                    ],
                    "linkHandler": [{
                            "linkColumn": "AnnouncementDesc",
                            handler: function () {
                                annid = $(this).parents("tr").find("#hidAnnID").val();
                                doModify(annid);
                            }
                        }]
                });
            }).fail(function (errorStatus) {
                BootstrapDialog.show({
                    title: '讀取失敗，系統發生錯誤！',
                    message: errorStatus.responseText
                });
            });
            return false;
        }
    </script>
    <body>
        <div class="DialogWraper">
            <form class="form-horizontal" >
                <div class="form-group formDig" >
                    <div class="col-md-12">
                        <div class="form-group">
                            <label for="email" class="col-xs-3 col-md-3 control-label">關鍵字查詢</label>
                            <div class="col-xs-5 col-md-5">
                                <input type="text" class="form-control" id="txtQuery" name="txtQuery"/>
                            </div>
                            <div>
                                <button class="btn btn-success" id="btnQuery">查詢</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="dataTable_wrapper">
                                <table id="table1" class="table table-striped table-bordered dt-responsive nowrap hover" cellspacing="0" width="100%">
                                    <thead>
                                        <tr>
                                            <th style="width: 5%">序</th>
                                            <th style="width: 80%">公布主旨</th>
                                            <th style="width: 15%">公布日期</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
