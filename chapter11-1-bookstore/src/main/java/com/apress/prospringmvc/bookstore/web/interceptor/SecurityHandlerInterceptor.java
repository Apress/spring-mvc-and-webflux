package com.apress.prospringmvc.bookstore.web.interceptor;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.service.AuthenticationException;
import com.apress.prospringmvc.bookstore.web.controller.LoginController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@code HandlerInterceptor} to apply security to controllers.
 * 
 * @author Marten Deinum
 */
public class SecurityHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var account = (Account) WebUtils.getSessionAttribute(request, LoginController.ACCOUNT_ATTRIBUTE);
        if (account == null) {

            //Retrieve and store the original URL.
            var url = request.getRequestURL().toString();
            WebUtils.setSessionAttribute(request, LoginController.REQUESTED_URL, url);
            throw new AuthenticationException("Authentication required.", "authentication.required");
        }
        return true;
    }

}
