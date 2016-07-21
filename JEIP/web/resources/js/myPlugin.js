/*
 * 在plugin中，jQuery會接收一個callback(回傳)，而這個callback本身就指到DOM所，可以直接使用this等於$(this)，因為"this"已經是jQuery的物件了 
 */
(function ($) {
    /**
     * 自訂的DataGrid
     * @param jsonText json格式的資料來源
     */
    $.fn.myDataGrid = function (jsonText, settings) {
        var config = {
            "searching": false, //取消搜尋欄位的功能
            "info": true, //取消頁數資訊
            "bLengthChange": false, //取消可以給使用者選擇每頁出現幾筆的選項
            "pageLength": 20, //每頁顯示筆數
            "stateSave": true, //保留查詢結果
            "oLanguage": {
                "sProcessing": "<font  color='#337ab7' style='font-size:12pt;'>資料讀取中...</font>",
                "sZeroRecords": "<font  color='#337ab7' style='font-size:12pt;'>目前條件查詢不到相關資料！</font>",
                "sEmptyTable": "<font  color='#337ab7' style='font-size:12pt;'>表中無資料存在！</font>",
                "oPaginate": {
                    "sFirst": "<font  color='#337ab7' style='font-size:12pt;'>首頁</font>",
                    "sPrevious": "<font  color='#337ab7' style='font-size:12pt;'>上一頁</font>",
                    "sNext": "<font  color='#337ab7' style='font-size:12pt;'>下一頁</font>",
                    "sLast": "<font  color='#337ab7' style='font-size:12pt;'>末頁</font>"
                },
                "sInfo": "<font  color='#337ab7' style='font-size:12pt;'>資料總筆數： _TOTAL_ 筆</font>",
                "sInfoEmpty": "<font  color='#337ab7' style='font-size:12pt;'>資料總筆數： _TOTAL_ 筆</font>"
            } //多語言配置
        };
        //原先的預設值(config)再擴充客製參數
        config = $.extend(config, settings);
        var thisObj = this;
        var tbody = $("<tbody/>");

        //檢查table是否已經是DataTable
        if ($.fn.DataTable.isDataTable(thisObj)) {
            //銷毀DataTable
            this.DataTable().destroy();
            //清除表格資料
            this.find("tbody").remove();
        }
        var tr, td;
        var seqNO = 1;//序號
        var linkHandler;//超連結事件
        var buttonHandler;//按鈕的事件
        var i = 0;//每一列按鈕的計數
        var buttonValue = {};//每一列按鈕的value
        //跑資料總筆數
        $.each(jsonText, function (key, value) {
            //取出每一列資料的value値
            tr = $("<tr/>");
            $.each(value, function (subKey, subValue) {
                if (settings.linkHandler == null) {
                    if (typeof subValue == "object") {
                        var v = "";
                        //遍歷物件
                        for (x in subValue) {
                            v += subValue[x];
                        }
                        td = $("<td/>").html(v);
                    } else {//放在jsonObject
                       td = $("<td/>").html(subValue);
                    }
                    tr.append(td);
                } else {
                    //欄位超連結
                    if (settings.linkHandler != null) {
                        var link = $('<a/>').attr("style", "cursor:pointer");
                        $.each(settings.linkHandler, function (key, value) {
                            if (value.linkColumn == subKey) {
                                if (linkHandler == null) {
                                    linkHandler = value.handler;
                                }
                                link.html(chkMsgLength(subValue));
                                td = $("<td/>").append(link);
                            } else {
                                td = $("<td/>").html(chkMsgLength(subValue));
                            }
                            tr.append(td);
                        });
                    }
                }
                //button
                if (settings.buttonHandler != null) {
                    $.each(settings.buttonHandler, function (key, value) {
                        //將值先存起來，判斷key值是存在jsonObject或jsonArray
                        if (value.value == subKey || Number.isInteger(subKey)) {
                            buttonValue[i] = subValue;
                            i++;
                        }
                    });
                }
            });
            //在資料列的前方增加序號欄位
            tr.find("td").eq(0).before($("<td/>").text(seqNO));
            seqNO++;

            //若有button欄位則增加在列尾
            if (settings.buttonHandler != null) {
                var btn = $('<button/>').addClass("btn btn-danger btn-sm");
                $.each(settings.buttonHandler, function (key, value) {
                    //複製handler
                    if (buttonHandler == null) {
                        buttonHandler = value.handler;
                    }
                    if (value.label == "") {
                        throw new Error("請設定myDataGrid，參數buttonHandle的label名稱");
                    } else if (value.value == "") {
                        throw new Error("請設定myDataGrid，參數buttonHandle的value欄位");
                    }
                    if (value.label != "") {
                        btn.attr("name", value.label).text(value.label);
                    }
                    if (buttonValue != null) {
                        for (var i in buttonValue) {
                            //放在jsonArray
                            if (typeof buttonValue[i] == "object") {
                                var v = "";
                                //遍歷物件
                                for (x in buttonValue[i]) {
                                    v += buttonValue[i][x];
                                }
                                btn.attr("value", v);
                            } else {//放在jsonObject
                                btn.attr("value", buttonValue[i]);
                            }
                        }
                    }
                });
                td = $("<td/>").append(btn);
                tr.append(td);
            }
            tbody.append(tr);
        });

        thisObj.append(tbody);
        //超連結事件不為null
        if (linkHandler != null) {
            thisObj.find('tbody').on('click', 'a', linkHandler);
        }
        //按鈕事件不為null
        if (buttonHandler != null) {
            thisObj.find('tbody').on('click', 'button', buttonHandler);
        }

        thisObj.DataTable(config);
        //註冊HightLight效果
        RegHiLight();
        thisObj.on('draw.dt', function () {
            RegHiLight();
        });
    };
    //Table的HightLight效果
    function RegHiLight() {
        $("tr").not(':first').hover(
                function () {
                    $(this).css("background", "#a2c7ec");
                },
                function () {
                    $(this).css("background", "");
                }
        );
    }

    //判斷字串長度是否超過30，若是則刪除後面的字串
    function chkMsgLength(msg) {
        msg = msg.toString();
        if (msg.indexOf("hidden") == -1 && msg.length > 31) {
            msg = msg.substring(0, 30) + "...";
        }
        return msg;
    }

    //DatePicker
    $.fn.myDatePicker = function (settings) {
        //限制只能書數字和/
        $(this).keypress(function (e) {
            var code = e.keyCode ? e.keyCode : e.which;
            if (code < 47 || code > 57) {
                e.preventDefault();
                return;
            }

        });
        var config = {
            "hiddenValue": false,
            "sub": "",
            "add": "",
            //以下為官方參數
            autoclose: true,
            orientation: "bottom left",
//            format: 'yyyy/mm/dd',
            language: 'zh-TW',
            todayHighlight: true,
            todayBtn: 'linked',
            format: {
                toDisplay: function (date, format, language) {
                    var d = new Date(date);
                    d.setDate(d.getDate());
                    //判斷輸入的是民國年還是西元年
                    var year;
                    if (d.getFullYear() < 999) {
                        //民國年
                        year = d.getFullYear();
                    } else {
                        //西元年轉民國年
                        year = d.getFullYear() - 1911;
                    }
                    year = year < 100 ? ('0' + year) : year;
                    var month = d.getMonth() + 1;
                    month = month < 10 ? ('0' + month) : month;
                    var day = d.getDate();
                    day = (day < 10 ? ('0' + day) : day);
                    return year + '/' + month + '/' + day;
                },
                toValue: function (date, format, language) {
                    var d = new Date(date);
                    d.setDate(d.getDate());
                    return new Date(d);
                }
            }
        };
        config = $.extend(config, settings);

        this.datepicker(config).on('show', function (e) {//出現日歷時
            //修改CSS
            $("th[class='prev']").eq(0).css('cursor', "pointer");
            $("th[class='next']").eq(0).css('cursor', "pointer");
            //todayHighlight失效，自己補背景淡藍色
            $(".today.day").css('background-color', "#d1edff");
            $.each($(".today"), function (key, value) {
                if ($(this).text() == "今天") {
                    $(this).css('cursor', "pointer");
                }
            });
        });

        var curDate = "";
        //是否隱藏當前日期
        if (!config.hiddenValue) {
            //取得當前日期
            var d = new Date();
            var month = d.getMonth() + 1;
            var day = d.getDate();
            curDate = d.getFullYear() + '/' +
                    (month < 10 ? '0' : '') + month + '/' +
                    (day < 10 ? '0' : '') + day;

            if (config.sub != "" && config.add != "") {
                throw new Error("參數sub和add請擇一輸入。");
            } else {
                var today = new Date(curDate);
                var newday = new Date(today);
                //減日期
                if (config.sub != "") {
                    newday.setDate(today.getDate() - parseInt(config.sub));
                }
                //加日期
                if (config.add != "") {
                    newday.setDate(today.getDate() + parseInt(config.add));
                }
                newday = new Date(newday);
                curDate = myFormatter(newday);
            }
        }
        this.datepicker('update', curDate);
    };
    function myFormatter(date) {
        return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate();
    }

    //TimePicker
    $.fn.myTimePicker = function () {
        this.clockpicker({
            placement: 'bottom',
            align: 'left',
            autoclose: true,
            default: 'now'
        });
    }

})(jQuery);


//檢查目前瀏覽器是否支援File API
function chkBrowserSupFileAPI() {
    if (!window.File || !window.FileReader) {
        BootstrapDialog.show({
            title: "上傳檔案錯誤訊息",
            message: "您目前的瀏覽器不支援檔案上傳功能，請更換瀏覽器後再試試看。",
            buttons: [{
                    label: '確定',
                    action: function (dialog) {
                        dialog.close();
                    }
                }]
        });
        return;
    }
}

/* 附件檔名特殊字判斷 */
function boolFileSpecial(fname) {
    var containSpecial = new RegExp(/^[\u4e00-\u9fa5a-zA-Z0-9\.\-\_]+$/);
    return containSpecial.test(fname);
}