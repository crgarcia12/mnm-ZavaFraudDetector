package com.zavabank.frauddetector;

public class FraudRuleRecord {
    private int ruleId;
    private String ruleName;
    private String ruleDescription;
    private String ruleExpression;
    private String severity;
    private String thresholdAmount;
    private String timeWindowMinutes;
    private boolean active;

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
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
