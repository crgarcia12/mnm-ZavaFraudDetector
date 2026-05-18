package com.zavabank.frauddetector;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

public class AuthTokenInterceptor implements Interceptor {
    private final SsoSessionService ssoSessionService = new SsoSessionService();

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String actionName = invocation.getProxy().getActionName();
        if ("login".equals(actionName) || "health".equals(actionName)) {
            return invocation.invoke();
        }

        Map<String, Object> session = invocation.getInvocationContext().getSession();
        if (session != null && session.get(SsoSessionService.SESSION_USER) instanceof SessionUser) {
            return invocation.invoke();
        }

        HttpServletRequest request = ServletActionContext.getRequest();
        SessionUser resolved = ssoSessionService.resolveSessionUser(request, "");
        if (resolved != null) {
            session.put(SsoSessionService.SESSION_USER, resolved);
            return invocation.invoke();
        }

        return "login";
    }
}
