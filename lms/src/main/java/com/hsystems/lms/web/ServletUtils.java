package com.hsystems.lms.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by administrator on 13/8/16.
 */
public final class ServletUtils {

  public static String getCookieValue(
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
}
