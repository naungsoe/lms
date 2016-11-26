package com.hsystems.lms.service.module;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.interceptor.LogInterceptor;
import com.hsystems.lms.common.interceptor.RequiresInterceptor;
import com.hsystems.lms.common.provider.PropertiesProvider;
import com.hsystems.lms.repository.AuditLogRepository;
import com.hsystems.lms.repository.GroupRepository;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.SchoolRepository;
import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.UserRepository;
import com.hsystems.lms.repository.hbase.HBaseAuditLogRepository;
import com.hsystems.lms.repository.hbase.HBaseGroupRepository;
import com.hsystems.lms.repository.hbase.HBaseQuestionRepository;
import com.hsystems.lms.repository.hbase.HBaseSchoolRepository;
import com.hsystems.lms.repository.hbase.HBaseShareLogRepository;
import com.hsystems.lms.repository.hbase.HBaseUserRepository;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;
import com.hsystems.lms.repository.hbase.provider.HBaseClientProvider;
import com.hsystems.lms.repository.solr.SolrIndexRepository;
import com.hsystems.lms.repository.solr.provider.SolrClient;
import com.hsystems.lms.repository.solr.provider.SolrClientProvider;
import com.hsystems.lms.service.AuthenticationService;
import com.hsystems.lms.service.SchoolService;
import com.hsystems.lms.service.UserService;

import java.util.Properties;

/**
 * Created by naungsoe on 21/8/16.
 */
public class DefaultServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bindInterceptor(Matchers.any(),
        Matchers.annotatedWith(Requires.class),
        new RequiresInterceptor());

    bindInterceptor(Matchers.any(),
        Matchers.annotatedWith(Log.class),
        new LogInterceptor());

    bind(HBaseClient.class).toProvider(HBaseClientProvider.class);
    bind(AuditLogRepository.class).to(HBaseAuditLogRepository.class);
    bind(ShareLogRepository.class).to(HBaseShareLogRepository.class);
    bind(SchoolRepository.class).to(HBaseSchoolRepository.class);
    bind(GroupRepository.class).to(HBaseGroupRepository.class);
    bind(UserRepository.class).to(HBaseUserRepository.class);
    bind(QuestionRepository.class).to(HBaseQuestionRepository.class);
    bind(Properties.class).toProvider(PropertiesProvider.class);
    bind(SolrClient.class).toProvider(SolrClientProvider.class);
    bind(IndexRepository.class).to(SolrIndexRepository.class);
    bind(AuthenticationService.class);
    bind(SchoolService.class);
    bind(UserService.class);
  }
}
