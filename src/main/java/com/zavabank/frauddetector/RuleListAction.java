package com.zavabank.frauddetector;

import com.opensymphony.xwork2.ActionSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class RuleListAction extends ActionSupport implements SessionAware {
    private List<FraudRuleRecord> rules = new ArrayList<FraudRuleRecord>();
    private String username;
    private Map<String, Object> session;

    @Override
    public String execute() {
        SessionUser user = session == null ? null : (SessionUser) session.get(SsoSessionService.SESSION_USER);
        username = user == null ? "Unknown" : user.getUsername();

        try (java.sql.Connection connection = FraudConnectionFactory.openConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(
                 "SELECT RuleID, RuleName, RuleDescription, RuleExpression, Severity, ThresholdAmount, TimeWindowMinutes, IsActive " +
                     "FROM FraudRules ORDER BY RuleID");
             java.sql.ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                FraudRuleRecord rule = new FraudRuleRecord();
                rule.setRuleId(resultSet.getInt(1));
                rule.setRuleName(resultSet.getString(2));
                rule.setRuleDescription(resultSet.getString(3));
                rule.setRuleExpression(resultSet.getString(4));
                rule.setSeverity(resultSet.getString(5));
                BigDecimal threshold = resultSet.getBigDecimal(6);
                rule.setThresholdAmount(threshold == null ? "" : threshold.toPlainString());
                int window = resultSet.getInt(7);
                rule.setTimeWindowMinutes(resultSet.wasNull() ? "" : String.valueOf(window));
                rule.setActive(resultSet.getBoolean(8));
                rules.add(rule);
            }
        } catch (java.sql.SQLException exception) {
            addActionError("Unable to load fraud rules.");
        }
        return SUCCESS;
    }

    public List<FraudRuleRecord> getRules() {
        return rules;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
