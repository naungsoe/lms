package com.hsystems.lms.web;

import com.hsystems.lms.service.annotation.Log;

import org.apache.log4j.Logger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Created by administrator on 8/8/16.
 */
@WebServlet(value = "/web/error", loadOnStartup = 1)
public final class ErrorServlet extends BaseServlet {

  private static final long serialVersionUID = -1943733219860896344L;

  private static final String ERROR_REQUEST_URI
      = "javax.servlet.error.request_uri";

  private static final String ERROR_EXCEPTION
      = "javax.servlet.error.exception";

  private final static Logger logger
      = Logger.getLogger(ErrorServlet.class);

  private final static String messageFormat
      = "error requesting uri %s";

  @Override
  @Log
  protected void doGet()
      throws ServletException, IOException {

    String servlet = (String)getAttribute(ERROR_REQUEST_URI);
    Exception exception = (Exception)getAttribute(ERROR_EXCEPTION);
    logger.error(String.format(messageFormat, servlet), exception);
    forwardRequest("/jsp/error/index.jsp");
  }

  @Override
  protected void doPost()
      throws ServletException, IOException {

  }
}
