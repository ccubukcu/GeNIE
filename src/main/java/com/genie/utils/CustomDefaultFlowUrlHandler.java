package com.genie.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.webflow.context.servlet.DefaultFlowUrlHandler;

/**
 * @author ccubukcu
 *
 */
public class CustomDefaultFlowUrlHandler extends DefaultFlowUrlHandler{

    @Override
    public String getFlowId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo(); // /post/99
        if (pathInfo != null) {
        	return pathInfo.substring(1);
        } else {
            String contextPath = request.getContextPath();
            if (StringUtils.hasText(contextPath)) {
                return request.getContextPath().substring(1);
            } else {
                return null;
            }
        }
    }
}
