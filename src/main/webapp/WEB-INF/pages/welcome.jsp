<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<link rel="stylesheet" href="https://unpkg.com/purecss@2.0.6/build/pure-min.css">
	<link rel="stylesheet" type="text/css" href="/static/global.css" />
	<meta charset="UTF-8">
	<title>Welcome page</title>
</head>
	<body style="padding: 20px; width: 50%; margin: 50px auto;">
		<h1>身份資訊</h1>
		<sec:authorize access="hasAnyRole('employee','manager')">
			<div>使用者 您好!</div>
		</sec:authorize>
		<sec:authorize access="hasRole('manager')">
			<div>登入身份 : [ 管理員 ]</div>
		</sec:authorize>
		<sec:authorize access="hasRole('employee')">
			<div>登入身份 : [ 員工 ]</div>
		</sec:authorize>
		<ul>
			<!-- 顯示使用者資訊 -->
			<!-- <sec:authentication  property = "principal" />  -->
			<li><label>登入帳號（name）: </label><span class="span_content"><sec:authentication property = "name" /></span></li>
			<li>登入帳號（principal.username）: <span class="span_content"><sec:authentication  property = "principal.username" /></span></li>
			<li>憑證（credentials）: <span class="span_content"><sec:authentication  property = "credentials" /></span></li>
			<li>權限與角色（authorities）: <span class="span_content"><sec:authentication  property = "authorities" /></span></li>
			<li>客戶端地址（details.remoteAddress）: <span class="span_content"><sec:authentication  property = "details.remoteAddress" /></span></li>
			<li>Session Id（details.sessionId）: <span class="span_content"><sec:authentication  property = "details.sessionId" /></span></li>
			<li>加密數據 (JWT AccessToken) : <span style="word-break: break-all"><label id="jwt" style="font-weight: 700"></label></span></li>
		</ul>
		<br>
		<sec:authorize access="hasAuthority('admin')">
			<button onclick="location.href='/time';">管理</button>
		</sec:authorize>
		<sec:authorize access="hasAuthority('admin')">
			<button onclick="location.href='/scheduleList';">排程管理</button>
		</sec:authorize>
		<sec:authorize access="hasAnyAuthority('admin', 'normal')">
			<button onclick="location.href='/logout';" >登出</button>
		</sec:authorize>
		<button onclick="getAccesstoken()" >產生加密數據</button>
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