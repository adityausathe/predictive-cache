package com.adus.predictivecache.client.config;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.*;
import java.io.IOException;

@Component
public class RequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //no-op
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String userId = ServletRequestUtils.getRequiredStringParameter(servletRequest, "userId");
        ThreadContextHolder.setUserId(userId);

        // experiment setup start
        ThreadContextHolder.setRequestTimeStamp(servletRequest.getParameter("original_timestamp"));
        // experiment setup end

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        //no-op
    }
}
