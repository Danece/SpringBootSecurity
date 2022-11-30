<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
    <head>
        <title>User List</title>
    </head>
    <%@ include file="/WEB-INF/pages/common/header.jsp" %>
	<body style="padding: 20px; width: 50%; margin: 50px auto;">
        <div class="page_title" >
            <h1><%=request.getAttribute("title") %></h1>
            <button onclick="location.href='/welcome';" type="button" class="btn btn-outline-info" ><%=request.getAttribute("btn_return") %></button>
        </div>
		
        <table style="text-align: center; width: 100%;">
            <tr>
                <td>
                    <i>*</i> <%=request.getAttribute("user_name") %>
                </td>
                <td>
                    <input type="text" id="input_name">
                </td>
                <td>
                    <i>*</i> <%=request.getAttribute("authority") %>
                </td>
                <td>
                    <input type="text" id="input_authority">
                </td>
            </tr>
        </table>
        <br>
        <button type="button" class="btn btn-outline-primary" onclick="searchData();" ><%=request.getAttribute("btn_search") %></button>
        <label id="resultMsg" class="resultMsg"></label>
		<br><br>
        <hr>
        <table class="display" id="table" style="width: 100%;"></table>
	</body>
</html>

<script type="text/javascript">

    function searchData() {
        var dataset = [];

        if (DataTable.isDataTable('#table')) {
            var table = $('#table').DataTable();
            // clear datatable
            table.clear().draw();
            // destroy datatable
            table.destroy();
        }
        
        var searchName = (null == document.getElementById("input_name").value ? "" : document.getElementById("input_name").value);
        var authority = (null == document.getElementById("input_authority").value ? "" : document.getElementById("input_authority").value);

        // 取得排程DB資料
        $.ajax({
			type: "GET",
			contentType: "application/json",
			url: "/api/userInfo",
			dataType: 'json',
            data: { 
                name :　searchName,
                authority : authority,
            },
            async: false,
			cache: false,
			timeout: 600000,
			success: function (data) {
                // 沒資料
                if (!data.content.length || typeof data.content.length == "undefined") {

                } else {
                // 有資料
                    for (var i=0; i<data.content.length; i++) {
                        dataset.push([data.content[i].name, data.content[i].authority]);
                    }
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
                {title: '<%=request.getAttribute("user_name") %>'},
                {title: '<%=request.getAttribute("authority") %>'}
            ],
            language: {
                url : "/jsons/TableLanguage_TW.json"
            }
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