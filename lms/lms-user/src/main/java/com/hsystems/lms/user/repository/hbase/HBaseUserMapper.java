package com.hsystems.lms.user.repository.hbase;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
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
    implements Mapper<List<Result>, Auditable<AppUser>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] FIRST_NAME_QUALIFIER = Bytes.toBytes("fname");
  private static final byte[] LAST_NAME_QUALIFIER = Bytes.toBytes("lname");
  private static final byte[] DATE_OF_BIRTH_QUALIFIER = Bytes.toBytes("dob");
  private static final byte[] GENDER_QUALIFIER = Bytes.toBytes("gender");
  private static final byte[] MOBILE_QUALIFIER = Bytes.toBytes("mobile");
  private static final byte[] EMAIL_QUALIFIER = Bytes.toBytes("email");
  private static final byte[] PERMISSIONS_QUALIFIER
      = Bytes.toBytes("permissions");
  private static final byte[] OTP_ENABLED_QUALIFIER = Bytes.toBytes("otp");

  private final HBasePreferencesMapper preferencesMapper;

  private final HBaseCredentialsMapper credentialsMapper;

  public HBaseUserMapper() {
    this.preferencesMapper = new HBasePreferencesMapper();
    this.credentialsMapper = new HBaseCredentialsMapper();
  }

  @Override
  public Auditable<AppUser> from(List<Result> source) {
    Optional<Result> mainResultOptional = source.stream()
        .filter(HBaseUtils.isMainResult()).findFirst();

    if (!mainResultOptional.isPresent()) {
      throw new IllegalArgumentException(
          "there is no main result found");
    }

    Result mainResult = mainResultOptional.get();
    String id = Bytes.toString(mainResult.getRow());
    String firstName = HBaseUtils.getString(
        mainResult, DATA_FAMILY, FIRST_NAME_QUALIFIER);
    String lastName = HBaseUtils.getString(
        mainResult, DATA_FAMILY, LAST_NAME_QUALIFIER);
    LocalDateTime dateOfBirth = HBaseUtils.getDateTime(
        mainResult, DATA_FAMILY, DATE_OF_BIRTH_QUALIFIER);
    String gender = HBaseUtils.getString(
        mainResult, DATA_FAMILY, GENDER_QUALIFIER);
    String mobile = HBaseUtils.getString(
        mainResult, DATA_FAMILY, MOBILE_QUALIFIER);
    String email = HBaseUtils.getString(
        mainResult, DATA_FAMILY, EMAIL_QUALIFIER);
    List<String> permissions = HBaseUtils.getStrings(
        mainResult, DATA_FAMILY, PERMISSIONS_QUALIFIER);
    Preferences preferences = preferencesMapper.from(mainResult);
    Credentials credentials = credentialsMapper.from(mainResult);
    boolean otpEnabled = HBaseUtils.getBoolean(
        mainResult, DATA_FAMILY, OTP_ENABLED_QUALIFIER);
    SchoolUser.Builder builder
        = new SchoolUser.Builder(id, firstName, lastName)
        .dateOfBirth(dateOfBirth)
        .gender(gender)
        .mobile(mobile)
        .email(email)
        .permissions(permissions)
        .preferences(preferences)
        .credentials(credentials)
        .mfaEnabled(otpEnabled);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      builder.school(school);
    }

    HBaseGroupRefsMapper groupRefsMapper = new HBaseGroupRefsMapper(id);
    List<Group> groups = groupRefsMapper.from(source);

    if (CollectionUtils.isNotEmpty(groups)) {
      builder.groups(groups);
    }

    SchoolUser user = builder.build();
    HBaseAuditableMapper<AppUser> auditableMapper
        = new HBaseAuditableMapper<>(user, id);
    return auditableMapper.from(source);
  }
}