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
			<h1><%=request.getAttribute("title") %></h1>
			<sec:authorize access="hasAnyAuthority('admin', 'normal')">
				<button onclick="location.href='/logout';" type="button" class="btn btn-outline-info" ><%=request.getAttribute("btn_logout") %></button>
			</sec:authorize>
		</div>
		<sec:authorize access="hasAnyRole('employee','manager')">
			<div><%=request.getAttribute("msg_hello") %></div>
		</sec:authorize>
		<sec:authorize access="hasRole('manager')">
			<div><%=request.getAttribute("msg_loginIdentity") %> : [ <%=request.getAttribute("role_manager") %> ]</div>
		</sec:authorize>
		<sec:authorize access="hasRole('employee')">
			<div><%=request.getAttribute("msg_loginIdentity") %> : [ <%=request.getAttribute("role_employee") %> ]</div>
		</sec:authorize>
			<ul>
				<!-- 顯示使用者資訊 -->
				<!-- <sec:authentication  property = "principal" />  -->
				<li><%=request.getAttribute("row_account") %> : <span class="span_content"><sec:authentication property = "name" /></span></li>
				<li><%=request.getAttribute("row_userName") %> : <span class="span_content"><sec:authentication  property = "principal.username" /></span></li>
				<li><%=request.getAttribute("row_credwntials") %> : <span class="span_content"><sec:authentication  property = "credentials" /></span></li>
				<li><%=request.getAttribute("row_authorities") %> : <span class="span_content"><sec:authentication  property = "authorities" /></span></li>
				<li><%=request.getAttribute("row_remoteAddress") %> : <span class="span_content"><sec:authentication  property = "details.remoteAddress" /></span></li>
				<li><%=request.getAttribute("row_sessionId") %> : <span class="span_content"><sec:authentication  property = "details.sessionId" /></span></li>
				<li><%=request.getAttribute("row_jwtAccessToken") %> : <span style="word-break: break-all"><label id="jwt" style="font-weight: 700"></label></span></li>
			</ul>
		<br>
		<sec:authorize access="hasAuthority('admin')">
			<button onclick="location.href='/time';" type="button" class="btn btn-outline-primary" ><%=request.getAttribute("test") %></button>
		</sec:authorize>
		<sec:authorize access="hasAuthority('admin')">
			<button onclick="location.href='/scheduleList';" type="button" class="btn btn-outline-primary" ><%=request.getAttribute("schedule_manager") %></button>
		</sec:authorize>
		<sec:authorize access="hasAuthority('admin')">
			<button onclick="location.href='/userList';" type="button" class="btn btn-outline-primary" ><%=request.getAttribute("user_manager") %></button>
		</sec:authorize>
		<button onclick="getAccesstoken()" type="button" class="btn btn-outline-warning" ><%=request.getAttribute("export_jwt") %></button>
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