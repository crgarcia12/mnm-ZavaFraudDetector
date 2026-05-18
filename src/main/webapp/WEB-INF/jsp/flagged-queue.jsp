<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>ZavaFraudDetector - Flagged Transactions Queue</title>
</head>
<body bgcolor="#f4f4f4">
<table width="1060" align="center" cellpadding="8" cellspacing="0" border="1" bgcolor="#ffffff">
    <tr bgcolor="#4b1f5f">
        <td colspan="8">
            <font color="#ffffff"><b>ZavaFraudDetector - AlertService Flagged Queue</b></font>
            <span style="float:right;color:#ffffff;">Analyst: <s:property value="username"/></span>
        </td>
    </tr>
    <tr bgcolor="#ececec">
        <td colspan="8">
            <a href="/" style="font-weight:bold;">&#9664; ZavaBank Portal</a> |
            <a href="<s:url action='flaggedQueue'/>">Flagged Queue</a> |
            <a href="<s:url action='rules'/>">Rule Management</a>
        </td>
    </tr>
    <tr bgcolor="#d7d7d7">
        <th align="left">Alert ID</th>
        <th align="left">Transaction ID</th>
        <th align="left">Account</th>
        <th align="right">Amount</th>
        <th align="left">Type</th>
        <th align="left">Severity</th>
        <th align="left">Status</th>
        <th align="left">Action</th>
    </tr>
    <s:iterator value="alerts">
        <s:url var="detailUrl" action="transactionDetail">
            <s:param name="alertId" value="%{alertId}"/>
        </s:url>
        <tr>
            <td><s:property value="alertId"/></td>
            <td><s:property value="transactionId"/></td>
            <td><s:property value="accountNumber"/></td>
            <td align="right">$<s:property value="amount"/></td>
            <td><s:property value="alertType"/></td>
            <td><s:property value="severity"/></td>
            <td><s:property value="status"/></td>
            <td><a href="${detailUrl}">Review</a></td>
        </tr>
    </s:iterator>
    <s:if test="alerts.isEmpty()">
        <tr>
            <td colspan="8"><i>No pending alerts in queue.</i></td>
        </tr>
    </s:if>
</table>
</body>
</html>
