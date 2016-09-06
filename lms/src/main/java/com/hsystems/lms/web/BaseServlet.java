package com.hsystems.lms.web;

import com.google.inject.Injector;

import com.hsystems.lms.FileUtils;
import com.hsystems.lms.JsonUtils;

import org.apache.commons.lang.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.json.JsonObject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by administrator on 8/8/16.
 */
public abstract class BaseServlet extends HttpServlet {

  private HttpServletRequest request;

  private HttpServletResponse response;

  private JsonObject localObject;

  protected HttpServletRequest getRequest() {
    return request;
  }

  protected HttpServletResponse getResponse() {
    return response;
  }

  @Override
  public void init(ServletConfig config) throws ServletException {
    String className = Injector.class.getName();
    ServletContext context = config.getServletContext();
    Injector injector = (Injector) context.getAttribute(className);

    if (injector == null) {
      throw new ServletException("guice Injector not found");
    }
    injector.injectMembers(this);
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

  protected void loadLocale(String page) {
    String defaultLocale = "en_US";
    String locale = ServletUtils.getCookie(request, "locale");
    locale = StringUtils.isEmpty(locale) ? defaultLocale : locale;
    request.setAttribute("locale", locale);

    String localeUrl = "/locales/" + page + "/" + locale + ".json";
    request.setAttribute("localeUrl", localeUrl);

    ServletContext context = request.getServletContext();
    InputStream stream = context.getResourceAsStream(localeUrl);
    localObject = JsonUtils.parseJson(stream).getJsonObject(locale);
  }

  protected void loadAttribute(String name) {
    this.loadAttributes(new String[]{name});
  }

  protected void loadAttributes(String[] names) {
    for (String name : names) {
      String value = localObject.getString(name);
      this.setAttribute(name, value);
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
