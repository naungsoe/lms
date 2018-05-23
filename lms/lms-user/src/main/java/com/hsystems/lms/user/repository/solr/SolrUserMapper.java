package com.hsystems.lms.user.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.solr.SolrGroupRefsMapper;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.repository.entity.Credentials;
import com.hsystems.lms.user.repository.entity.SchoolUser;

import org.apache.solr.common.SolrDocument;

import java.time.LocalDateTime;

public final class SolrUserMapper
    implements Mapper<SolrDocument, Auditable<AppUser>> {

  private static final String ID_FIELD = "id";
  private static final String FIRST_NAME_FIELD = "firstName";
  private static final String LAST_NAME_FIELD = "lastName";
  private static final String DATE_OF_BIRTH_FIELD = "dateOfBirth";
  private static final String GENDER_FIELD = "gender";
  private static final String MOBILE_FIELD = "mobile";
  private static final String EMAIL_FIELD = "email";
  private static final String ACCOUNT_FIELD = "account";

  public SolrUserMapper() {

  }

  @Override
  public Auditable<AppUser> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String firstName = SolrUtils.getString(source, FIRST_NAME_FIELD);
    String lastName = SolrUtils.getString(source, LAST_NAME_FIELD);
    LocalDateTime dateOfBirth
        = SolrUtils.getDateTime(source, DATE_OF_BIRTH_FIELD);
    String gender = SolrUtils.getString(source, GENDER_FIELD);
    String mobile = SolrUtils.getString(source, MOBILE_FIELD);
    String email = SolrUtils.getString(source, EMAIL_FIELD);
    String account = SolrUtils.getString(source, ACCOUNT_FIELD);
    Credentials credentials = new Credentials(account, "", "");
    SchoolUser.Builder builder
        = new SchoolUser.Builder(id, firstName, lastName)
        .dateOfBirth(dateOfBirth)
        .gender(gender)
        .mobile(mobile)
        .email(email)
        .credentials(credentials);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    SolrGroupRefsMapper groupRefsMapper = new SolrGroupRefsMapper();
    builder.groups(groupRefsMapper.from(source));

    SolrAuditableMapper<AppUser> auditableMapper
        = new SolrAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}