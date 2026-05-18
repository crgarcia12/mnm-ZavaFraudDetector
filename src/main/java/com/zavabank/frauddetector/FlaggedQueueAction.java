package com.zavabank.frauddetector;

import com.opensymphony.xwork2.ActionSupport;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class FlaggedQueueAction extends ActionSupport implements SessionAware {
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<String, Object> session;
    private List<FraudAlertRecord> alerts = new ArrayList<FraudAlertRecord>();
    private String username;

    @Override
    public String execute() {
        SessionUser user = session != null ? (SessionUser) session.get(SsoSessionService.SESSION_USER) : null;
        username = user == null ? "Unknown" : user.getUsername();

        try (java.sql.Connection connection = FraudConnectionFactory.openConnection();
             java.sql.PreparedStatement statement = connection.prepareStatement(
                 "SELECT TOP 100 fa.AlertID, fa.TransactionID, fa.AlertType, fa.Severity, fa.Status, " +
                     "fa.Description, fa.CreatedDate, a.AccountNumber, t.Amount " +
                     "FROM FraudAlerts fa " +
                     "LEFT JOIN Accounts a ON fa.AccountID = a.AccountID " +
                     "LEFT JOIN Transactions t ON fa.TransactionID = t.TransactionID " +
                     "WHERE fa.Status IN ('New', 'InReview') " +
                     "ORDER BY fa.CreatedDate DESC");
             java.sql.ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                FraudAlertRecord record = new FraudAlertRecord();
                record.setAlertId(resultSet.getInt(1));
                long transactionId = resultSet.getLong(2);
                record.setTransactionId(resultSet.wasNull() ? null : Long.valueOf(transactionId));
                record.setAlertType(resultSet.getString(3));
                record.setSeverity(resultSet.getString(4));
                record.setStatus(resultSet.getString(5));
                record.setDescription(resultSet.getString(6));
                java.util.Date created = resultSet.getTimestamp(7);
                record.setCreatedDate(created == null ? "" : DATE_FORMAT.format(created));
                record.setAccountNumber(resultSet.getString(8));
                BigDecimal amount = resultSet.getBigDecimal(9);
                record.setAmount(amount == null ? "" : MONEY_FORMAT.format(amount));
                alerts.add(record);
            }
        } catch (java.sql.SQLException exception) {
            addActionError("Unable to load flagged transaction queue.");
        }
        return SUCCESS;
    }

    public List<FraudAlertRecord> getAlerts() {
        return alerts;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
