package com.hsystems.lms.repository;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 13/10/16.
 */
public class Constants {

  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public static final String FIELD_ID = "id";
  public static final String FIELD_PASSWORD = "password";
  public static final String FIELD_SALT = "salt";
  public static final String FIELD_NAME = "name";
  public static final String FIELD_FIRST_NAME = "firstName";
  public static final String FIELD_LAST_NAME = "lastName";
  public static final String FIELD_DATE_OF_BIRTH = "dateOfBirth";
  public static final String FIELD_GENDER = "gender";
  public static final String FIELD_MOBILE = "mobile";
  public static final String FIELD_EMAIL = "email";
  public static final String FIELD_LOCALE = "locale";
  public static final String FIELD_PERMISSIONS = "permissions";
  public static final String FIELD_SCHOOL = "school";
  public static final String FIELD_GROUPS = "groups";
  public static final String FIELD_OWNER = "owner";
  public static final String FIELD_USER = "user";
  public static final String FIELD_CREATED_BY = "createdBy";
  public static final String FIELD_CREATED_DATE_TIME = "createdDateTime";
  public static final String FIELD_MODIFIED_BY = "modifiedBy";
  public static final String FIELD_MODIFIED_DATE_TIME = "modifiedDateTime";

  public static final String FIELD_TYPE = "type";
  public static final String FIELD_BODY = "body";
  public static final String FIELD_HINT = "hint";
  public static final String FIELD_EXPLANATION = "hint";
  public static final String FIELD_OPTIONS = "options";
  public static final String FIELD_FEEDBACK = "feedback";
  public static final String FIELD_CORRECT = "correct";
  public static final String FIELD_TIMESTAMP = "timestamp";
  public static final String FIELD_ACTION = "action";

  public static final String TABLE_SCHOOLS = "schools";
  public static final String TABLE_GROUPS = "groups";
  public static final String TABLE_USERS = "users";
  public static final String TABLE_QUESTIONS = "questions";
  public static final String TABLE_AUDIT_LOG = "auditlog";

  public static final byte[] FAMILY_DATA = Bytes.toBytes("d");

  public static final byte[] IDENTIFIER_DATE_TIME = Bytes.toBytes("datetime");
  public static final byte[] IDENTIFIER_ID = Bytes.toBytes(FIELD_ID);
  public static final byte[] IDENTIFIER_PASSWORD = Bytes.toBytes(FIELD_PASSWORD);
  public static final byte[] IDENTIFIER_SALT = Bytes.toBytes(FIELD_SALT);
  public static final byte[] IDENTIFIER_NAME = Bytes.toBytes(FIELD_NAME);
  public static final byte[] IDENTIFIER_FIRST_NAME = Bytes.toBytes("fname");
  public static final byte[] IDENTIFIER_LAST_NAME = Bytes.toBytes("lname");
  public static final byte[] IDENTIFIER_DATE_OF_BIRTH = Bytes.toBytes("dob");
  public static final byte[] IDENTIFIER_GENDER = Bytes.toBytes(FIELD_GENDER);
  public static final byte[] IDENTIFIER_MOBILE = Bytes.toBytes(FIELD_MOBILE);
  public static final byte[] IDENTIFIER_EMAIL = Bytes.toBytes(FIELD_EMAIL);
  public static final byte[] IDENTIFIER_LOCALE = Bytes.toBytes(FIELD_LOCALE);
  public static final byte[] IDENTIFIER_PERMISSIONS = Bytes.toBytes(FIELD_PERMISSIONS);
  public static final byte[] IDENTIFIER_TYPE = Bytes.toBytes(FIELD_TYPE);
  public static final byte[] IDENTIFIER_BODY = Bytes.toBytes(FIELD_BODY);
  public static final byte[] IDENTIFIER_HINT = Bytes.toBytes(FIELD_HINT);
  public static final byte[] IDENTIFIER_EXPLANATION = Bytes.toBytes(FIELD_EXPLANATION);
  public static final byte[] IDENTIFIER_FEEDBACK = Bytes.toBytes(FIELD_FEEDBACK);
  public static final byte[] IDENTIFIER_CORRECT = Bytes.toBytes(FIELD_CORRECT);
  public static final byte[] IDENTIFIER_TIMESTAMP = Bytes.toBytes(FIELD_TIMESTAMP);
  public static final byte[] IDENTIFIER_ACTION = Bytes.toBytes(FIELD_ACTION);

  public static final String SEPARATOR = "_";
  public static final String SEPARATOR_SCHOOL = "_sch_";
  public static final String SEPARATOR_GROUP = "_grp_";
  public static final String SEPARATOR_USER = "_usr_";
  public static final String SEPARATOR_CREATED_BY = "_cre_";
  public static final String SEPARATOR_MODIFIED_BY = "_mod_";
  public static final String SEPARATOR_OPTION = "_opt_";
}
