package com.zavabank.frauddetector;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class AlertDecisionAction extends ActionSupport implements SessionAware {
    private Map<String, Object> session;
    private Integer alertId;
    private String decision;
    private String notes;

    @Override
    public String execute() {
        if (alertId == null || decision == null || decision.trim().isEmpty()) {
            return SUCCESS;
        }

        String status = "Approved".equalsIgnoreCase(decision) ? "Approved" : "Rejected";
        SessionUser user = session == null ? null : (SessionUser) session.get(SsoSessionService.SESSION_USER);
        String username = user == null ? "system" : user.getUsername();
        String resolution = (notes == null ? "" : notes.trim());
        if (!resolution.isEmpty()) {
            resolution = resolution + " | ";
        }
        resolution = resolution + "Decision by " + username;

        try (java.sql.Connection connection = FraudConnectionFactory.openConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(
                 "UPDATE FraudAlerts SET Status = ?, ResolutionNotes = ?, ResolvedDate = GETDATE(), ModifiedDate = GETDATE() WHERE AlertID = ?")) {
            statement.setString(1, status);
            statement.setString(2, resolution);
            statement.setInt(3, alertId.intValue());
            statement.executeUpdate();
        } catch (java.sql.SQLException exception) {
            addActionError("Unable to update alert decision.");
        }
        return SUCCESS;
    }

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
