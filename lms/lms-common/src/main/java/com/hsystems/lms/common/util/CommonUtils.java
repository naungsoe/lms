package com.hsystems.lms.common.util;

import java.util.UUID;

/**
 * Created by naungsoe on 12/9/16.
 */
public class CommonUtils {

  private static final char[] HEX_CHARS
      = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f'};

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

  public static String getHex(byte[] bytes) {
    int numChars = bytes.length * 2;
    char[] ch = new char[numChars];

    for(int i = 0; i < numChars; i += 2) {
      byte d = bytes[i / 2];
      ch[i] = HEX_CHARS[d >> 4 & 15];
      ch[i + 1] = HEX_CHARS[d & 15];
    }

    return new String(ch);
  }
}
