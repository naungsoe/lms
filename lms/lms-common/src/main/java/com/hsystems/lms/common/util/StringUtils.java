package com.hsystems.lms.common.util;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by naungsoe on 12/9/16.
 */
public class StringUtils {

  public static boolean isEmpty(String value) {
    return (value == null) || (value.length() == 0);
  }

  public static boolean isNotEmpty(String value) {
    return !isEmpty(value);
  }

  public static String uncapitalize(String value) {
    if (isEmpty(value)) {
      return value;
    }

    int length = value.length();
    char firstChar = value.charAt(0);
    return Character.isLowerCase(firstChar)
        ? value : (new StringBuilder(length))
        .append(Character.toLowerCase(firstChar))
        .append(value.substring(1)).toString();
  }

  public static <T> String join(List<T> items, String separator) {
    if (CollectionUtils.isEmpty(items)) {
      return "";
    }

    StringBuilder builder = new StringBuilder();
    IntStream.range(0, items.size()).forEach(i -> {
      builder.append(items.get(i));

      if ((i + 1) < items.size()) {
        builder.append(separator);
      }
    });

    return builder.toString();
  }

  public static <T> String prepend(List<T> items, String prefix) {
    if (CollectionUtils.isEmpty(items)) {
      return "";
    }

    StringBuilder builder = new StringBuilder();
    IntStream.range(0, items.size()).forEach(i -> {
      builder.append(prefix);
      builder.append(items.get(i));
    });

    return builder.toString();
  }
}
