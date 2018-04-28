package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.util.SecurityUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.service.UserService;
import com.hsystems.lms.service.model.SignUpModel;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Created by naungsoe on 10/9/16.
 */
@Path("/account")
public class AccountController extends AbstractController {

  private static final String APPLICATION_PNG = "image/png";

  private final Provider<Properties> propertiesProvider;

  private final UserService userService;

  @Inject
  AccountController(
      Provider<Properties> propertiesProvider,
      UserService userService) {

    this.propertiesProvider = propertiesProvider;
    this.userService = userService;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response signUp(SignUpModel signUpModel)
      throws IOException {

    userService.signUp(signUpModel);

    return Response.status(Status.CREATED)
        .entity("account created").build();
  }

  @GET
  @Produces(APPLICATION_PNG)
  @Path("/captcha")
  public Response generateCaptcha(@Context HttpServletRequest request) {
    HttpSession session = request.getSession();
    String captcha = (String) session.getAttribute("captcha");
    CommonUtils.checkArgument(StringUtils.isNotEmpty(captcha),
        "error retrieving captcha");

    Properties properties = propertiesProvider.get();
    String imgWidth = properties.getProperty("captcha.image.width");
    String imgHeight = properties.getProperty("captcha.image.height");
    int width = Integer.parseInt(imgWidth);
    int height = Integer.parseInt(imgHeight);
    byte[] image = SecurityUtils.createCaptchaPng(captcha, width, height);
    return Response.ok(image).build();
  }
}
