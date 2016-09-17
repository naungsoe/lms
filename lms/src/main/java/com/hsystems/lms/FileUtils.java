package com.hsystems.lms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by administrator on 25/8/16.
 */
public final class FileUtils {

  private static final String UTF8_CHARSET = "UTF-8";

  public static String readContent(String path) throws IOException {
    String content = "";
    File file = new File(path);
    FileInputStream fileInputStream = null;

    try {
      fileInputStream = new FileInputStream(file);
      content = readContent(fileInputStream);
    } finally {
      if (fileInputStream != null) {
        fileInputStream.close();
      }
    }
    return content;
  }

  public static String readContent(FileInputStream fileInputStream)
      throws IOException {

    StringBuilder stringBuilder = new StringBuilder();
    InputStreamReader streamReader = new InputStreamReader(fileInputStream);
    BufferedReader bufferedReader = new BufferedReader(streamReader);
    String line = null;

    try {
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }
    } finally {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
    }
    return stringBuilder.toString();
  }
}
