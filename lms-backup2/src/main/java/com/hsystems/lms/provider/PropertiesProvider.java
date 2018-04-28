package com.hsystems.lms.provider;

import com.google.inject.Provider;

import com.hsystems.lms.service.interceptor.LogInterceptor;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by naungsoe on 19/9/16.
 */
public class PropertiesProvider implements Provider<Properties> {

  private final static Logger logger
      = Logger.getLogger(LogInterceptor.class);

  private static final String fileName = "application.properties";

  public Properties get() {
    Properties properties = new Properties();
    InputStream inputStream = getClass().getClassLoader()
        .getResourceAsStream(fileName);

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
