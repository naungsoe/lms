package com.hsystems.lms.web.module;

import com.google.inject.AbstractModule;

import com.hsystems.lms.web.webapi.AccountController;
import com.hsystems.lms.web.webapi.QuestionController;
import com.hsystems.lms.web.webapi.SchoolController;
import com.hsystems.lms.web.webapi.UserController;

/**
 * Created by naungsoe on 21/8/16.
 */
public final class WebAPIModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AccountController.class);
    bind(SchoolController.class);
    bind(UserController.class);
    bind(QuestionController.class);
  }
}