package com.hsystems.lms.school.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.entity.Preferences;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBasePreferencesMapper
    implements Mapper<Result, Preferences> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] LOCALE_QUALIFIER = Bytes.toBytes("locale");
  private static final byte[] TIME_FORMAT_QUALIFIER = Bytes.toBytes("tformat");
  private static final byte[] DATE_FORMAT_QUALIFIER = Bytes.toBytes("dformat");
  private static final byte[] DATE_TIME_FORMAT_QUALIFIER
      = Bytes.toBytes("dtformat");

  public HBasePreferencesMapper() {

  }

  @Override
  public Preferences from(Result source) {
    String locale = HBaseUtils.getString(
        source, DATA_FAMILY, LOCALE_QUALIFIER);
    String timeFormat = HBaseUtils.getString(
        source, DATA_FAMILY, TIME_FORMAT_QUALIFIER);
    String dateFormat = HBaseUtils.getString(
        source, DATA_FAMILY, DATE_FORMAT_QUALIFIER);
    String dateTimeFormat = HBaseUtils.getString(
        source, DATA_FAMILY, DATE_TIME_FORMAT_QUALIFIER);
    return new Preferences(locale, timeFormat,
        dateFormat, dateTimeFormat);
  }
}