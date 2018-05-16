package com.hsystems.lms.web.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.hsystems.lms.web.webapi.AccountController;
import com.hsystems.lms.web.webapi.CourseController;
import com.hsystems.lms.web.webapi.DriveController;
import com.hsystems.lms.web.webapi.FilterController;
import com.hsystems.lms.web.webapi.GraphQLController;
import com.hsystems.lms.web.webapi.GroupController;
import com.hsystems.lms.web.webapi.LessonController;
import com.hsystems.lms.web.webapi.LocaleController;
import com.hsystems.lms.web.webapi.QuestionController;
import com.hsystems.lms.web.webapi.QuizController;
import com.hsystems.lms.web.webapi.SchoolController;
import com.hsystems.lms.web.webapi.UploadController;
import com.hsystems.lms.web.webapi.UserController;
import com.hsystems.lms.web.webapi.indexing.CourseIndexController;
import com.hsystems.lms.web.webapi.indexing.GroupIndexController;
import com.hsystems.lms.web.webapi.indexing.LessonIndexController;
import com.hsystems.lms.web.webapi.indexing.LevelIndexController;
import com.hsystems.lms.web.webapi.indexing.QuestionIndexController;
import com.hsystems.lms.web.webapi.indexing.QuizIndexController;
import com.hsystems.lms.web.webapi.indexing.SubjectIndexController;
import com.hsystems.lms.web.webapi.indexing.UserIndexController;

/**
 * Created by naungsoe on 21/8/16.
 */
public final class WebAPIModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(LocaleController.class).in(Singleton.class);
    bind(FilterController.class).in(Singleton.class);
    bind(UploadController.class).in(Singleton.class);
    bind(AccountController.class).in(Singleton.class);
    bind(SchoolController.class).in(Singleton.class);
    bind(LevelIndexController.class).in(Singleton.class);
    bind(SubjectIndexController.class).in(Singleton.class);
    bind(GroupController.class).in(Singleton.class);
    bind(GroupIndexController.class).in(Singleton.class);
    bind(UserController.class).in(Singleton.class);
    bind(UserIndexController.class).in(Singleton.class);
    bind(CourseController.class).in(Singleton.class);
    bind(CourseIndexController.class).in(Singleton.class);
    bind(LessonController.class).in(Singleton.class);
    bind(LessonIndexController.class).in(Singleton.class);
    bind(QuizController.class).in(Singleton.class);
    bind(QuizIndexController.class).in(Singleton.class);
    bind(QuestionController.class).in(Singleton.class);
    bind(QuestionIndexController.class).in(Singleton.class);
    bind(DriveController.class).in(Singleton.class);
    bind(GraphQLController.class).in(Singleton.class);
  }
}
