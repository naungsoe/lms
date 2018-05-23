package com.hsystems.lms.web.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.hsystems.lms.authentication.repository.hbase.HBaseSignInLogRepository;
import com.hsystems.lms.component.repository.hbase.HBaseComponentRepository;
import com.hsystems.lms.component.repository.solr.SolrComponentRepository;
import com.hsystems.lms.course.repository.hbase.HBaseCourseRepository;
import com.hsystems.lms.course.repository.solr.SolrCourseRepository;
import com.hsystems.lms.course.service.CourseService;
import com.hsystems.lms.group.repository.hbase.HBaseGroupRepository;
import com.hsystems.lms.group.repository.solr.SolrGroupRepository;
import com.hsystems.lms.group.service.GroupService;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.lesson.repository.hbase.HBaseLessonRepository;
import com.hsystems.lms.lesson.repository.solr.SolrLessonRepository;
import com.hsystems.lms.lesson.service.LessonService;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRepository;
import com.hsystems.lms.level.repository.solr.SolrLevelRepository;
import com.hsystems.lms.level.service.LevelService;
import com.hsystems.lms.operation.service.GroupIndexService;
import com.hsystems.lms.operation.service.LevelIndexService;
import com.hsystems.lms.operation.service.SchoolIndexService;
import com.hsystems.lms.operation.service.SubjectIndexService;
import com.hsystems.lms.operation.service.UserIndexService;
import com.hsystems.lms.question.repository.hbase.HBaseQuestionRepository;
import com.hsystems.lms.question.repository.solr.SolrQuestionRepository;
import com.hsystems.lms.question.service.QuestionService;
import com.hsystems.lms.quiz.repository.hbase.HBaseQuizRepository;
import com.hsystems.lms.quiz.repository.solr.SolrQuizRepository;
import com.hsystems.lms.quiz.service.QuizService;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRepository;
import com.hsystems.lms.school.repository.solr.SolrSchoolRepository;
import com.hsystems.lms.school.service.SchoolService;
import com.hsystems.lms.solr.SolrClient;
import com.hsystems.lms.subject.repository.hbase.HBaseSubjectRepository;
import com.hsystems.lms.subject.repository.solr.SolrSubjectRepository;
import com.hsystems.lms.subject.service.SubjectService;
import com.hsystems.lms.user.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.user.repository.solr.SolrUserRepository;
import com.hsystems.lms.user.service.UserService;

public final class ServiceModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(HBaseClient.class).in(Singleton.class);
    bind(HBaseSchoolRepository.class).in(Singleton.class);
    bind(HBaseLevelRepository.class).in(Singleton.class);
    bind(HBaseSubjectRepository.class).in(Singleton.class);
    bind(HBaseGroupRepository.class).in(Singleton.class);
    bind(HBaseUserRepository.class).in(Singleton.class);
    bind(HBaseComponentRepository.class).in(Singleton.class);
    bind(HBaseQuestionRepository.class).in(Singleton.class);
    bind(HBaseQuizRepository.class).in(Singleton.class);
    bind(HBaseLessonRepository.class).in(Singleton.class);
    bind(HBaseCourseRepository.class).in(Singleton.class);
    bind(HBaseSignInLogRepository.class).in(Singleton.class);

    bind(SolrClient.class).in(Singleton.class);
    bind(SolrSchoolRepository.class).in(Singleton.class);
    bind(SolrLevelRepository.class).in(Singleton.class);
    bind(SolrSubjectRepository.class).in(Singleton.class);
    bind(SolrGroupRepository.class).in(Singleton.class);
    bind(SolrUserRepository.class).in(Singleton.class);
    bind(SolrComponentRepository.class).in(Singleton.class);
    bind(SolrQuestionRepository.class).in(Singleton.class);
    bind(SolrQuizRepository.class).in(Singleton.class);
    bind(SolrLessonRepository.class).in(Singleton.class);
    bind(SolrCourseRepository.class).in(Singleton.class);

    bind(SchoolService.class).in(Singleton.class);
    bind(SchoolIndexService.class).in(Singleton.class);
    bind(LevelService.class).in(Singleton.class);
    bind(LevelIndexService.class).in(Singleton.class);
    bind(SubjectService.class).in(Singleton.class);
    bind(SubjectIndexService.class).in(Singleton.class);
    bind(GroupService.class).in(Singleton.class);
    bind(GroupIndexService.class).in(Singleton.class);
    bind(UserService.class).in(Singleton.class);
    bind(UserIndexService.class).in(Singleton.class);
    bind(QuestionService.class).in(Singleton.class);
    bind(QuizService.class).in(Singleton.class);
    bind(LessonService.class).in(Singleton.class);
    bind(CourseService.class).in(Singleton.class);
  }
}