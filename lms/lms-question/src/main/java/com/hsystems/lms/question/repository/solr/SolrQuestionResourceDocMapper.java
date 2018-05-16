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
  private static final String TYPE_NAME_FIELD = "typeName";
  private static final String NAME_FIELD = "name";
  private static final String PERMISSIONS_FIELD = "permissions";

  public SolrQuestionResourceDocMapper() {

  }

  @Override
  public SolrInputDocument from(Auditable<QuestionResource> source) {
    SolrInputDocument document = new SolrInputDocument();
    QuestionResource resource = source.getEntity();
    document.addField(ID_FIELD, resource.getId());

    String typeName = QuestionResource.class.getSimpleName();
    document.addField(TYPE_NAME_FIELD, typeName);

    String parentId = resource.getId();
    Question question = resource.getQuestion();
    SolrQuestionDocMapper questionDocMapper
        = new SolrQuestionDocMapper(parentId);
    SolrInputDocument questionDocument
        = questionDocMapper.from(question);
    document.addChildDocument(questionDocument);

    School school = resource.getSchool();

    if (school != null) {
      SolrSchoolRefDocMapper schoolRefDocMapper
          = new SolrSchoolRefDocMapper(parentId);
      SolrInputDocument schoolDocument
          = schoolRefDocMapper.from(school);
      document.addChildDocument(schoolDocument);
    }

    SolrAuditableDocMapper<QuestionResource> auditableDocMapper
        = new SolrAuditableDocMapper<>(document, parentId);
    return auditableDocMapper.from(source);
  }
}