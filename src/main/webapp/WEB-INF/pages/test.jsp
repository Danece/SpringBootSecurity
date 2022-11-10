
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My springboot test jsp</title>
    </head>
    <body style="padding: 20px; width: 50%; margin: 50px auto;">
        進入操作時間戳 - ${now}
        <hr>
        <%
            String noticeMsg = "( 施工中 . . . )";
        %>
        <%= noticeMsg %>
        <hr>
        <button sec:authorize="hasAnyAuthority('admin')" onclick="location.href='/welcome';" >返回</button>
        <button sec:authorize="hasAnyAuthority('admin', 'normal')" onclick="location.href='/logout';" >登出</button>
    </body>
</html>