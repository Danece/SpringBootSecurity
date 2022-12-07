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
                <legend style="font-weight: 700;">${LOGIN_TITLE}</legend>
                <div class="container" style="width: 30%;">
                    <div class="input-group input-group-sm mb-3">
                        <span class="input-group-text login_span" id="inputGroup-sizing-sm">${LANGUAGE}</span>
                        <select id="langSelect" onchange="changeLanguage(this);" class="form-select" style="height: 37px; padding-top: 7px; padding-bottom: 7px;">
                            <option value="zh_TW" selected><%=request.getAttribute("LANG_TW") %></option>
                            <option value="en_US"><%=request.getAttribute("LANG_EN") %></option>
                        </select>
                    </div>
                    <div class="input-group input-group-sm mb-3">
                        <span class="input-group-text  login_span" id="inputGroup-sizing-sm">${ACCOUNT}</span>
                        <input type="text" class="form-control" placeholder=" <%=request.getAttribute("PLACEHOLDER_ACCOUNT") %>" name="username" />
                    </div>
                    <div class="input-group input-group-sm mb-3">
                        <span class="input-group-text login_span" id="inputGroup-sizing-sm">${PASSWORD}</span>
                        <input type="password" class="form-control" placeholder=" <%=request.getAttribute("PLACEHOLDER_PASSWORD") %>" name="password" />
                    </div>
                    <div style="margin: -10px 0 5px 0;">
                        <input type="checkbox" name="remember-me" value="true" /> <%=request.getAttribute("REMEMBER_ME") %>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-outline-primary">${BTN_LOGIN}</button>
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
                authResult.innerHTML = '<%=request.getAttribute("MSG_LOGIN_FAILED") %>';
                break;
            case 'entryPintError':
                authResult.innerHTML = '<%=request.getAttribute("MSG_NEDD_LOGIN") %>';
                break;
            default:
        }

        $.ajax({
			type: "GET",
			contentType: "application/json",
			url: "/lang",
			dataType: 'json',
            async: false,
			cache: false,
			timeout: 600000,
            success: function (data) {
                document.getElementById("langSelect").value = data.lang;
			},
			error: function (e) {
                console.log(e);
			}
		});
    }

    function changeLanguage(obj) {
        var values = {}
		values["lang"] = obj.value;
        $.ajax({
			type: "POST",
			contentType: "application/json",
			url: "/lang",
            async: false,
			data: JSON.stringify(values),
			dataType: 'json',
			cache: false,
			timeout: 600000,
			error: function (e) {
                console.log(e);
			}
		});
        
        location.reload();
    }
</script>