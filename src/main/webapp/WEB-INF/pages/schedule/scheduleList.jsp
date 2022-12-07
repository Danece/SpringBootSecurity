<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
    <head>
        <title>Schedule List</title>
    </head>
    <%@ include file="/WEB-INF/pages/common/header.jsp" %>
	<body style="padding: 20px; width: 50%; margin: 50px auto;">
        <div class="page_title" >
            <h1><%=request.getAttribute("SCHEDULE_TITLE") %></h1>
            <button onclick="location.href='/welcome';" type="button" class="btn btn-outline-info" ><%=request.getAttribute("BTN_RETURN") %></button>
        </div>
		
        <table style="text-align: center; width: 100%;">
            <tr>
                <td>
                    <i>*</i> <%=request.getAttribute("SCHEDULELIST_ROW_SCHEDULE_NAME") %>
                </td>
                <td>
                    <input type="text" id="input_name" disabled>
                </td>
                <td>
                    <i>*</i> <%=request.getAttribute("SCHEDULELIST_ROW_EXECUTION_TIME") %>
                </td>
                <td>
                    <input type="text" id="input_cron">
                </td>
            </tr>
        </table>
        <br>
        <button onclick="checkInput('updateSchedule');" type="button" class="btn btn-outline-primary" ><%=request.getAttribute("BTN_SAVE") %></button>
        <label id="resultMsg" class="resultMsg"></label>
		<br><br>
        <hr>
        <table class="display" id="table" style="width: 100%;"></table>
	</body>
</html>

<script type="text/javascript">
    
    init();

    // 初始化
    function init() {
        var dataset = [];

        // 取得排程DB資料
        $.ajax({
			type: "GET",
			contentType: "application/json",
			url: "/api/schedule",
			dataType: 'json',
            async: false,
			cache: false,
			timeout: 600000,
			success: function (data) {
                for (var i=0; i<data.content.length; i++) {
                    dataset.push([data.content[i].name, data.content[i].cron]);
                }
			},
			error: function (e) {
			}
		});

        var lang = "";
        $.ajax({
			type: "GET",
			contentType: "application/json",
			url: "/lang",
			dataType: 'json',
            async: false,
			cache: false,
			timeout: 600000,
            success: function (data) {
                switch(data.lang) {
                    case "zh_TW":
                        lang = "/jsons/TableLanguage_TW.json";
                        break;
                    case "en_US":
                        lang = "";
                        break;
                }
			},
			error: function (e) {
                console.log(e);
			}
		});

        // 建立 DataTables 空實體
        $('#table').empty();
        // 建立 DataTables 內容
        var table = $('#table').DataTable({
            data: dataset,
            columns: [
                {title: '<%=request.getAttribute("SCHEDULELIST_ROW_SCHEDULE_NAME") %>'},
                {title: '<%=request.getAttribute("SCHEDULELIST_ROW_EXECUTION_TIME") %>'}
            ],
            language: {
                url : lang
            }
        });
        // 設定 Click 動作
        $('#table tbody').on('click', 'tr', function () {
            var data = table.row(this).data();
            document.getElementById("resultMsg").classList.remove("resultMsg_success", "resultMsg_failure");
            document.getElementById("resultMsg").innerText = "";
            document.getElementById("input_name").value = data[0];
            document.getElementById("input_cron").value = data[1];
        });
    }

    // 檢查必填項目
    function checkInput(funName) {
        var cron = document.getElementById("input_cron").value;
        document.getElementById("resultMsg").classList.remove("resultMsg_success", "resultMsg_failure");
        document.getElementById("resultMsg").innerText = "";
        switch(funName) {
            case "updateSchedule":
                var cron = document.getElementById("input_cron").value;
                if ("" == cron) {
                    document.getElementById("resultMsg").classList.add("resultMsg_failure");
                    document.getElementById("resultMsg").innerText = '<%=request.getAttribute("MSG_REQUIREDVALUE") %>';

                } else if (6 != cron.split(' ').length || !checkout_shcheduleCron(cron.split(' '))) {
                    document.getElementById("resultMsg").classList.add("resultMsg_failure");
                    document.getElementById("resultMsg").innerText = '[<%=request.getAttribute("execution_time") %>] <%=request.getAttribute("MSG_FORMATERROR") %>';

                } else {
                    updataSchedule();
                }
                break;
            default:
        }
    }

    // 更新排程資訊
    function updataSchedule() {
        var values = {}
		values["scheduleName"] = document.getElementById("input_name").value;
		values["cronString"] = document.getElementById("input_cron").value;
        $.ajax({
			type: "POST",
			contentType: "application/json",
			url: "/api/schedule",
			data: JSON.stringify(values),
			dataType: 'json',
			cache: false,
			timeout: 600000,
			success: function (data) {
                var table = $('#table').DataTable();
                // clear datatable
                table.clear().draw();
                // destroy datatable
                table.destroy();
                // clear input
                document.getElementById("input_name").value = "";
                document.getElementById("input_cron").value = "";
                
                document.getElementById("resultMsg").classList.add("resultMsg_success");
                document.getElementById("resultMsg").innerText = '<%=request.getAttribute("SUCCESS") %>';
                init();
			},
			error: function (e) {
			}
		});
    }
</script>