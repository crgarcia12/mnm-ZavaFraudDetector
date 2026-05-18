package com.zavabank.frauddetector;

import com.opensymphony.xwork2.ActionSupport;

public class RuleDeleteAction extends ActionSupport {
    private Integer ruleId;

    @Override
    public String execute() {
        if (ruleId == null) {
            return SUCCESS;
        }

        try (java.sql.Connection connection = FraudConnectionFactory.openConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement("DELETE FROM FraudRules WHERE RuleID = ?")) {
            statement.setInt(1, ruleId.intValue());
            statement.executeUpdate();
        } catch (java.sql.SQLException exception) {
            addActionError("Unable to delete selected rule.");
        }
        return SUCCESS;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }
}
