package com.bestvike.portal.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public final class HttpUtils {
    public static String getToken(HttpServletRequest httpServletRequest) {
        String token = null;
        String authorization = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }
        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equalsIgnoreCase("X-Token")) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return token;
    }
}
