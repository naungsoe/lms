package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;

import org.apache.solr.common.SolrInputDocument;

import java.time.LocalDateTime;

public final class SolrAuditableDocMapper<T extends Entity>
    implements Mapper<Auditable<T>, SolrInputDocument> {

  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String CREATED_ON_FIELD = "createdOn";
  private static final String MODIFIED_BY_FIELD = "modifiedBy";
  private static final String MODIFIED_ON_FIELD = "modifiedOn";

  private final SolrInputDocument entityDocument;

  private final String parentId;

  public SolrAuditableDocMapper(
      SolrInputDocument entityDocument,
      String parentId) {

    this.entityDocument = entityDocument;
    this.parentId = parentId;
  }

  @Override
  public SolrInputDocument from(Auditable<T> source) {
    String parentId = source.getEntity().getId();
    User createdBy = source.getCreatedBy();
    LocalDateTime createdOn = source.getCreatedOn();

    if ((createdBy != null) && (createdOn != null)) {
      SolrUserRefDocMapper userRefDocMapper
          = new SolrUserRefDocMapper(parentId, CREATED_BY_FIELD);
      SolrInputDocument createdByDocument = userRefDocMapper.from(createdBy);
      entityDocument.addChildDocument(createdByDocument);
      entityDocument.addField(CREATED_ON_FIELD, createdOn);
    }

    User modifiedBy = source.getModifiedBy();
    LocalDateTime modifiedOn = source.getModifiedOn();

    if (modifiedBy != null) {
      SolrUserRefDocMapper userRefDocMapper
          = new SolrUserRefDocMapper(parentId, MODIFIED_BY_FIELD);
      SolrInputDocument modifiedByDocument = userRefDocMapper.from(modifiedBy);
      entityDocument.addChildDocument(modifiedByDocument);
      entityDocument.addField(MODIFIED_ON_FIELD, modifiedOn);
    }

    return entityDocument;
  }
}