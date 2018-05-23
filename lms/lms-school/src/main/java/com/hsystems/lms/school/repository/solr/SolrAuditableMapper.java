package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.time.LocalDateTime;

public final class SolrAuditableMapper<T extends Entity>
    implements Mapper<SolrDocument, Auditable<T>> {

  private static final String CREATED_ON_FIELD = "createdOn";
  private static final String MODIFIED_ON_FIELD = "modifiedOn";

  private final T entity;

  private final SolrCreatedByMapper createdByMapper;

  private final SolrModifiedByMapper modifiedByMapper;

  public SolrAuditableMapper(T entity) {
    this.entity = entity;
    this.createdByMapper = new SolrCreatedByMapper();
    this.modifiedByMapper = new SolrModifiedByMapper();
  }

  @Override
  public Auditable<T> from(SolrDocument source) {
    Auditable.Builder<T> builder = new Auditable.Builder<>(entity);
    User createdBy = createdByMapper.from(source);
    LocalDateTime createdOn = SolrUtils.getDateTime(source, CREATED_ON_FIELD);
    builder.createdBy(createdBy).createdOn(createdOn);

    if (SolrUtils.containsField(source, MODIFIED_ON_FIELD)) {
      User modifiedBy = modifiedByMapper.from(source);
      LocalDateTime modifiedOn
          = SolrUtils.getDateTime(source, MODIFIED_ON_FIELD);
      builder.modifiedBy(modifiedBy).modifiedOn(modifiedOn);
    }

    return builder.build();
  }

  public User createUser(String id, String firstName, String lastName) {
    return new User() {
      @Override
      public String getId() {
        return id;
      }

      @Override
      public String getFirstName() {
        return firstName;
      }

      @Override
      public String getLastName() {
        return lastName;
      }
    };
  }

  class SolrCreatedByMapper implements Mapper<SolrDocument, User> {

    private static final String ID_FIELD = "createdBy.id";
    private static final String FIRST_NAME_FIELD = "createdBy.firstName";
    private static final String LAST_NAME_FIELD = "createdBy.lastName";

    @Override
    public User from(SolrDocument source) {
      String id = SolrUtils.getString(source, ID_FIELD);
      String firstName = SolrUtils.getString(source, FIRST_NAME_FIELD);
      String lastName = SolrUtils.getString(source, LAST_NAME_FIELD);
      return createUser(id, firstName, lastName);
    }
  }

  class SolrModifiedByMapper implements Mapper<SolrDocument, User> {

    static final String ID_FIELD = "modifiedBy.id";
    static final String FIRST_NAME_FIELD = "modifiedBy.firstName";
    static final String LAST_NAME_FIELD = "modifiedBy.lastName";

    @Override
    public User from(SolrDocument source) {
      String id = SolrUtils.getString(source, ID_FIELD);
      String firstName = SolrUtils.getString(source, FIRST_NAME_FIELD);
      String lastName = SolrUtils.getString(source, LAST_NAME_FIELD);
      return createUser(id, firstName, lastName);
    }
  }
}