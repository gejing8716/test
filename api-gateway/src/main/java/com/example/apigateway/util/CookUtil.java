package com.example.apigateway.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookUtil {

    /**
     * 通过key获取cookie值的方法
     * @param key
     * @param request
     * @return
     */
    public static Cookie getCookieValue(String key, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies!= null  && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (key.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }

        return null;

    }
   //hello
    //word
}
