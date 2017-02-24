package com.hsystems.lms.web.util;

import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.lang.reflect.Field;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by naungsoe on 13/8/16.
 */
public final class ServletUtils {

  private static final String HEADER_XFORWARDEDFOR = "X-FORWARDED-FOR";

  public static String getCookie(
      HttpServletRequest request, String name) {

    Cookie[] cookies = request.getCookies();
    String value = "";

    if (cookies == null) {
      return value;
    }

    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(name)) {
        value = cookie.getValue();
      }
    }

    return value;
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

  public static String createToken(
      HttpServletRequest request, String id) {

    String forwardedAddr = request.getHeader(HEADER_XFORWARDEDFOR);
    String remoteAddr = StringUtils.isEmpty(forwardedAddr)
        ? request.getRemoteAddr() : forwardedAddr;
    return SecurityUtils.getMD5Hash(remoteAddr, id);
  }
}