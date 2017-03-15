package com.hsystems.lms.common.provider;

import com.google.inject.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by naungsoe on 19/9/16.
 */
public class PropertiesProvider implements Provider<Properties> {

  private static final Logger logger = LogManager.getRootLogger();

  private static final String FILE_NAME = "application.properties";

  private final Properties properties;

  PropertiesProvider() {
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream(FILE_NAME);
    properties = new Properties();

    try {
      properties.load(inputStream);

    } catch (IOException e) {
      logger.error("failed to load properties", e);

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();

        } catch (IOException e) {
          logger.error("failed to close stream", e);
        }
      }
    }
  }

  public Properties get() {
    return properties;
  }
}
