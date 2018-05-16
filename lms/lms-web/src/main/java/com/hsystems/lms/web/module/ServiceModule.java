package com.hsystems.lms.web.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.hsystems.lms.component.repository.hbase.HBaseComponentRepository;
import com.hsystems.lms.component.repository.solr.SolrComponentRepository;
import com.hsystems.lms.group.repository.GroupRepository;
import com.hsystems.lms.group.repository.NoSqlGroupRepository;
import com.hsystems.lms.group.service.GroupService;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.level.repository.LevelRepository;
import com.hsystems.lms.level.repository.NoSqlLevelRepository;
import com.hsystems.lms.level.service.LevelService;
import com.hsystems.lms.question.repository.NoSqlQuestionRepository;
import com.hsystems.lms.question.repository.QuestionRepository;
import com.hsystems.lms.question.service.QuestionService;
import com.hsystems.lms.school.repository.NoSqlSchoolRepository;
import com.hsystems.lms.school.repository.SchoolRepository;
import com.hsystems.lms.school.service.SchoolService;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.subject.repository.NoSqlSubjectRepository;
import com.hsystems.lms.subject.repository.SubjectRepository;
import com.hsystems.lms.subject.service.SubjectService;
import com.hsystems.lms.user.repository.NoSqlUserRepository;
import com.hsystems.lms.user.repository.UserRepository;
import com.hsystems.lms.user.service.UserService;

public final class ServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(SchoolRepository.class).to(NoSqlSchoolRepository.class)
        .in(Singleton.class);
    bind(LevelRepository.class).to(NoSqlLevelRepository.class)
        .in(Singleton.class);
    bind(SubjectRepository.class).to(NoSqlSubjectRepository.class)
        .in(Singleton.class);
    bind(GroupRepository.class).to(NoSqlGroupRepository.class)
        .in(Singleton.class);
    bind(UserRepository.class).to(NoSqlUserRepository.class)
        .in(Singleton.class);
    bind(QuestionRepository.class).to(NoSqlQuestionRepository.class)
        .in(Singleton.class);

    bind(HBaseClient.class).in(Singleton.class);
    bind(HBaseComponentRepository.class).in(Singleton.class);
    bind(SolrClient.class).in(Singleton.class);
    bind(SolrComponentRepository.class).in(Singleton.class);
    bind(SchoolService.class).in(Singleton.class);
    bind(LevelService.class).in(Singleton.class);
    bind(SubjectService.class).in(Singleton.class);
    bind(GroupService.class).in(Singleton.class);
    bind(UserService.class).in(Singleton.class);
    bind(QuestionService.class).in(Singleton.class);
  }
}