package com.hsystems.lms.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public class ErrorServlet extends AbstractServlet {

  private static final long serialVersionUID = -1943733219860896344L;

  private static final String INDEX_PATH = "/jsp/error/index.jsp";

  private static final String REQUEST_URI = "javax.servlet.error.request_uri";
  private static final String EXCEPTION = "javax.servlet.error.exception";

  private final static Logger logger = LogManager.getRootLogger();

  private final static String messageFormat = "Error requesting uri %s";

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logException(request);
    forwardRequest(request, response, INDEX_PATH);
  }

  private void logException(HttpServletRequest request) {
    String servlet = (String) request.getAttribute(REQUEST_URI);
    Exception exception = (Exception) request.getAttribute(EXCEPTION);
    logger.error(String.format(messageFormat, servlet), exception);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    logException(request);
    forwardRequest(request, response, INDEX_PATH);
  }
}
