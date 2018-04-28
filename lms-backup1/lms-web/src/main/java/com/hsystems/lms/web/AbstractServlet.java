package com.hsystems.lms.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hsystems.lms.common.util.JsonUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.web.util.ServletUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public abstract class AbstractServlet extends HttpServlet {

  private static final String REFERER_PATTERN = "^[^/]*\\//[^/]*(\\/.*)$";

  private static final String REFERER_HEADER = "referer";

  protected void forwardRequest(
      HttpServletRequest request, HttpServletResponse response, String path)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(path);
    dispatcher.forward(request, response);
  }

  protected void redirectRequest(
      HttpServletRequest request, HttpServletResponse response, String path)
      throws ServletException, IOException {

    String contextPath = request.getContextPath();
    String redirectPath = path.startsWith(contextPath)
        ? path : String.format("%s%s", contextPath, path);
    response.sendRedirect(redirectPath);
  }

  protected void loadLocale(HttpServletRequest request, String module)
      throws IOException {

    String defaultLocale = "en_US";
    String locale = ServletUtils.getCookie(request, "locale");
    locale = StringUtils.isEmpty(locale) ? defaultLocale : locale;
    request.setAttribute("locale", locale);

    String localeFilePath = String.format(
        "locales/%s/%s.json", module, locale);
    InputStream stream = getClass().getClassLoader()
        .getResourceAsStream(localeFilePath);
    JsonNode localNode = JsonUtils.parseJson(stream).get(locale);
    localNode.fields().forEachRemaining(field -> {
      JsonNode valueNode = field.getValue();
      request.setAttribute(field.getKey(), valueNode.textValue());
    });
  }

  protected String getRefererPath(HttpServletRequest request) {
    String referer = request.getHeader(REFERER_HEADER);
    Pattern pattern = Pattern.compile(REFERER_PATTERN);
    Matcher matcher = pattern.matcher(referer);
    return matcher.find() ? matcher.group(1) : "";
  }
}