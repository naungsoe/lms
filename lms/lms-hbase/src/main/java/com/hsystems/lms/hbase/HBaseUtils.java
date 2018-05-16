package com.hsystems.lms.hbase;

import com.hsystems.lms.common.util.DateTimeUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class HBaseUtils {

  private static final String PREFIXED_KEY_FORMAT = "%s([A-Za-z0-9]*)$";

  private static final String KEY_SEPARATOR = "_";
  private static final String VALUE_SEPARATOR = ",";

  public static void forEachRowSetResults(
      List<Result> results, Consumer<List<Result>> action) {

    results.stream().filter(isMainResult())
        .forEach(result -> {
          List<Result> rowSetResults = new ArrayList<>();
          rowSetResults.add(result);

          String rowKey = Bytes.toString(result.getRow());
          results.stream().filter(isChildResult(rowKey))
              .forEach(rowResult -> rowSetResults.add(rowResult));

          action.accept(rowSetResults);
        });
  }

  public static Predicate<Result> isMainResult() {
    return p -> !Bytes.toString(p.getRow()).contains(KEY_SEPARATOR);
  }

  public static Predicate<Result> isChildResult(String prefix) {
    String regex = String.format(PREFIXED_KEY_FORMAT, prefix);
    Pattern pattern = Pattern.compile(regex);
    return result -> {
      String rowKey = Bytes.toString(result.getRow());
      return pattern.matcher(rowKey).find();
    };
  }

  public static boolean getBoolean(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return Boolean.parseBoolean(value);
  }

  public static int getInteger(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return Integer.parseInt(value);
  }

  public static long getLong(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return Long.parseLong(value);
  }

  public static List<String> getStrings(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    String[] items = value.split(VALUE_SEPARATOR);
    return Arrays.asList(items);
  }

  public static String getString(
      Result result, byte[] family, byte[] qualifier) {

    byte[] value = result.getValue(family, qualifier);
    return Bytes.toString(value);
  }

  public static LocalDateTime getDateTime(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return DateTimeUtils.toLocalDateTime(value);
  }
}