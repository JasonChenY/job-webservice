<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<title>Login Failed</title>
<script type="text/javascript">
    function load()
    {
        if ( window.opener ) {
            window.opener.ThirdPartyLoginInCallback(false);
        }
    }
</script>
</head>
<body onload="load()">
<div>
    <span>Login failed!</span>
</div>
</body>
</html>