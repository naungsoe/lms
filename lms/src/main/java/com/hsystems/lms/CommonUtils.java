package com.hsystems.lms;

/**
 * Created by naungsoe on 12/9/16.
 */
public class CommonUtils {

  public static void checkArgument(
      boolean expression, String errorMessage)
      throws IllegalArgumentException {

    if (!expression) {
      throw new IllegalArgumentException(errorMessage);
    }
  }
}
