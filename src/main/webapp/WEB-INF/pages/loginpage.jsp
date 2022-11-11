<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
	  xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
    <head>
        <link rel="stylesheet" href="https://unpkg.com/purecss@2.0.6/build/pure-min.css">
        <meta charset="UTF-8">
        <title>Login form</title>
    </head>
    <body style="padding: 20px">
        <form class="pure-form" method="post" action="/api/login" style="text-align: center; margin: 150px;">
            <fieldset>
                <legend style="font-weight: 700;">登入頁面</legend>
                <input type="text" placeholder=" 請輸入帳號" name="username" />
                <p />
                <input type="password" placeholder=" 請輸入密碼" name="password" />
                <p />
                <!-- <input type="text" th:value="${_csrf.token}" name="_csrf" th:if="${_csrf}" />
                <p /> -->
                <!-- <input type="checkbox" name="remember-me" value="true" /> remember me
                <p /> -->
                <label id="authResult" style="color: red;"></label>
                <p />
                <button type="submit" class="pure-button pure-button-primary">登入</button>
            </fieldset>
        </form>
    </body>
</html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" type="text/javascript"></script>
<script>
    init();
    function init() {
        // ${status} is set from AuthenticationFailureHandlerImpl.java
        console.log("TEST", '${status}');
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