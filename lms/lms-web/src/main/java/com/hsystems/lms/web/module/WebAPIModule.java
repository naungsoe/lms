package com.hsystems.lms.web.module;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.RequestScoped;

import com.hsystems.lms.web.webapi.AccountController;
import com.hsystems.lms.web.webapi.QuestionController;
import com.hsystems.lms.web.webapi.SchoolController;
import com.hsystems.lms.web.webapi.UserController;

/**
 * Created by naungsoe on 21/8/16.
 */
public class WebAPIModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AccountController.class).in(RequestScoped.class);
    bind(SchoolController.class).in(RequestScoped.class);
    bind(UserController.class).in(RequestScoped.class);
    bind(QuestionController.class).in(RequestScoped.class);
  }
}
