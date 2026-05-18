<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>ZavaFraudDetector Login</title>
</head>
<body bgcolor="#f4f4f4">
<table width="760" align="center" cellpadding="8" cellspacing="0" border="1" bgcolor="#ffffff">
    <tr bgcolor="#4b1f5f">
        <td><font color="#ffffff"><b>ZavaFraudDetector - Fraud Analysis Dashboard</b></font></td>
    </tr>
    <tr bgcolor="#d8d0de">
        <td style="padding:3px 8px;font-size:11px;font-family:Verdana,Arial;"><a href="/" style="color:#4b1f5f;text-decoration:none;font-weight:bold;">&#9664; ZavaBank Portal</a></td>
    </tr>
    <tr>
        <td>
            <p>Authenticate using SessionTokens issued by ZavaAuthGateway.</p>
            <s:if test="hasActionErrors()">
                <font color="#cc0000"><b><s:actionerror/></b></font>
            </s:if>
            <s:form action="login" method="post">
                <table cellpadding="6" cellspacing="0" border="0">
                    <tr>
                        <td><b>Session Token</b></td>
                        <td><s:textfield name="sessionToken" size="56"/></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td><s:submit value="Sign In"/></td>
                    </tr>
                </table>
            </s:form>
            <p><small>Accepted token sources: .ZAVAAUTH cookie, sessionToken query value, or X-Session-Token header.</small></p>
        </td>
    </tr>
</table>
</body>
</html>
