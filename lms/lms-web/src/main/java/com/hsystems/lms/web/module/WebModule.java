package com.hsystems.lms.web.module;

import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.interceptor.LogInterceptor;
import com.hsystems.lms.service.model.UserModel;
import com.hsystems.lms.web.AuthenticationFilter;
import com.hsystems.lms.web.ErrorServlet;
import com.hsystems.lms.web.HomeServlet;
import com.hsystems.lms.web.QuestionServlet;
import com.hsystems.lms.web.SignInServlet;
import com.hsystems.lms.web.SignOutServlet;
import com.hsystems.lms.web.SignUpServlet;
import com.hsystems.lms.web.StorageServlet;
import com.hsystems.lms.web.UserServlet;
import com.hsystems.lms.web.provider.UserModelProvider;
import com.hsystems.lms.web.security.RequiresInterceptor;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Created by naungsoe on 29/8/16.
 */
public class WebModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bindInterceptor(Matchers.any(),
        Matchers.annotatedWith(Log.class),
        new LogInterceptor());

    bindInterceptor(Matchers.any(),
        Matchers.annotatedWith(Requires.class),
        new RequiresInterceptor(getProvider(UserModel.class)));

    bind(UserModel.class).toProvider(UserModelProvider.class)
        .in(RequestScoped.class);
    bind(ErrorServlet.class).in(Singleton.class);
    bind(SignUpServlet.class).in(Singleton.class);
    bind(SignInServlet.class).in(Singleton.class);
    bind(SignOutServlet.class).in(Singleton.class);
    bind(UserServlet.class).in(Singleton.class);
    bind(HomeServlet.class).in(Singleton.class);
    bind(StorageServlet.class).in(Singleton.class);
    bind(QuestionServlet.class).in(Singleton.class);
    bind(AuthenticationFilter.class).in(Singleton.class);

    filter("/web/*").through(AuthenticationFilter.class);
    serve("/web/error").with(ErrorServlet.class);
    serve("/web/signup").with(SignUpServlet.class);
    serve("/web/signin").with(SignInServlet.class);
    serve("/web/signout").with(SignOutServlet.class);
    serve("/web/users").with(UserServlet.class);
    serve("/web/home").with(HomeServlet.class);
    serve("/web/storage").with(StorageServlet.class);
    serve("/web/questions").with(QuestionServlet.class);
    serve("/webapi/*").with(GuiceContainer.class);
  }
}
