<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
    <head>
        <title>User List</title>
    </head>
    <%@ include file="/WEB-INF/pages/common/header.jsp" %>
	<body style="padding: 20px; width: 50%; margin: 50px auto;">
        <div class="page_title" >
            <h1><%=request.getAttribute("USERLIST_TITLE") %></h1>
            <button onclick="location.href='/welcome';" type="button" class="btn btn-outline-info" ><%=request.getAttribute("BTN_RETURN") %></button>
        </div>
		
        <table style="text-align: center; width: 100%;">
            <tr>
                <td>
                    <%=request.getAttribute("USERLIST_ROW_USER_NAME") %>
                </td>
                <td>
                    <input type="text" id="input_name">
                </td>
                <td>
                    <%=request.getAttribute("USERLIST_ROW_AUTHORITY") %>
                </td>
                <td>
                    <input type="text" id="input_authority">
                </td>
            </tr>
        </table>
        <br>
        <div style="display: flex; justify-content: space-between;">
            <div>
                <button type="button" class="btn btn-outline-primary" onclick="searchData();" ><%=request.getAttribute("BTN_SEARCH") %></button>
            </div>
            <div>
                <button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#deleteModal" data-whatever="@getbootstrap" onclick="passValue();"><%=request.getAttribute("BTN_DELETE") %></button>
                <button type="button" class="btn btn-outline-warning" data-toggle="modal" data-target="#createUserModal" data-whatever="@getbootstrap"><%=request.getAttribute("BTN_CREATE") %></button>
            </div>
        </div>
        <br>
        <hr>
        <table class="display" id="table" style="width: 100%;"></table>
	</body>
    <!-- Modal -->
    <div class="modal fade" id="createUserModal" tabindex="-1" role="dialog" aria-labelledby="createUserModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header" style="background-color: #0d6efd; color: white;">
                    <h4 class="modal-title" id="createUserModalLabel"><%=request.getAttribute("MODAL_CREATE_USER") %></h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <i>*</i> <label for="modal-name" class="col-form-label"><%=request.getAttribute("ACCOUNT") %> :</label>
                            <input type="text" class="form-control" id="modal-name">
                        </div>
                        <div class="form-group">
                            <i>*</i> <label for="modal-password" class="col-form-label"><%=request.getAttribute("PASSWORD") %> :</label>
                            <input type="password" class="form-control" id="modal-password">
                        </div>
                        <div class="form-group">
                            <i>*</i> <label class="col-form-label"><%=request.getAttribute("ROLE") %> :</label>
                            <select id="modal-role" onchange="" class="form-select" style="height: 37px; padding-top: 7px; padding-bottom: 7px;">
                                <%
                                    ArrayList roleValues = (ArrayList) request.getAttribute("roleValueList");
                                    ArrayList roleNames = (ArrayList) request.getAttribute("roleNameList");
                                    for (int i=0; i<roleValues.size(); i++) {
                                %>
                                        <option value="<%=roleValues.get(i) %>" selected><%=roleNames.get(i) %></option>
                                <%  } %>
                            </select>
                        </div>
                        <div class="form-group">
                            <i>*</i> <label class="col-form-label"><%=request.getAttribute("AUTHORITY") %> :</label>
                            <div>
                                <%
                                    ArrayList authValues = (ArrayList) request.getAttribute("authValueList");
                                    ArrayList authNames = (ArrayList) request.getAttribute("authNameList");
                                    for (int i=0; i<roleValues.size(); i++) {
                                        boolean isAdmin = ("admin".equals(authValues.get(i)));
                                %>
                                    <div class="form-check form-check-inline">
                                        <input class="form-check-input" type="checkbox" id="auth_<%=authValues.get(i) %>" name="modal-authority" value="<%=authValues.get(i) %>"  
                                        <% if (isAdmin) { %>onchange="ckeckedAll(this);"<% } else { %>checked<% } %>>
                                        <label class="form-check-label" for="auth_<%=authValues.get(i) %>"><%=authNames.get(i) %></label>
                                    </div>
                                <%  } %>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer" style="display: flex; justify-content: space-between;">
                    <div>
                        <button type="button" class="btn btn-outline-danger" data-dismiss="modal" onclick="clearModalRowValue();"><%=request.getAttribute("BTN_CLOSE") %></button>
                        </div>
                    <div>
                        <label id="resultMsg" class="resultMsg"></label>
                    </div>
                    <div>
                        <button type="button" class="btn btn-outline-primary" onclick="createUser();"><%=request.getAttribute("BTN_CREATE") %></button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header" style="background-color: #0d6efd; color: white;">
                    <h4 class="modal-title" id="deleteModalLabel"><%=request.getAttribute("MODAL_DELETE_USER") %></h4>
                </div>
                <div class="modal-body">
                    <form>
                        <div class="form-group">
                            <label id="deleteUserList">
                        </div>
                    </form>
                </div>
                <div class="modal-footer" style="display: flex; justify-content: space-between;">
                    <button type="button" class="btn btn-outline-danger" data-dismiss="modal" onclick="clearModalRowValue();"><%=request.getAttribute("BTN_CANCEL") %></button>
                    <button type="button" class="btn btn-outline-primary" data-dismiss="modal" onclick="deleteData();"><%=request.getAttribute("BTN_SURE") %></button>
                </div>
            </div>
        </div>
    </div>
