package com.hsystems.lms.web;

import com.hsystems.lms.common.util.CommonUtils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by naungsoe on 8/8/16.
 */
public class UtilServlet extends BaseServlet {

  private static final long serialVersionUID = 6520939507549033697L;

  private static final String JSP_PATH = "/jsp/util/index.jsp";

  @Override
  protected void doGet(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    request.setAttribute("id", CommonUtils.genUniqueKey());
    forwardRequest(request, response, JSP_PATH);
  }

  @Override
  protected void doPost(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
