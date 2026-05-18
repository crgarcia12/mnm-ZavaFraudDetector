package com.zavabank.frauddetector;

import com.opensymphony.xwork2.ActionSupport;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class TransactionDetailAction extends ActionSupport implements SessionAware {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<String, Object> session;
    private Integer alertId;
    private FraudAlertRecord alert;
    private String username;

    @Override
    public String execute() {
        SessionUser user = session != null ? (SessionUser) session.get(SsoSessionService.SESSION_USER) : null;
        username = user == null ? "Unknown" : user.getUsername();
        if (alertId == null) {
            return "notfound";
        }

        try (java.sql.Connection connection = FraudConnectionFactory.openConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(
                 "SELECT TOP 1 fa.AlertID, fa.TransactionID, fa.AlertType, fa.Severity, fa.Status, " +
                     "fa.Description, fa.CreatedDate, a.AccountNumber, t.Amount " +
                     "FROM FraudAlerts fa " +
                     "LEFT JOIN Accounts a ON fa.AccountID = a.AccountID " +
                     "LEFT JOIN Transactions t ON fa.TransactionID = t.TransactionID " +
                     "WHERE fa.AlertID = ?")) {
            statement.setInt(1, alertId.intValue());
            try (java.sql.ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return "notfound";
                }
                FraudAlertRecord record = new FraudAlertRecord();
                record.setAlertId(resultSet.getInt(1));
                long transactionIdValue = resultSet.getLong(2);
                record.setTransactionId(resultSet.wasNull() ? null : Long.valueOf(transactionIdValue));
                record.setAlertType(resultSet.getString(3));
                record.setSeverity(resultSet.getString(4));
                record.setStatus(resultSet.getString(5));
                record.setDescription(resultSet.getString(6));
                java.util.Date createdDate = resultSet.getTimestamp(7);
                record.setCreatedDate(createdDate == null ? "" : DATE_FORMAT.format(createdDate));
                record.setAccountNumber(resultSet.getString(8));
                java.math.BigDecimal amount = resultSet.getBigDecimal(9);
                record.setAmount(amount == null ? "" : amount.toPlainString());
                alert = record;
            }
        } catch (java.sql.SQLException exception) {
            addActionError("Unable to load alert detail.");
            return "notfound";
        }
        return SUCCESS;
    }

    public Integer getAlertId() {
        return alertId;
    }

    public void setAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public FraudAlertRecord getAlert() {
        return alert;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
