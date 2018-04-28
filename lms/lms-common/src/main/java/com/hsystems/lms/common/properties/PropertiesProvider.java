package com.hsystems.lms.common.properties;

import com.google.inject.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by naungsoe on 19/9/16.
 */
public final class PropertiesProvider implements Provider<Properties> {

  private static final Logger logger = LogManager.getRootLogger();

  private static final String FILE_NAME = "application.properties";

  private volatile Properties properties;

  PropertiesProvider() {

  }

  public Properties get() {
    Properties instance = properties;

    if (instance == null) {
      synchronized (this) {
        instance = properties;

        if (instance == null) {
          properties = getProperties();
          instance = properties;
        }
      }
    }

    return instance;
  }

  private Properties getProperties() {
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream(FILE_NAME);
    Properties properties = new Properties();

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

    return properties;
  }
}
