package com.hsystems.lms.web;

import com.hsystems.lms.ReflectionUtils;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by naungsoe on 13/8/16.
 */
public final class ServletUtils {

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
  
  public static <T> T getEntity(
      HttpServletRequest request, Class<T> type)
      throws ServletException {

    try {
      T instance = (T) ReflectionUtils.getInstance(type);
      Field[] fields = type.getDeclaredFields();

      for (Field field : fields) {
        String value = request.getParameter(field.getName());

        if (StringUtils.isNotEmpty(value)) {
          ReflectionUtils.setValue(instance, field.getName(), value);
        }
      }
      return instance;

    } catch (InstantiationException | IllegalAccessException
        | InvocationTargetException | NoSuchFieldException e) {

      throw new ServletException("error getting entity", e);
    }
  }
}
