package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrQuestionResourceMapper
    implements Mapper<SolrDocument, Auditable<QuestionResource>> {

  private static final String ID_FIELD = "id";

  private final List<Component> components;

  private final SolrQuestionMapperFactory questionMapperFactory;

  public SolrQuestionResourceMapper(List<Component> components) {
    this.components = components;
    this.questionMapperFactory = new SolrQuestionMapperFactory();
  }

  @Override
  public Auditable<QuestionResource> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    SolrQuestionMapper<Question> questionMapper
        = questionMapperFactory.create(source, components);
    Question question = questionMapper.from(source);
    QuestionResource.Builder builder
        = new QuestionResource.Builder(id, question);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    SolrAuditableMapper<QuestionResource> auditableMapper
        = new SolrAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}