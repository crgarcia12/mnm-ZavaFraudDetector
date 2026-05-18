package com.zavabank.frauddetector;

import com.opensymphony.xwork2.ActionSupport;
import java.math.BigDecimal;

public class RuleEditAction extends ActionSupport {
    private Integer ruleId;
    private String ruleName;
    private String ruleDescription;
    private String ruleExpression;
    private String severity;
    private String thresholdAmount;
    private String timeWindowMinutes;
    private boolean active = true;

    @Override
    public String execute() {
        if (ruleId == null) {
            return INPUT;
        }

        try (java.sql.Connection connection = FraudConnectionFactory.openConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(
                 "SELECT RuleName, RuleDescription, RuleExpression, Severity, ThresholdAmount, TimeWindowMinutes, IsActive " +
                     "FROM FraudRules WHERE RuleID = ?")) {
            statement.setInt(1, ruleId.intValue());
            try (java.sql.ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ruleName = resultSet.getString(1);
                    ruleDescription = resultSet.getString(2);
                    ruleExpression = resultSet.getString(3);
                    severity = resultSet.getString(4);
                    BigDecimal threshold = resultSet.getBigDecimal(5);
                    thresholdAmount = threshold == null ? "" : threshold.toPlainString();
                    int timeWindow = resultSet.getInt(6);
                    timeWindowMinutes = resultSet.wasNull() ? "" : String.valueOf(timeWindow);
                    active = resultSet.getBoolean(7);
                }
            }
        } catch (java.sql.SQLException exception) {
            addActionError("Unable to load selected rule.");
        }
        return INPUT;
    }

    public String save() {
        if (ruleName == null || ruleName.trim().isEmpty()) {
            addActionError("Rule name is required.");
            return INPUT;
        }
        if (severity == null || severity.trim().isEmpty()) {
            addActionError("Severity is required.");
            return INPUT;
        }

        BigDecimal threshold = null;
        Integer timeWindow = null;
        try {
            if (thresholdAmount != null && !thresholdAmount.trim().isEmpty()) {
                threshold = new BigDecimal(thresholdAmount.trim());
            }
            if (timeWindowMinutes != null && !timeWindowMinutes.trim().isEmpty()) {
                timeWindow = Integer.valueOf(Integer.parseInt(timeWindowMinutes.trim()));
            }
        } catch (NumberFormatException exception) {
            addActionError("Threshold amount or time window is invalid.");
            return INPUT;
        }

        try (java.sql.Connection connection = FraudConnectionFactory.openConnection()) {
            if (ruleId == null) {
                try (java.sql.PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO FraudRules (RuleName, RuleDescription, RuleExpression, Severity, IsActive, ThresholdAmount, TimeWindowMinutes, CreatedDate, ModifiedDate) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())")) {
                    insert.setString(1, trim(ruleName));
                    insert.setString(2, trim(ruleDescription));
                    insert.setString(3, trim(ruleExpression));
                    insert.setString(4, trim(severity));
                    insert.setBoolean(5, active);
                    if (threshold == null) {
                        insert.setNull(6, java.sql.Types.DECIMAL);
                    } else {
                        insert.setBigDecimal(6, threshold);
                    }
                    if (timeWindow == null) {
                        insert.setNull(7, java.sql.Types.INTEGER);
                    } else {
                        insert.setInt(7, timeWindow.intValue());
                    }
                    insert.executeUpdate();
                }
            } else {
                try (java.sql.PreparedStatement update = connection.prepareStatement(
                    "UPDATE FraudRules SET RuleName = ?, RuleDescription = ?, RuleExpression = ?, Severity = ?, IsActive = ?, " +
                        "ThresholdAmount = ?, TimeWindowMinutes = ?, ModifiedDate = GETDATE() WHERE RuleID = ?")) {
                    update.setString(1, trim(ruleName));
                    update.setString(2, trim(ruleDescription));
                    update.setString(3, trim(ruleExpression));
                    update.setString(4, trim(severity));
                    update.setBoolean(5, active);
                    if (threshold == null) {
                        update.setNull(6, java.sql.Types.DECIMAL);
                    } else {
                        update.setBigDecimal(6, threshold);
                    }
                    if (timeWindow == null) {
                        update.setNull(7, java.sql.Types.INTEGER);
                    } else {
                        update.setInt(7, timeWindow.intValue());
                    }
                    update.setInt(8, ruleId.intValue());
                    update.executeUpdate();
                }
            }
        } catch (java.sql.SQLException exception) {
            addActionError("Unable to save fraud rule.");
            return INPUT;
        }
        return SUCCESS;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public String getRuleExpression() {
        return ruleExpression;
    }

    public void setRuleExpression(String ruleExpression) {
        this.ruleExpression = ruleExpression;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getThresholdAmount() {
        return thresholdAmount;
    }

    public void setThresholdAmount(String thresholdAmount) {
        this.thresholdAmount = thresholdAmount;
    }

    public String getTimeWindowMinutes() {
        return timeWindowMinutes;
    }

    public void setTimeWindowMinutes(String timeWindowMinutes) {
        this.timeWindowMinutes = timeWindowMinutes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
