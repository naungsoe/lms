package com.hsystems.lms.user.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.repository.solr.SolrGroupRefsDocMapper;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableDocMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefDocMapper;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.repository.entity.Credentials;
import com.hsystems.lms.user.repository.entity.SchoolUser;

import org.apache.solr.common.SolrInputDocument;

import java.util.Collections;
import java.util.Enumeration;

public final class SolrUserDocMapper
    implements Mapper<Auditable<AppUser>, SolrInputDocument> {

  private static final String ID_FIELD = "id";
  private static final String FIRST_NAME_FIELD = "firstName";
  private static final String LAST_NAME_FIELD = "lastName";
  private static final String DATE_OF_BIRTH_FIELD = "dateOfBirth";
  private static final String GENDER_FIELD = "gender";
  private static final String MOBILE_FIELD = "mobile";
  private static final String EMAIL_FIELD = "email";
  private static final String ACCOUNT_FIELD = "account";

  public SolrUserDocMapper() {

  }

  @Override
  public SolrInputDocument from(Auditable<AppUser> source) {
    SolrInputDocument document = new SolrInputDocument();
    AppUser user = source.getEntity();
    document.addField(ID_FIELD, user.getId());
    document.addField(FIRST_NAME_FIELD, user.getFirstName());
    document.addField(LAST_NAME_FIELD, user.getLastName());
    document.addField(DATE_OF_BIRTH_FIELD, user.getDateOfBirth());
    document.addField(GENDER_FIELD, user.getGender());
    document.addField(MOBILE_FIELD, user.getMobile());
    document.addField(EMAIL_FIELD, user.getEmail());
    document.addField(EMAIL_FIELD, user.getEmail());

    Credentials credentials = user.getCredentials();
    document.addField(ACCOUNT_FIELD, credentials.getAccount());

    if (user instanceof SchoolUser) {
      SchoolUser schoolUser = (SchoolUser) user;
      School school = schoolUser.getSchool();
      SolrSchoolRefDocMapper schoolRefDocMapper
          = new SolrSchoolRefDocMapper(document);
      document = schoolRefDocMapper.from(school);

      Enumeration<Group> enumeration = schoolUser.getGroups();

      if (CollectionUtils.isNotEmpty(enumeration)) {
        SolrGroupRefsDocMapper groupRefDocsMapper
            = new SolrGroupRefsDocMapper(document);
        document = groupRefDocsMapper.from(
            Collections.list(enumeration));
      }
    }

    SolrAuditableDocMapper<AppUser> auditableDocMapper
        = new SolrAuditableDocMapper<>(document);
    return auditableDocMapper.from(source);
  }
}