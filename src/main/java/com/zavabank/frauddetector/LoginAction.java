package com.zavabank.frauddetector;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

public class LoginAction extends ActionSupport implements SessionAware {
    private final SsoSessionService ssoSessionService = new SsoSessionService();
    private Map<String, Object> session;
    private String sessionToken;

    @Override
    public String execute() {
        if (session != null && session.get(SsoSessionService.SESSION_USER) instanceof SessionUser) {
            return SUCCESS;
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        SessionUser resolved = ssoSessionService.resolveSessionUser(request, sessionToken);
        if (resolved != null) {
            session.put(SsoSessionService.SESSION_USER, resolved);
            return SUCCESS;
        }

        if (sessionToken != null && !sessionToken.trim().isEmpty()) {
            addActionError("Session token was not found or has expired.");
        }
        return INPUT;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
