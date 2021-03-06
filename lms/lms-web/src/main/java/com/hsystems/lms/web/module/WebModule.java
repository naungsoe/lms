package com.hsystems.lms.web.module;

import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

import com.hsystems.lms.common.logging.LogInterceptor;
import com.hsystems.lms.common.logging.annotation.Log;
import com.hsystems.lms.common.properties.PropertiesProvider;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.security.RequiresInterceptor;
import com.hsystems.lms.common.security.annotation.Requires;
import com.hsystems.lms.web.AccountServlet;
import com.hsystems.lms.web.CourseServlet;
import com.hsystems.lms.web.DriveServlet;
import com.hsystems.lms.web.ErrorServlet;
import com.hsystems.lms.web.GroupServlet;
import com.hsystems.lms.web.HomeServlet;
import com.hsystems.lms.web.IndexServlet;
import com.hsystems.lms.web.LessonServlet;
import com.hsystems.lms.web.QuestionServlet;
import com.hsystems.lms.web.QuizServlet;
import com.hsystems.lms.web.SignInServlet;
import com.hsystems.lms.web.SignOutServlet;
import com.hsystems.lms.web.SignUpServlet;
import com.hsystems.lms.web.UserServlet;
import com.hsystems.lms.web.UtilServlet;
import com.hsystems.lms.web.filter.AuthenticationFilter;
import com.hsystems.lms.web.filter.CsrfValidationFilter;
import com.hsystems.lms.web.provider.PrincipalProvider;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.Properties;

/**
 * Created by naungsoe on 29/8/16.
 */
public final class WebModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Log.class),
        new LogInterceptor());
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Requires.class),
        new RequiresInterceptor(getProvider(Principal.class)));

    bind(Properties.class).toProvider(PropertiesProvider.class)
        .in(Singleton.class);
    bind(Principal.class).toProvider(PrincipalProvider.class)
        .in(RequestScoped.class);

    bind(AuthenticationFilter.class).in(Singleton.class);
    bind(CsrfValidationFilter.class).in(Singleton.class);
    bind(UtilServlet.class).in(Singleton.class);
    bind(ErrorServlet.class).in(Singleton.class);
    bind(SignUpServlet.class).in(Singleton.class);
    bind(SignInServlet.class).in(Singleton.class);
    bind(SignOutServlet.class).in(Singleton.class);
    bind(AccountServlet.class).in(Singleton.class);
    bind(HomeServlet.class).in(Singleton.class);
    bind(GroupServlet.class).in(Singleton.class);
    bind(UserServlet.class).in(Singleton.class);
    bind(CourseServlet.class).in(Singleton.class);
    bind(LessonServlet.class).in(Singleton.class);
    bind(QuizServlet.class).in(Singleton.class);
    bind(QuestionServlet.class).in(Singleton.class);
    bind(DriveServlet.class).in(Singleton.class);
    bind(IndexServlet.class).in(Singleton.class);

    filterRegex("/(web|webapi2)(\\/\\w+)*").through(AuthenticationFilter.class);
    filterRegex("/(web|webapi2)(\\/\\w+)*").through(CsrfValidationFilter.class);
    serveRegex("/web/util(\\/\\w+)*").with(UtilServlet.class);
    serveRegex("/web/error(\\/\\w+)*").with(ErrorServlet.class);
    serveRegex("/web/signup(\\/\\w+)*").with(SignUpServlet.class);
    serveRegex("/web/signin(\\/\\w+)*").with(SignInServlet.class);
    serveRegex("/web/signout(\\/\\w+)*").with(SignOutServlet.class);
    serveRegex("/web/account(\\/\\w+)*").with(AccountServlet.class);
    serveRegex("/web/home(\\/\\w+)*").with(HomeServlet.class);
    serveRegex("/web/groups(\\/\\w+)*").with(GroupServlet.class);
    serveRegex("/web/users(\\/\\w+)*").with(UserServlet.class);
    serveRegex("/web/courses(\\/\\w+)*").with(CourseServlet.class);
    serveRegex("/web/lessons(\\/\\w+)*").with(LessonServlet.class);
    serveRegex("/web/quizzes(\\/\\w+)*").with(QuizServlet.class);
    serveRegex("/web/questions(\\/\\w+)*").with(QuestionServlet.class);
    serveRegex("/web/drive(\\/\\w+)*").with(DriveServlet.class);
    serveRegex("/web/index(\\/\\w+)*").with(IndexServlet.class);
    serve("/webapi/*").with(GuiceContainer.class);
  }
}