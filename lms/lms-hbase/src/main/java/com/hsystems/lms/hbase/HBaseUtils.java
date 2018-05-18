package com.hsystems.lms.hbase;

import com.hsystems.lms.common.util.DateTimeUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public final class HBaseUtils {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final String VALUE_SEPARATOR = ",";

  public static boolean containsColumn(Result result, byte[] qualifier) {
    return result.containsColumn(DATA_FAMILY, qualifier);
  }

  public static boolean getBoolean(Result result, byte[] qualifier) {
    String value = getString(result, qualifier);
    return Boolean.parseBoolean(value);
  }

  public static int getInteger(Result result, byte[] qualifier) {
    String value = getString(result, qualifier);
    return Integer.parseInt(value);
  }

  public static long getLong(Result result, byte[] qualifier) {
    String value = getString(result, qualifier);
    return Long.parseLong(value);
  }

  public static List<String> getStrings(Result result, byte[] qualifier) {
    String value = getString(result, qualifier);
    String[] items = value.split(VALUE_SEPARATOR);
    return Arrays.asList(items);
  }

  public static String getString(Result result, byte[] qualifier) {
    byte[] value = result.getValue(DATA_FAMILY, qualifier);
    return Bytes.toString(value);
  }

  public static LocalDateTime getDateTime(Result result, byte[] qualifier) {
    String value = getString(result, qualifier);
    return DateTimeUtils.toLocalDateTime(value);
  }
}