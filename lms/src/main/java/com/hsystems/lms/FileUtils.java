package com.hsystems.lms;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by administrator on 25/8/16.
 */
public final class FileUtils {

  public static String readContent(String path)
      throws FileNotFoundException, IOException {

    String content = "";
    File file = new File(path);
    FileInputStream fileInputStream = null;

    try {
      fileInputStream = new FileInputStream(file);
      content = IOUtils.toString(fileInputStream);
    } finally {
      if (fileInputStream != null) {
        fileInputStream.close();
      }
    }
    return content;
  }
}
