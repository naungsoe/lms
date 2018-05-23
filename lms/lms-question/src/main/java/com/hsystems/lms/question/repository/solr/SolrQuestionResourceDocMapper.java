package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableDocMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefDocMapper;

import org.apache.solr.common.SolrInputDocument;

public final class SolrQuestionResourceDocMapper
    implements Mapper<Auditable<QuestionResource>, SolrInputDocument> {

  private static final String ID_FIELD = "id";

  private final SolrQuestionDocMapperFactory mapperFactory;

  public SolrQuestionResourceDocMapper() {
    this.mapperFactory = new SolrQuestionDocMapperFactory();
  }

  @Override
  public SolrInputDocument from(Auditable<QuestionResource> source) {
    SolrInputDocument document = new SolrInputDocument();
    QuestionResource resource = source.getEntity();
    document.addField(ID_FIELD, resource.getId());

    Question question = resource.getQuestion();
    SolrQuestionDocMapper<Question> questionDocMapper
        = mapperFactory.create(question, document);
    document = questionDocMapper.from(question);

    School school = resource.getSchool();
    SolrSchoolRefDocMapper schoolRefDocMapper
        = new SolrSchoolRefDocMapper(document);
    document = schoolRefDocMapper.from(school);

    SolrAuditableDocMapper<QuestionResource> auditableDocMapper
        = new SolrAuditableDocMapper<>(document);
    return auditableDocMapper.from(source);
  }
}