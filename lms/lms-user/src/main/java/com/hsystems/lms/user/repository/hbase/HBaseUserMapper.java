package com.hsystems.lms.user.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.hbase.HBaseGroupRefsMapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBasePreferencesMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;
import com.hsystems.lms.user.repository.entity.AppUser;
import com.hsystems.lms.user.repository.entity.Credentials;
import com.hsystems.lms.user.repository.entity.SchoolUser;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseUserMapper
    implements Mapper<Result, Auditable<AppUser>> {

  private static final byte[] FIRST_NAME_QUALIFIER = Bytes.toBytes("fname");
  private static final byte[] LAST_NAME_QUALIFIER = Bytes.toBytes("lname");
  private static final byte[] DATE_OF_BIRTH_QUALIFIER = Bytes.toBytes("dob");
  private static final byte[] GENDER_QUALIFIER = Bytes.toBytes("gender");
  private static final byte[] MOBILE_QUALIFIER = Bytes.toBytes("mobile");
  private static final byte[] EMAIL_QUALIFIER = Bytes.toBytes("email");
  private static final byte[] PERMISSIONS_QUALIFIER
      = Bytes.toBytes("permissions");
  private static final byte[] MFA_ENABLED_QUALIFIER = Bytes.toBytes("mfa");

  private final HBasePreferencesMapper preferencesMapper;

  private final HBaseCredentialsMapper credentialsMapper;

  public HBaseUserMapper() {
    this.preferencesMapper = new HBasePreferencesMapper();
    this.credentialsMapper = new HBaseCredentialsMapper();
  }

  @Override
  public Auditable<AppUser> from(Result source) {
    String id = Bytes.toString(source.getRow());
    String firstName = HBaseUtils.getString(source, FIRST_NAME_QUALIFIER);
    String lastName = HBaseUtils.getString(source, LAST_NAME_QUALIFIER);
    LocalDateTime dateOfBirth = HBaseUtils.getDateTime(
        source, DATE_OF_BIRTH_QUALIFIER);
    String gender = HBaseUtils.getString(source, GENDER_QUALIFIER);
    String mobile = HBaseUtils.getString(source, MOBILE_QUALIFIER);
    String email = HBaseUtils.getString(source, EMAIL_QUALIFIER);
    List<String> permissions = HBaseUtils.getStrings(
        source, PERMISSIONS_QUALIFIER);
    Preferences preferences = preferencesMapper.from(source);
    Credentials credentials = credentialsMapper.from(source);
    boolean mfaEnabled = HBaseUtils.getBoolean(source, MFA_ENABLED_QUALIFIER);
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

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper();
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      builder.school(schoolOptional.get());
    }

    HBaseGroupRefsMapper groupRefsMapper = new HBaseGroupRefsMapper();
    builder.groups(groupRefsMapper.from(source));

    SchoolUser user = builder.build();
    HBaseAuditableMapper<AppUser> auditableMapper
        = new HBaseAuditableMapper<>(user);
    return auditableMapper.from(source);
  }
}