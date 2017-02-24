package com.hsystems.lms.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hsystems.lms.common.util.JsonUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public abstract class BaseServlet extends HttpServlet {

  protected void forwardRequest(
      HttpServletRequest request, HttpServletResponse response, String path)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(path);
    dispatcher.forward(request, response);
  }

  protected void redirectRequest(
      HttpServletResponse response, String path)
      throws ServletException, IOException {

    response.sendRedirect(path);
  }

  protected void loadLocale(HttpServletRequest request, String module)
      throws IOException {

    String defaultLocale = "en_US";
    String locale = ServletUtils.getCookie(request, "locale");
    locale = StringUtils.isEmpty(locale) ? defaultLocale : locale;
    request.setAttribute("locale", locale);

    String localeUrl = String.format("/webapi/locales/%s", module);
    request.setAttribute("localeUrl", localeUrl);

    String localeFilePath = String.format(
        "locales/%s/%s.json", module, locale);
    InputStream stream = getClass().getClassLoader()
        .getResourceAsStream(localeFilePath);
    JsonNode localNode = JsonUtils.parseJson(stream).get(locale);
    localNode.fields().forEachRemaining(field -> {
      request.setAttribute(field.getKey(),
          field.getValue().textValue());
    });
  }
}
