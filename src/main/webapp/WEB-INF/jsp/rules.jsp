<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>ZavaFraudDetector - Rule Management</title>
</head>
<body bgcolor="#f4f4f4">
<table width="1060" align="center" cellpadding="8" cellspacing="0" border="1" bgcolor="#ffffff">
    <tr bgcolor="#4b1f5f">
        <td colspan="9">
            <font color="#ffffff"><b>ZavaFraudDetector - Fraud Rule Management</b></font>
            <span style="float:right;color:#ffffff;">Analyst: <s:property value="username"/></span>
        </td>
    </tr>
    <tr bgcolor="#ececec">
        <td colspan="9">
            <a href="/" style="font-weight:bold;">&#9664; ZavaBank Portal</a> |
            <a href="<s:url action='flaggedQueue'/>">Flagged Queue</a> |
            <a href="<s:url action='ruleEdit'/>">Create Rule</a>
        </td>
    </tr>
    <tr bgcolor="#d7d7d7">
        <th align="left">Rule ID</th>
        <th align="left">Name</th>
        <th align="left">Expression</th>
        <th align="left">Severity</th>
        <th align="right">Threshold</th>
        <th align="right">Window (min)</th>
        <th align="center">Active</th>
        <th align="left">Edit</th>
        <th align="left">Delete</th>
    </tr>
    <s:iterator value="rules">
        <s:url var="editUrl" action="ruleEdit">
            <s:param name="ruleId" value="%{ruleId}"/>
        </s:url>
        <s:url var="deleteUrl" action="ruleDelete">
            <s:param name="ruleId" value="%{ruleId}"/>
        </s:url>
        <tr>
            <td><s:property value="ruleId"/></td>
            <td><s:property value="ruleName"/></td>
            <td><s:property value="ruleExpression"/></td>
            <td><s:property value="severity"/></td>
            <td align="right"><s:property value="thresholdAmount"/></td>
            <td align="right"><s:property value="timeWindowMinutes"/></td>
            <td align="center"><s:if test="active">Y</s:if><s:else>N</s:else></td>
            <td><a href="${editUrl}">Edit</a></td>
            <td><a href="${deleteUrl}" onclick="return confirm('Delete this rule?');">Delete</a></td>
        </tr>
    </s:iterator>
    <s:if test="rules.isEmpty()">
        <tr>
            <td colspan="9"><i>No rules configured.</i></td>
        </tr>
    </s:if>
</table>
</body>
</html>
