package com.hsystems.lms.common;

import java.util.UUID;

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

  public static String getUniqueKey() {
    String uuid = UUID.randomUUID().toString();
    return SecurityUtils.getMD5Hash(uuid, null);
  }
}
