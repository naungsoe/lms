package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;

import org.apache.solr.common.SolrInputDocument;

public final class SolrUserRefDocMapper
    implements Mapper<User, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String ENTITY_ID_FIELD = "entityId";
  private static final String PARENT_ID_FIELD = "parentId";
  private static final String FIELD_NAME_FIELD = "fieldName";
  private static final String FIRST_NAME_FIELD = "firstName";
  private static final String LAST_NAME_FIELD = "lastName";

  private final String parentId;

  private final String fieldName;

  public SolrUserRefDocMapper(String parentId, String fieldName) {
    this.parentId = parentId;
    this.fieldName = fieldName;
  }

  @Override
  public SolrInputDocument from(User source) {
    SolrInputDocument document = new SolrInputDocument();
    document.addField(ID_FIELD, CommonUtils.genUniqueKey());
    document.addField(ENTITY_ID_FIELD, source.getId());
    document.addField(PARENT_ID_FIELD, parentId);
    document.addField(FIELD_NAME_FIELD, fieldName);
    document.addField(FIRST_NAME_FIELD, source.getLastName());
    document.addField(LAST_NAME_FIELD, source.getLastName());
    return document;
  }
}