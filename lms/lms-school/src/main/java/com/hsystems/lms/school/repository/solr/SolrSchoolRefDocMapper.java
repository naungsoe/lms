package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.School;

import org.apache.solr.common.SolrInputDocument;

public final class SolrSchoolRefDocMapper
    implements Mapper<School, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String ENTITY_ID_FIELD = "entityId";
  private static final String PARENT_ID_FIELD = "parentId";
  private static final String FIELD_NAME_FIELD = "fieldName";
  private static final String SCHOOL_FIELD = "school";
  private static final String NAME_FIELD = "name";

  private final String parentId;

  public SolrSchoolRefDocMapper(String parentId) {
    this.parentId = parentId;
  }

  @Override
  public SolrInputDocument from(School source) {
    SolrInputDocument document = new SolrInputDocument();
    document.addField(ID_FIELD, CommonUtils.genUniqueKey());
    document.addField(ENTITY_ID_FIELD, source.getId());
    document.addField(PARENT_ID_FIELD, parentId);
    document.addField(FIELD_NAME_FIELD, SCHOOL_FIELD);
    document.addField(NAME_FIELD, source.getName());
    return document;
  }
}