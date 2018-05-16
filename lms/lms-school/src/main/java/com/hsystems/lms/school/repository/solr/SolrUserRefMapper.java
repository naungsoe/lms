package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

public final class SolrUserRefMapper
    implements Mapper<SolrDocument, User> {

  private static final String ID_FIELD = "entityId";
  private static final String FIRST_NAME_FIELD = "firstName";
  private static final String LAST_NAME_FIELD = "lastName";

  public SolrUserRefMapper() {

  }

  @Override
  public User from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String firstName = SolrUtils.getString(source, FIRST_NAME_FIELD);
    String lastName = SolrUtils.getString(source, LAST_NAME_FIELD);

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
}