<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>登录失败</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/jquery.mobile-1.4.5.min.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/vendor/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/vendor/jquery.mobile-1.4.5.min.js"></script>
<script>
$(document).on({
    pageshow: function() {
        if ( window.opener ) {
            window.opener.ThirdPartyLoginInCallback(false);
        } else {
            localStorage.setItem("LoginResult", false);
        }
    }
});
$(document).ready(function(){
    $('#btn-close').click(function() {
        window.open('', '_self', '');
        window.close();
    });
});
</script>
</head>
<body>
<div data-role="page" data-title="登录失败">
    <div data-role="header" data-theme="b"><h1>失败原因</h1></div>
    <div data-role="content">
        <p>抱歉,登录失败，请稍候再试!</p>
        <div id="fail-reason" style="color:red">${reason}</div>
        <button id="btn-close" class="ui-shadow ui-btn ui-btn-inline ui-corner-all ui-icon-delete ui-btn-icon-right">关闭当前窗口</button>
    </div>
    <div data-role="footer" data-position="fixed" data-tap-toggle="false" data-transition="none" data-theme="b" data-mini="true">
        <h1>Copyright @ 2015-2025</h1>
    </div>
</div>
</body>
</html>