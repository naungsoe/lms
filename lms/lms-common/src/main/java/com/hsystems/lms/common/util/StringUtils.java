package com.hsystems.lms.common.util;

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
}
