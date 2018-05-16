package com.hsystems.lms.user.repository.solr;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.repository.solr.SolrGroupRefsMapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrPreferencesMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.repository.entity.Credentials;
import com.hsystems.lms.user.repository.entity.SchoolUser;

import org.apache.solr.common.SolrDocument;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public final class SolrUserMapper
    implements Mapper<SolrDocument, Auditable<AppUser>> {

  private static final String ID_FIELD = "id";
  private static final String FIRST_NAME_FIELD = "firstName";
  private static final String LAST_NAME_FIELD = "lastName";
  private static final String DATE_OF_BIRTH_FIELD = "dateOfBirth";
  private static final String GENDER_FIELD = "gender";
  private static final String MOBILE_FIELD = "mobile";
  private static final String EMAIL_FIELD = "email";
  private static final String PERMISSIONS_FIELD = "permissions";
  private static final String MFA_ENABLED_FIELD = "mfaEnabled";

  private final SolrPreferencesMapper preferencesMapper;

  private final SolrCredentialsMapper credentialsMapper;

  public SolrUserMapper() {
    this.preferencesMapper = new SolrPreferencesMapper();
    this.credentialsMapper = new SolrCredentialsMapper();
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
    List<String> permissions
        = SolrUtils.getStrings(source, PERMISSIONS_FIELD);
    Preferences preferences = preferencesMapper.from(source);
    Credentials credentials = credentialsMapper.from(source);
    boolean mfaEnabled = SolrUtils.getBoolean(source, MFA_ENABLED_FIELD);
    SchoolUser.Builder builder
        = new SchoolUser.Builder(id, firstName, lastName)
        .dateOfBirth(dateOfBirth)
        .gender(gender)
        .mobile(mobile)
        .email(email)
        .permissions(permissions)
        .preferences(preferences)
        .credentials(credentials)
        .mfaEnabled(mfaEnabled);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      builder.school(school);
    }

    SolrGroupRefsMapper groupRefsMapper = new SolrGroupRefsMapper(id);
    List<Group> groups = groupRefsMapper.from(source);

    if (CollectionUtils.isNotEmpty(groups)) {
      builder.groups(groups);
    }

    SchoolUser user = builder.build();
    SolrAuditableMapper<AppUser> auditableMapper
        = new SolrAuditableMapper<>(user, id);
    return auditableMapper.from(source);
  }
}