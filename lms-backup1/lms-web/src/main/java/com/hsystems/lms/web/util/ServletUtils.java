package com.hsystems.lms.web.util;

import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.lang.reflect.Field;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by naungsoe on 13/8/16.
 */
public final class ServletUtils {

  private static final String ACCESS_TOKEN_HEADER = "X-ACCESS-TOKEN";
  private static final String SESSION_TOKEN_HEADER = "X-SESSION-TOKEN";
  private static final String CSRF_TOKEN_HEADER = "X-CSRF-TOKEN";
  private static final String FORWARDED_HEADER = "X-FORWARDED-FOR";

  public static String getCookie(
      HttpServletRequest request, String name) {

    Cookie[] cookies = request.getCookies();

    if (cookies == null) {
      return "";
    }

    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(name)) {
        return cookie.getValue();
      }
    }

    return "";
  }

  public static <T> T getModel(
      HttpServletRequest request, Class<T> type) {

    T instance = (T) ReflectionUtils.getInstance(type);
    Field[] fields = type.getDeclaredFields();

    for (Field field : fields) {
      String value = request.getParameter(field.getName());

      if (StringUtils.isNotEmpty(value)) {
        ReflectionUtils.setValue(instance, field.getName(), value);
      }
    }

    return instance;
  }

  public static String getAccessToken(HttpServletRequest request) {
    return request.getHeader(ACCESS_TOKEN_HEADER);
  }

  public static String getSessionToken(HttpServletRequest request) {
    return request.getHeader(SESSION_TOKEN_HEADER);
  }

  public static String getRemoteAddress(HttpServletRequest request) {
    String address = request.getHeader(FORWARDED_HEADER);
    return StringUtils.isEmpty(address) ? request.getRemoteAddr() : address;
  }
}