</html>

<script type="text/javascript">

    var lang = "";
    init();
    function init() {
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
    }

    // search DB data and show on view
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
                        dataset.push([
                            '<input class="form-check-input" type="checkbox" name="deleteCheckbox" value="' + data.content[i].id + ',' + data.content[i].name + '">', 
                            data.content[i].name, 
                            data.content[i].authority
                        ]);
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
                {title: ''},
                {title: '<%=request.getAttribute("USERLIST_ROW_USER_NAME") %>'},
                {title: '<%=request.getAttribute("USERLIST_ROW_AUTHORITY") %>'}
            ],
            columnDefs: [
                {
                    targets: 0,
                    className: 'dt-body-center'
                }
            ],
            language: {
                url : lang
            }
        });
    }

    // check or uncheck authority of all
    function ckeckedAll(obj) {
        var auth = document.getElementsByName("modal-authority");
        if (obj.checked) {
            for (var i=1;i<auth.length;i++){
                auth[i].checked = true;
                auth[i].disabled = true;
            }

        } else {
            for (var i=1;i<auth.length;i++){
                auth[i].checked = false;
                auth[i].disabled = false;
            }
        }
    }

    // do create user
    function createUser() {
        var acc = document.getElementById("modal-name").value;
        var pwd = document.getElementById("modal-password").value;
        var role = document.getElementById("modal-role").value;
        var auth = document.getElementsByName("modal-authority");

        document.getElementById("resultMsg").classList.remove("resultMsg_success", "resultMsg_failure");
        document.getElementById("resultMsg").innerText = '';

        if ("" == acc || "" == pwd || "" == role || 0==auth.length) {
            document.getElementById("resultMsg").classList.add("resultMsg_failure");
            document.getElementById("resultMsg").innerText = '<%=request.getAttribute("MSG_REQUIREDVALUE") %>';

        } else {
            var authAndRole = "";
            for (var i=0;i<auth.length;i++){
                if ( auth[i].checked ) {
                    authAndRole = authAndRole + auth[i].value + ",";
                }
            }
            authAndRole = authAndRole + "ROLE_" + role;

            var infos = {}
            infos["name"] = acc;
            infos["authority"] = authAndRole;
            infos["password"] = pwd;
            infos["operation"] = "create";

            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/api/userInfo",
                data: JSON.stringify(infos),
                dataType: 'json',
                cache: false,
                timeout: 600000,
                success: function (data) {
                    clearModalRowValue();
                    if (0 == data.errorCode) {
                        document.getElementById("resultMsg").classList.add("resultMsg_success");
                        document.getElementById("resultMsg").innerText = '<%=request.getAttribute("SUCCESS") %>';

                    } else {
                        document.getElementById("resultMsg").classList.add("resultMsg_failure");
                        document.getElementById("resultMsg").innerText = '<%=request.getAttribute("FAILED") %> : ' + data.errorMsg;
                    }
                },
                error: function (e) {
                }
            });
        }
    }

    // close create user modal, reset row value
    function clearModalRowValue() {
        document.getElementById("modal-name").value = '';
        document.getElementById("modal-password").value = '';
        document.getElementById("resultMsg").classList.remove("resultMsg_success", "resultMsg_failure");
        document.getElementById("resultMsg").innerText = '';
    }

    function deleteData() {
        var selected = document.getElementsByName("deleteCheckbox");
        var deleteUserList = document.getElementById("deleteUserList");
        var ids = [];

        
        for (var i=0;i<selected.length;i++){
            if (selected[i].checked) {
                var id = (selected[i].value).split(',')[0];
                ids[ids.length] = parseInt(id);
            }
        }

        console.log("SSS0", ids.length);
        if (0 < ids.length) {
            var infos = {}
            infos["id"] = ids;

            $.ajax({
                type: "DELETE",
                contentType: "application/json",
                url: "/api/userInfo",
                data: JSON.stringify(infos),
                dataType: 'json',
                cache: false,
                timeout: 600000,
                success: function (data) {
                    searchData();
                },
                error: function (e) {
                    console.log("error", e);
                }
            });
        }
    }

    function passValue() {
        var selected = document.getElementsByName("deleteCheckbox");
        var names = [];

        for (var i=0;i<selected.length;i++){
            if (selected[i].checked) {
                var name = (selected[i].value).split(',')[1];
                names[names.length] = name;
            }
        }
        $("#deleteUserList").text('<%=request.getAttribute("MODAL_DELETE_USER_MSG") %> : ' + names );
    }
</script>