package com.hsystems.lms.repository.exception;

import com.hsystems.lms.exception.ApplicationException;

/**
 * Created by administrator on 13/8/16.
 */
public class RepositoryException extends ApplicationException {

  public RepositoryException(String message, Throwable cause) {
    super(message, cause);
  }
}
