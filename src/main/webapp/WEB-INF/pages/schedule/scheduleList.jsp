<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" type="text/javascript"></script>
	<link rel="stylesheet" href="https://unpkg.com/purecss@2.0.6/build/pure-min.css">
	<link rel="stylesheet" type="text/css" href="/static/global.css" />
	<meta charset="UTF-8">
    <link rel="stylesheet" href="//cdn.datatables.net/1.13.1/css/jquery.dataTables.min.css" type="text/css">
    <script src="//cdn.datatables.net/1.13.1/js/jquery.dataTables.min.js"></script>
    <script src="../../../scripts/global_function.js"></script>
</head>
	<body style="padding: 20px; width: 50%; margin: 50px auto;">
        <div style="display: flex; justify-content: space-between; align-items: center;" >
            <h1><%=request.getAttribute("title") %></h1>
            <button onclick="location.href='/welcome';" ><%=request.getAttribute("btn_return") %></button>
        </div>
		
        <table style="text-align: center; width: 100%;">
            <tr>
                <td>
                    <i>*</i> <%=request.getAttribute("schedule_name") %>
                </td>
                <td>
                    <input type="text" id="input_name" disabled>
                </td>
                <td>
                    <i>*</i> <%=request.getAttribute("execution_time") %>
                </td>
                <td>
                    <input type="text" id="input_cron">
                </td>
            </tr>
        </table>
        <br>
        <button onclick="checkInput('updateSchedule');" ><%=request.getAttribute("btn_save") %></button>
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

        // 建立 DataTables 空實體
        $('#table').empty();
        // 建立 DataTables 內容
        var table = $('#table').DataTable({
            data: dataset,
            columns: [
                {title: '<%=request.getAttribute("schedule_name") %>'},
                {title: '<%=request.getAttribute("execution_time") %>'}
            ],
            language: {
                url : "/jsons/TableLanguage_TW.json"
            }
        });
        // 設定 Click 動作
        $('#table tbody').on('click', 'tr', function () {
            var data = table.row(this).data();
            document.getElementById("resultMsg").classList.remove("resultMsg_success", "resultMsg_failure");;
            document.getElementById("resultMsg").innerText = "";
            document.getElementById("input_name").value = data[0];
            document.getElementById("input_cron").value = data[1];
        });
    }

    // 檢查必填項目
    function checkInput(funName) {
        var cron = document.getElementById("input_cron").value;
        document.getElementById("resultMsg").classList.remove("resultMsg_success", "resultMsg_failure");;
        document.getElementById("resultMsg").innerText = "";
        switch(funName) {
            case "updateSchedule":
                var cron = document.getElementById("input_cron").value;
                if ("" == cron) {
                    document.getElementById("resultMsg").classList.add("resultMsg_failure");
                    document.getElementById("resultMsg").innerText = '<%=request.getAttribute("msg_requiredValue") %>';

                } else if (6 != cron.split(' ').length || !checkout_shcheduleCron(cron.split(' '))) {
                    document.getElementById("resultMsg").classList.add("resultMsg_failure");
                    document.getElementById("resultMsg").innerText = '[<%=request.getAttribute("execution_time") %>] <%=request.getAttribute("msg_formatError") %>';

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
                document.getElementById("resultMsg").innerText = "成功";
                init();
			},
			error: function (e) {
			}
		});
    }
</script>