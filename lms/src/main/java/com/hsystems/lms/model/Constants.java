package com.hsystems.lms.model;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by administrator on 13/10/16.
 */
public class Constants {

  public static final String FIELD_ID = "id";
  public static final String FIELD_PASSWORD = "password";
  public static final String FIELD_NAME = "name";
  public static final String FIELD_FIRST_NAME = "firstName";
  public static final String FIELD_LAST_NAME = "lastName";
  public static final String FIELD_DATE_OF_BIRTH = "dateOfBirth";
  public static final String FIELD_GENDER = "gender";
  public static final String FIELD_MOBILE = "mobile";
  public static final String FIELD_EMAIL = "mobile";
  public static final String FIELD_LOCALE = "locale";
  public static final String FIELD_SCHOOL = "school";
  public static final String FIELD_PERMISSIONS = "permissions";
  public static final String FIELD_GROUPS = "groups";

  public static final String TABLE_SCHOOLS = "schools";
  public static final String TABLE_USERS = "users";

  public static final byte[] FAMILY_DATA = Bytes.toBytes('d');

  public static final byte[] IDENTIFIER_PASSWORD = Bytes.toBytes(FIELD_PASSWORD);
  public static final byte[] IDENTIFIER_NAME = Bytes.toBytes(FIELD_NAME);
  public static final byte[] IDENTIFIER_FIRST_NAME = Bytes.toBytes("fname");
  public static final byte[] IDENTIFIER_LAST_NAME = Bytes.toBytes("lname");
  public static final byte[] IDENTIFIER_DATE_OF_BIRTH = Bytes.toBytes("dob");
  public static final byte[] IDENTIFIER_GENDER = Bytes.toBytes(FIELD_GENDER);
  public static final byte[] IDENTIFIER_MOBILE = Bytes.toBytes(FIELD_MOBILE);
  public static final byte[] IDENTIFIER_EMAIL = Bytes.toBytes(FIELD_EMAIL);
  public static final byte[] IDENTIFIER_LOCALE = Bytes.toBytes(FIELD_LOCALE);
  public static final byte[] IDENTIFIER_PERMISSIONS = Bytes.toBytes(FIELD_PERMISSIONS);
  public static final String SEPARATOR_SCHOOL = "_sch_";
  public static final String SEPARATOR_GROUP = "_grp_";
}
