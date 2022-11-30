<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
    <head>
        <title>Login form</title>
    </head>
    <%@ include file="/WEB-INF/pages/common/header.jsp" %>
    <body style="padding: 20px">
        <form class="pure-form" method="post" action="/api/login" style="text-align: center; margin: 150px;">
            <fieldset>
                <legend style="font-weight: 700;">${title}</legend>
                <div class="container" style="width: 30%;">
                    <div class="input-group input-group-sm mb-3">
                        <span class="input-group-text" id="inputGroup-sizing-sm">帳號</span>
                        <input type="text" class="form-control" placeholder=" <%=request.getAttribute("placeholder_account") %>" name="username" />
                    </div>
                    <div class="input-group input-group-sm mb-3">
                        <span class="input-group-text" id="inputGroup-sizing-sm">密碼</span>
                        <input type="password" class="form-control" placeholder=" <%=request.getAttribute("placeholder_password") %>" name="password" />
                    </div>
                    <div style="margin: -10px 0 5px 0;">
                        <input type="checkbox" name="remember-me" value="true" /> <%=request.getAttribute("remember_me") %>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-outline-primary">${btn_login}</button>
                    </div>
                    <div class="row">
                        <label id="authResult" style="color: red;"></label>
                    </div>
                </div>
            </fieldset>
        </form>
    </body>
</html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" type="text/javascript"></script>
<script>
    init();
    function init() {
        // ${status} is set from AuthenticationFailureHandlerImpl.java
        var authResult = document.getElementById("authResult");
        switch('${status}') {
            case 'loginError':
                authResult.innerHTML = "登入失敗";
                break;
            case 'entryPintError':
                authResult.innerHTML = "請先登入才可進行操作";
                break;
            default:
        }
    }
</script>