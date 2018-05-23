package com.hsystems.lms.quiz.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.quiz.repository.entity.Quiz;
import com.hsystems.lms.quiz.repository.entity.QuizResource;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrQuizResourceMapper
    implements Mapper<SolrDocument, Auditable<QuizResource>> {

  private static final String ID_FIELD = "id";

  private final List<Component> components;

  public SolrQuizResourceMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public Auditable<QuizResource> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    SolrQuizMapper quizMapper = new SolrQuizMapper(components);
    Quiz quiz = quizMapper.from(source);
    QuizResource.Builder builder = new QuizResource.Builder(id, quiz);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    SolrAuditableMapper<QuizResource> auditableMapper
        = new SolrAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}