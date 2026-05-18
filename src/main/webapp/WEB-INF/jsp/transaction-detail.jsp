<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>ZavaFraudDetector - Transaction Detail</title>
</head>
<body bgcolor="#f4f4f4">
<table width="980" align="center" cellpadding="8" cellspacing="0" border="1" bgcolor="#ffffff">
    <tr bgcolor="#4b1f5f">
        <td colspan="2">
            <font color="#ffffff"><b>ZavaFraudDetector - Transaction Alert Detail</b></font>
            <span style="float:right;color:#ffffff;">Analyst: <s:property value="username"/></span>
        </td>
    </tr>
    <tr bgcolor="#ececec">
        <td colspan="2">
            <a href="/" style="font-weight:bold;">&#9664; ZavaBank Portal</a> |
            <a href="<s:url action='flaggedQueue'/>">Back to Queue</a> |
            <a href="<s:url action='rules'/>">Rule Management</a>
        </td>
    </tr>
    <tr>
        <td width="45%" valign="top">
            <table width="100%" cellpadding="6" cellspacing="0" border="1">
                <tr><td><b>Alert ID</b></td><td><s:property value="alert.alertId"/></td></tr>
                <tr><td><b>Transaction ID</b></td><td><s:property value="alert.transactionId"/></td></tr>
                <tr><td><b>Alert Type</b></td><td><s:property value="alert.alertType"/></td></tr>
                <tr><td><b>Severity</b></td><td><s:property value="alert.severity"/></td></tr>
                <tr><td><b>Status</b></td><td><s:property value="alert.status"/></td></tr>
                <tr><td><b>Account</b></td><td><s:property value="alert.accountNumber"/></td></tr>
                <tr><td><b>Amount</b></td><td>$<s:property value="alert.amount"/></td></tr>
                <tr><td><b>Created</b></td><td><s:property value="alert.createdDate"/></td></tr>
                <tr><td><b>Description</b></td><td><s:property value="alert.description"/></td></tr>
            </table>
        </td>
        <td width="55%" valign="top">
            <h3>Approve / Reject</h3>
            <s:form action="alertDecision" method="post">
                <s:hidden name="alertId" value="%{alert.alertId}"/>
                <table cellpadding="6" cellspacing="0" border="0">
                    <tr>
                        <td><b>Decision</b></td>
                        <td>
                            <s:select name="decision" list="#{'Approved':'Approve','Rejected':'Reject'}"/>
                        </td>
                    </tr>
                    <tr>
                        <td><b>Notes</b></td>
                        <td><s:textarea name="notes" rows="6" cols="50"/></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td><s:submit value="Submit Decision"/></td>
                    </tr>
                </table>
            </s:form>
        </td>
    </tr>
</table>
</body>
</html>
