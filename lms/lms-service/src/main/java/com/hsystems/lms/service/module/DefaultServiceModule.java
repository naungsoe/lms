package com.hsystems.lms.service.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.QuizRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.hbase.HBaseAuditLogRepository;
import com.hsystems.lms.repository.hbase.HBaseGroupRepository;
import com.hsystems.lms.repository.hbase.HBaseQuestionRepository;
import com.hsystems.lms.repository.hbase.HBaseQuizRepository;
import com.hsystems.lms.repository.hbase.HBaseSchoolRepository;
import com.hsystems.lms.repository.hbase.HBaseShareLogRepository;
import com.hsystems.lms.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;
import com.hsystems.lms.repository.hbase.provider.HBaseClientProvider;
import com.hsystems.lms.repository.solr.SolrIndexRepository;
import com.hsystems.lms.repository.solr.provider.SolrClient;
import com.hsystems.lms.repository.solr.provider.SolrClientProvider;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.QuestionService;
import com.hsystems.lms.service.QuizService;
import com.hsystems.lms.service.SchoolService;
import com.hsystems.lms.service.UserService;

/**
 * Created by naungsoe on 21/8/16.
 */
public class DefaultServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(HBaseClient.class).toProvider(HBaseClientProvider.class)
        .in(Singleton.class);
    bind(AuditLogRepository.class).to(HBaseAuditLogRepository.class)
        .in(Singleton.class);
    bind(ShareLogRepository.class).to(HBaseShareLogRepository.class)
        .in(Singleton.class);
    bind(SchoolRepository.class).to(HBaseSchoolRepository.class)
        .in(Singleton.class);
    bind(GroupRepository.class).to(HBaseGroupRepository.class)
        .in(Singleton.class);
    bind(UserRepository.class).to(HBaseUserRepository.class)
        .in(Singleton.class);
    bind(QuizRepository.class).to(HBaseQuizRepository.class)
        .in(Singleton.class);
    bind(QuestionRepository.class).to(HBaseQuestionRepository.class)
        .in(Singleton.class);
    bind(SolrClient.class).toProvider(SolrClientProvider.class)
        .in(Singleton.class);
    bind(IndexRepository.class).to(SolrIndexRepository.class)
        .in(Singleton.class);
    bind(AuthenticationService.class).in(Singleton.class);
    bind(SchoolService.class).in(Singleton.class);
    bind(UserService.class).in(Singleton.class);
    bind(QuizService.class).in(Singleton.class);
    bind(QuestionService.class).in(Singleton.class);
  }
}
