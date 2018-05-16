package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class SolrAuditableMapper<T extends Entity>
    implements Mapper<SolrDocument, Auditable<T>> {

  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String CREATED_ON_FIELD = "createdOn";
  private static final String MODIFIED_BY_FIELD = "modifiedBy";
  private static final String MODIFIED_ON_FIELD = "modifiedOn";

  private final T entity;

  private final String parentId;

  private final SolrUserRefMapper userRefMapper;

  public SolrAuditableMapper(T entity, String parentId) {
    this.entity = entity;
    this.parentId = parentId;
    this.userRefMapper = new SolrUserRefMapper();
  }

  @Override
  public Auditable<T> from(SolrDocument source) {
    Auditable.Builder<T> builder = new Auditable.Builder<>(entity);
    List<SolrDocument> childDocuments = source.getChildDocuments();
    Predicate<SolrDocument> createdByDocument
        = SolrUtils.isChildDocument(parentId, CREATED_BY_FIELD);
    Optional<SolrDocument> documentOptional = childDocuments.stream()
        .filter(createdByDocument).findFirst();

    if (documentOptional.isPresent()) {
      SolrDocument document = documentOptional.get();
      User createdBy = userRefMapper.from(document);
      LocalDateTime createdOn
          = SolrUtils.getDateTime(source, CREATED_ON_FIELD);
      builder.createdBy(createdBy).createdOn(createdOn);
    }

    Predicate<SolrDocument> modifiedByDocument
        = SolrUtils.isChildDocument(parentId, MODIFIED_BY_FIELD);
    documentOptional = childDocuments.stream()
        .filter(modifiedByDocument).findFirst();

    if (documentOptional.isPresent()) {
      SolrDocument document = documentOptional.get();
      User modifiedBy = userRefMapper.from(document);
      LocalDateTime modifiedOn
          = SolrUtils.getDateTime(source, MODIFIED_ON_FIELD);
      builder.modifiedBy(modifiedBy).modifiedOn(modifiedOn);
    }

    return builder.build();
  }
}