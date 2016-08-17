package com.hsystems.lms.exception;

/**
 * Created by administrator on 13/8/16.
 */
public class ApplicationException extends Exception {

  public ApplicationException(String message) {
    super((message));
  }

  public ApplicationException(String message, Throwable cause) {
    super(message, cause);
  }
}
