package com.hsystems.lms.web.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.hsystems.lms.web.webapi.AccountController;
import com.hsystems.lms.web.webapi.FilterController;
import com.hsystems.lms.web.webapi.IndexController;
import com.hsystems.lms.web.webapi.LocaleController;
import com.hsystems.lms.web.webapi.QuestionController;
import com.hsystems.lms.web.webapi.QuizController;
import com.hsystems.lms.web.webapi.SchoolController;
import com.hsystems.lms.web.webapi.UserController;

/**
 * Created by naungsoe on 21/8/16.
 */
public class WebAPIModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(LocaleController.class).in(Singleton.class);
    bind(FilterController.class).in(Singleton.class);
    bind(AccountController.class).in(Singleton.class);
    bind(SchoolController.class).in(Singleton.class);
    bind(UserController.class).in(Singleton.class);
    bind(QuizController.class).in(Singleton.class);
    bind(QuestionController.class).in(Singleton.class);
    bind(IndexController.class).in(Singleton.class);
  }
}
