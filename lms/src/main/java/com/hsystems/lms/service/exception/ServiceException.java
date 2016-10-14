package com.hsystems.lms.service.exception;

import com.hsystems.lms.exception.ApplicationException;

/**
 * Created by administrator on 13/8/16.
 */
public class ServiceException extends ApplicationException {

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
