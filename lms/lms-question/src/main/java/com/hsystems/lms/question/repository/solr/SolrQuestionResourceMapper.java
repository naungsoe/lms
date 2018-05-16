package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.Optional;

public final class SolrQuestionResourceMapper
    implements Mapper<SolrDocument, Auditable<QuestionResource>> {

  private static final String ID_FIELD = "id";
  private static final String QUESTION_FIELD = "question";

  public SolrQuestionResourceMapper() {

  }

  @Override
  public Auditable<QuestionResource> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    SolrQuestionMapper questionMapper
        = new SolrQuestionMapper(id, QUESTION_FIELD);
    Question question = questionMapper.from(source);
    QuestionResource.Builder builder
        = new QuestionResource.Builder(id, question);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      builder.school(school);
    }

    QuestionResource resource = builder.build();
    SolrAuditableMapper<QuestionResource> auditableMapper
        = new SolrAuditableMapper<>(resource, id);
    return auditableMapper.from(source);
  }
}