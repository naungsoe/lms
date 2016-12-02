package com.hsystems.lms.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hsystems.lms.common.util.JsonUtils;
import com.hsystems.lms.web.util.ServletUtils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public abstract class BaseServlet extends HttpServlet {

  private HttpServletRequest request;

  private HttpServletResponse response;

  private JsonNode localObject;

  protected HttpServletRequest getRequest() {
    return request;
  }

  protected HttpServletResponse getResponse() {
    return response;
  }

  protected void initRequest(
    HttpServletRequest request, HttpServletResponse response) {

    this.request = request;
    this.response = response;
  }

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    initRequest(request, response);
    doGet();
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    initRequest(request, response);
    doPost();
  }

  protected void forwardRequest(String url)
      throws ServletException, IOException {

    RequestDispatcher dispatcher = request.getRequestDispatcher(url);
    dispatcher.forward(request, response);
  }

  protected void sendRedirect(String url)
      throws IOException {

    response.sendRedirect(url);
  }

  protected void loadLocale(String page)
      throws IOException {

    String defaultLocale = "en_US";
    String locale = ServletUtils.getCookie(request, "locale");
    locale = StringUtils.isEmpty(locale) ? defaultLocale : locale;
    request.setAttribute("locale", locale);

    String localeUrl = "/locales/" + page + "/" + locale + ".json";
    request.setAttribute("localeUrl", localeUrl);

    ServletContext context = request.getServletContext();
    InputStream stream = context.getResourceAsStream(localeUrl);
    localObject = JsonUtils.parseJson(stream).get(locale);
  }

  protected void loadAttribute(String name) {
    this.loadAttributes(new String[]{name});
  }

  protected void loadAttributes(String[] names) {
    for (String name : names) {
      JsonNode node = localObject.get(name);
      this.setAttribute(name, node.textValue());
    }
  }

  protected Object getSession(String name) {
    return request.getSession().getAttribute(name);
  }

  protected void setSession(String name, Object value) {
    request.getSession().setAttribute(name, value);
  }

  protected String getCookie(String name) {
    return ServletUtils.getCookie(request, name);
  }

  protected String getParameter(String name) {
    return request.getParameter(name);
  }

  protected Object getAttribute(String name) {
    return request.getAttribute(name);
  }

  protected void setAttribute(String name, Object value) {
    request.setAttribute(name, value);
  }

  protected abstract void doGet()
      throws ServletException, IOException;

  protected abstract void doPost()
      throws ServletException, IOException;
}
