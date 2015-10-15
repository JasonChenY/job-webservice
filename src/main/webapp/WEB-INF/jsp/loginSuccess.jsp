<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
<title>Login Successfully</title>
<script type="text/javascript">
    function load()
    {
        if ( window.opener ) {
            window.opener.ThirdPartyLoginInCallback();
        }
    }
</script>
</head>
<body onload="load()">
<div>
    <span>Login successfully with following info:</span>
	<h3>username: "${username}"</h3>
	<h3>email: "${email}"</h3>
	<h3>phone: "${phone}"</h3>
</div>
</body>
</html>