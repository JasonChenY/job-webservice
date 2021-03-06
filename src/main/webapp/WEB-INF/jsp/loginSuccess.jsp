<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>登录成功</title>
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
        var user = {
            "username": "${username}",
            "identity_type": "${identity_type}",
            "role": "${role}"
        };
        if ( window.opener ) {
            $('#auto-close-hint').hide();
            $('#btn-close').show();
            //window.opener.ThirdPartyLoginInCallback(user);
            window.opener.postMessage(user, '*');
        } else {
            localStorage.setItem("LoginResult", JSON.stringify(user));
            $('#auto-close-hint').show();
            $('#btn-close').hide();
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
<div data-role="page" data-title="登录成功">
    <div data-role="header" data-theme="b"><h1>登录信息</h1></div>
    <div data-role="content">
        <p>帐号: ${username}</p>
        <h3>温馨提示</h3>
        <p>已经可以使用跳哪儿的所有功能了,你也可以到用户管理中心创建本地帐号，绑定多个第三方帐号.</p>
        <div id="auto-close-hint" style="color:red">请稍候，当前窗口将会自动关闭！</div>
        <button id="btn-close" class="ui-shadow ui-btn ui-btn-inline ui-corner-all ui-icon-delete ui-btn-icon-right">关闭当前窗口</button>
    </div>
    <div data-role="footer" data-position="fixed" data-tap-toggle="false" data-transition="none" data-theme="b" data-mini="true">
        <h1>Copyright @ 2015-2025</h1>
    </div>
</div>
</body>
</html>
