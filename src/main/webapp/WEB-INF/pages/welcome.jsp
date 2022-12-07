<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
	<head>
		<title>Welcome page</title>
	</head>
	<%@ include file="/WEB-INF/pages/common/header.jsp" %>
	<body style="padding: 20px; width: 50%; margin: 50px auto;">
		<div class="page_title" >
			<h1><%=request.getAttribute("WELCOME_TITLE") %></h1>
			<sec:authorize access="hasAnyAuthority('admin', 'normal')">
				<button onclick="location.href='/logout';" type="button" class="btn btn-outline-info" ><%=request.getAttribute("BTN_LOGOUT") %></button>
			</sec:authorize>
		</div>
		<sec:authorize access="hasAnyRole('employee','manager')">
			<div><%=request.getAttribute("MSG_HELLO") %></div>
		</sec:authorize>
		<sec:authorize access="hasRole('manager')">
			<div><%=request.getAttribute("MSG_LOGINIDENTITY") %> : [ <%=request.getAttribute("ROLE_MANAGER") %> ]</div>
		</sec:authorize>
		<sec:authorize access="hasRole('employee')">
			<div><%=request.getAttribute("MSG_LOGINIDENTITY") %> : [ <%=request.getAttribute("ROLE_EMPLOYEE") %> ]</div>
		</sec:authorize>
			<ul>
				<!-- 顯示使用者資訊 -->
				<!-- <sec:authentication  property = "principal" />  -->
				<li><%=request.getAttribute("WELCOME_ROW_ACCOUNT") %> : <span class="span_content"><sec:authentication property = "name" /></span></li>
				<li><%=request.getAttribute("WELCOME_ROW_USERNAME") %> : <span class="span_content"><sec:authentication  property = "principal.username" /></span></li>
				<li><%=request.getAttribute("WELCOME_ROW_AUTHRITIES") %> : <span class="span_content"><sec:authentication  property = "authorities" /></span></li>
				<li><%=request.getAttribute("WELCOME_ROW_REMOTEADDRESS") %> : <span class="span_content"><sec:authentication  property = "details.remoteAddress" /></span></li>
				<li><%=request.getAttribute("WELCOME_ROW_SESSIONID") %> : <span class="span_content"><sec:authentication  property = "details.sessionId" /></span></li>
				<li><%=request.getAttribute("WELCOME_ROW_JWTACCESSTOKEN") %> : <span style="word-break: break-all"><label id="jwt" style="font-weight: 700"></label></span></li>
			</ul>
		<br>
		<sec:authorize access="hasAuthority('admin')">
			<button onclick="location.href='/time';" type="button" class="btn btn-outline-primary" ><%=request.getAttribute("BTN_TEST") %></button>
		</sec:authorize>
		<sec:authorize access="hasAuthority('admin')">
			<button onclick="location.href='/scheduleList';" type="button" class="btn btn-outline-primary" ><%=request.getAttribute("BTN_SCHEDULE_MANAGER") %></button>
		</sec:authorize>
		<sec:authorize access="hasAuthority('admin')">
			<button onclick="location.href='/userList';" type="button" class="btn btn-outline-primary" ><%=request.getAttribute("BTN_USER_MANAGER") %></button>
		</sec:authorize>
		<button onclick="getAccesstoken()" type="button" class="btn btn-outline-warning" ><%=request.getAttribute("BTN_EXPORT_JWT") %></button>
	</body>
</html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" type="text/javascript"></script>
<script>
	function getAccesstoken(userName) {
		var search = {}
		var jwt = document.getElementById("jwt");
		search["userName"] = '<sec:authentication property = "name" />';
		$.ajax({
			type: "POST",
			contentType: "application/json",
			url: "/api/jwtResult",
			data: JSON.stringify(search),
			dataType: 'json',
			cache: false,
			timeout: 600000,
			success: function (data) {
				jwt.innerHTML = "";
				jwt.innerHTML += data.Token;
			},
			error: function (e) {
			}
		});
	}
</script>