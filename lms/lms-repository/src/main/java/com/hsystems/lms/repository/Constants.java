package com.hsystems.lms.repository;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 13/10/16.
 */
public class Constants {

  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public static final String TABLE_SCHOOLS = "schools";
  public static final String TABLE_GROUPS = "groups";
  public static final String TABLE_USERS = "users";
  public static final String TABLE_QUIZZES = "quizzes";
  public static final String TABLE_QUESTIONS = "questions";
  public static final String TABLE_MUTATE_LOGS = "mutatelogs";
  public static final String TABLE_REFERENCE_LOGS = "referencelogs";
  public static final String TABLE_SHARE_LOGS = "sharelogs";
  public static final String TABLE_AUDIT_LOGS = "auditlogs";

  public static final byte[] FAMILY_DATA = Bytes.toBytes("d");

  public static final byte[] IDENTIFIER_DATE_TIME = Bytes.toBytes("datetime");
  public static final byte[] IDENTIFIER_ID = Bytes.toBytes("id");
  public static final byte[] IDENTIFIER_SIGNIN_ID = Bytes.toBytes("signinid");
  public static final byte[] IDENTIFIER_PASSWORD = Bytes.toBytes("password");
  public static final byte[] IDENTIFIER_SALT = Bytes.toBytes("salt");
  public static final byte[] IDENTIFIER_NAME = Bytes.toBytes("name");
  public static final byte[] IDENTIFIER_FIRST_NAME = Bytes.toBytes("fname");
  public static final byte[] IDENTIFIER_LAST_NAME = Bytes.toBytes("lname");
  public static final byte[] IDENTIFIER_DATE_OF_BIRTH = Bytes.toBytes("dob");
  public static final byte[] IDENTIFIER_GENDER = Bytes.toBytes("gender");
  public static final byte[] IDENTIFIER_MOBILE = Bytes.toBytes("mobile");
  public static final byte[] IDENTIFIER_EMAIL = Bytes.toBytes("email");
  public static final byte[] IDENTIFIER_LOCALE = Bytes.toBytes("locale");
  public static final byte[] IDENTIFIER_DATE_FORMAT = Bytes.toBytes("dateFormat");
  public static final byte[] IDENTIFIER_DATE_TIME_FORMAT = Bytes.toBytes("dateTimeFormat");
  public static final byte[] IDENTIFIER_PERMISSION = Bytes.toBytes("permission");
  public static final byte[] IDENTIFIER_PERMISSIONS = Bytes.toBytes("permissions");
  public static final byte[] IDENTIFIER_TYPE = Bytes.toBytes("type");
  public static final byte[] IDENTIFIER_IP_ADDRESS = Bytes.toBytes("ip");
  public static final byte[] IDENTIFIER_TITLE = Bytes.toBytes("title");
  public static final byte[] IDENTIFIER_INSTRUCTIONS = Bytes.toBytes("instructions");
  public static final byte[] IDENTIFIER_BODY = Bytes.toBytes("body");
  public static final byte[] IDENTIFIER_HINT = Bytes.toBytes("hint");
  public static final byte[] IDENTIFIER_EXPLANATION = Bytes.toBytes("explanation");
  public static final byte[] IDENTIFIER_FEEDBACK = Bytes.toBytes("feedback");
  public static final byte[] IDENTIFIER_CORRECT = Bytes.toBytes("correct");
  public static final byte[] IDENTIFIER_ORDER = Bytes.toBytes("order");
  public static final byte[] IDENTIFIER_STATUS = Bytes.toBytes("status");
  public static final byte[] IDENTIFIER_TIMESTAMP = Bytes.toBytes("timestamp");
  public static final byte[] IDENTIFIER_ACTION = Bytes.toBytes("action");

  public static final String SEPARATOR = "_";
  public static final String SEPARATOR_SCHOOL = "_sch_";
  public static final String SEPARATOR_GROUP = "_grp_";
  public static final String SEPARATOR_CREATED_BY = "_cre_";
  public static final String SEPARATOR_MODIFIED_BY = "_mod_";
  public static final String SEPARATOR_MEMBER = "_mem_";
  public static final String SEPARATOR_SECTION = "_sec_";
  public static final String SEPARATOR_QUESTION = "_que_";
  public static final String SEPARATOR_OPTION = "_opt_";
  public static final String SEPARATOR_SHARE = "_share_";
}
