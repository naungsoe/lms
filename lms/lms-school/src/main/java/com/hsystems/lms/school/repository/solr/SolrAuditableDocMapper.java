package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.entity.User;

import org.apache.solr.common.SolrInputDocument;

public final class SolrAuditableDocMapper<T extends Entity>
    implements Mapper<Auditable<T>, SolrInputDocument> {

  private static final String CREATED_BY_ID_FIELD = "createdBy.id";
  private static final String CREATED_BY_FIRST_NAME_FIELD = "createdBy.firstName";
  private static final String CREATED_BY_LAST_NAME_FIELD = "createdBy.lastName";
  private static final String CREATED_ON_FIELD = "createdOn";
  private static final String MODIFIED_BY_ID_FIELD = "modifiedBy.id";
  private static final String MODIFIED_BY_FIRST_NAME_FIELD = "modifiedBy.firstName";
  private static final String MODIFIED_BY_LAST_NAME_FIELD = "modifiedBy.lastName";
  private static final String MODIFIED_ON_FIELD = "modifiedOn";

  private final SolrInputDocument document;

  public SolrAuditableDocMapper(SolrInputDocument document) {
    this.document = document;
  }

  @Override
  public SolrInputDocument from(Auditable<T> source) {
    User createdBy = source.getCreatedBy();
    document.addField(CREATED_BY_ID_FIELD, createdBy.getId());
    document.addField(CREATED_BY_FIRST_NAME_FIELD, createdBy.getFirstName());
    document.addField(CREATED_BY_LAST_NAME_FIELD, createdBy.getLastName());
    document.addField(CREATED_ON_FIELD, source.getCreatedOn());

    User modifiedBy = source.getModifiedBy();

    if (modifiedBy != null) {
      document.addField(MODIFIED_BY_ID_FIELD, createdBy.getId());
      document.addField(MODIFIED_BY_FIRST_NAME_FIELD, createdBy.getFirstName());
      document.addField(MODIFIED_BY_LAST_NAME_FIELD, createdBy.getLastName());
      document.addField(MODIFIED_ON_FIELD, source.getCreatedOn());
    }

    return document;
  }
}