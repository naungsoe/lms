package com.hsystems.lms.web;

import javax.json.Json;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by administrator on 13/8/16.
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
}
