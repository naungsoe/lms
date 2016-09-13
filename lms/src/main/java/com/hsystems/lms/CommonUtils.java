package com.hsystems.lms;

/**
 * Created by administrator on 12/9/16.
 */
public class CommonUtils {

  public static void checkArgument(
      boolean expression, String errorMessage)
      throws IllegalArgumentException {

    if (!expression) {
      throw new IllegalArgumentException(errorMessage);
    }
  }

  public static <T> void checkNotNull(
      T value, String errorMessage)
      throws IllegalArgumentException {

    if (value == null) {
      throw new IllegalArgumentException(errorMessage);
    }
  }
}
