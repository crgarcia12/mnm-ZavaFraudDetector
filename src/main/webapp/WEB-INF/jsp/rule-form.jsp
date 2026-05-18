<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>ZavaFraudDetector - Rule Editor</title>
</head>
<body bgcolor="#f4f4f4">
<table width="900" align="center" cellpadding="8" cellspacing="0" border="1" bgcolor="#ffffff">
    <tr bgcolor="#4b1f5f">
        <td><font color="#ffffff"><b>ZavaFraudDetector - Rule Editor</b></font></td>
    </tr>
    <tr bgcolor="#ececec">
        <td>
            <a href="/" style="font-weight:bold;">&#9664; ZavaBank Portal</a> |
            <a href="<s:url action='rules'/>">Back to Rules</a> |
            <a href="<s:url action='flaggedQueue'/>">Flagged Queue</a>
        </td>
    </tr>
    <tr>
        <td>
            <s:if test="hasActionErrors()">
                <font color="#cc0000"><b><s:actionerror/></b></font>
            </s:if>
            <s:form action="ruleSave" method="post">
                <s:hidden name="ruleId"/>
                <table cellpadding="6" cellspacing="0" border="0">
                    <tr>
                        <td><b>Rule Name</b></td>
                        <td><s:textfield name="ruleName" size="60"/></td>
                    </tr>
                    <tr>
                        <td><b>Description</b></td>
                        <td><s:textarea name="ruleDescription" rows="3" cols="62"/></td>
                    </tr>
                    <tr>
                        <td><b>Expression</b></td>
                        <td><s:textfield name="ruleExpression" size="60"/></td>
                    </tr>
                    <tr>
                        <td><b>Severity</b></td>
                        <td>
                            <s:select name="severity" list="#{'Low':'Low','Medium':'Medium','High':'High','Critical':'Critical'}"/>
                        </td>
                    </tr>
                    <tr>
                        <td><b>Threshold Amount</b></td>
                        <td><s:textfield name="thresholdAmount" size="20"/></td>
                    </tr>
                    <tr>
                        <td><b>Time Window (Minutes)</b></td>
                        <td><s:textfield name="timeWindowMinutes" size="20"/></td>
                    </tr>
                    <tr>
                        <td><b>Active</b></td>
                        <td><s:checkbox name="active"/></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td><s:submit value="Save Rule"/></td>
                    </tr>
                </table>
            </s:form>
        </td>
    </tr>
</table>
</body>
</html>
