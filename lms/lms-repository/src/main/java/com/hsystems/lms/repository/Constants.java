package com.hsystems.lms.repository;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 13/10/16.
 */
public final class Constants {

  public static final String FORMAT_DATE = "yyyy-MM-dd";
  public static final String FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  public static final byte[] FAMILY_DATA = Bytes.toBytes("d");

  public static final byte[] QUALIFIER_DATE_TIME = Bytes.toBytes("datetime");
  public static final byte[] QUALIFIER_ID = Bytes.toBytes("id");
  public static final byte[] QUALIFIER_ACCOUNT = Bytes.toBytes("account");
  public static final byte[] QUALIFIER_PASSWORD = Bytes.toBytes("password");
  public static final byte[] QUALIFIER_SALT = Bytes.toBytes("salt");
  public static final byte[] QUALIFIER_NAME = Bytes.toBytes("name");
  public static final byte[] QUALIFIER_FIRST_NAME = Bytes.toBytes("fname");
  public static final byte[] QUALIFIER_LAST_NAME = Bytes.toBytes("lname");
  public static final byte[] QUALIFIER_DATE_OF_BIRTH = Bytes.toBytes("dob");
  public static final byte[] QUALIFIER_GENDER = Bytes.toBytes("gender");
  public static final byte[] QUALIFIER_MOBILE = Bytes.toBytes("mobile");
  public static final byte[] QUALIFIER_EMAIL = Bytes.toBytes("email");
  public static final byte[] QUALIFIER_LOCALE = Bytes.toBytes("locale");
  public static final byte[] QUALIFIER_DATE_FORMAT
      = Bytes.toBytes("dateFormat");
  public static final byte[] QUALIFIER_DATE_TIME_FORMAT
      = Bytes.toBytes("dateTimeFormat");
  public static final byte[] QUALIFIER_PERMISSIONS
      = Bytes.toBytes("permissions");
  public static final byte[] QUALIFIER_TYPE = Bytes.toBytes("type");
  public static final byte[] QUALIFIER_QUESTION_TYPE = Bytes.toBytes("qtype");
  public static final byte[] QUALIFIER_SESSION_ID = Bytes.toBytes("session");
  public static final byte[] QUALIFIER_IP_ADDRESS = Bytes.toBytes("ip");
  public static final byte[] QUALIFIER_FAILS = Bytes.toBytes("fails");
  public static final byte[] QUALIFIER_TITLE = Bytes.toBytes("title");
  public static final byte[] QUALIFIER_INSTRUCTIONS
      = Bytes.toBytes("instructions");
  public static final byte[] QUALIFIER_KEYWORDS = Bytes.toBytes("keywords");
  public static final byte[] QUALIFIER_BODY = Bytes.toBytes("body");
  public static final byte[] QUALIFIER_HINT = Bytes.toBytes("hint");
  public static final byte[] QUALIFIER_EXPLANATION
      = Bytes.toBytes("explanation");
  public static final byte[] QUALIFIER_FEEDBACK = Bytes.toBytes("feedback");
  public static final byte[] QUALIFIER_CORRECT = Bytes.toBytes("correct");
  public static final byte[] QUALIFIER_ORDER = Bytes.toBytes("order");
  public static final byte[] QUALIFIER_SCORE = Bytes.toBytes("score");
  public static final byte[] QUALIFIER_STATUS = Bytes.toBytes("status");
  public static final byte[] QUALIFIER_TIMESTAMP = Bytes.toBytes("timestamp");
  public static final byte[] QUALIFIER_ACTION = Bytes.toBytes("action");
  public static final byte[] QUALIFIER_PARENT = Bytes.toBytes("parent");

  public static final byte[] VALUE_TRUE = Bytes.toBytes("true");
  public static final byte[] VALUE_FALSE = Bytes.toBytes("false");

  public static final String SEPARATOR = "_";
  public static final String SEPARATOR_SCHOOL = "_sch_";
  public static final String SEPARATOR_CREATED_BY = "_cre_";
  public static final String SEPARATOR_MODIFIED_BY = "_mod_";
  public static final String SEPARATOR_SHARED_BY = "_sha_";
  public static final String SEPARATOR_LEVEL = "_lvl_";
  public static final String SEPARATOR_SUBJECT = "_sub_";
  public static final String SEPARATOR_GROUP = "_grp_";
  public static final String SEPARATOR_ENROLLMENT = "_enr_";
  public static final String SEPARATOR_LESSON = "_lsn_";
  public static final String SEPARATOR_QUIZ = "_quz_";
  public static final String SEPARATOR_COMPONENT = "_com_";
  public static final String SEPARATOR_OPTION = "_opt_";
  public static final String SEPARATOR_ENTRY = "_ent_";

  public static final String FIELD_ID = "id";
  public static final String FIELD_TYPE_NAME = "typeName";
  public static final String FIELD_SCHOOL_ID = "schoolId";
  public static final String FIELD_PARENT_ID = "parentId";
  public static final String MEMBER_FIELD_NAME = "fieldName";
  public static final String MEMBER_FIELD_TYPE_NAME = "fieldTypeName";

  public static final String FORMAT_FIELD_ID = "%s_%s";
  public static final String FORMAT_FIELD_NAME = "%s.%s";
}
