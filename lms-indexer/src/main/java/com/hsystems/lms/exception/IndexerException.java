package com.hsystems.lms.exception;

/**
 * Created by administrator on 3/10/16.
 */
public class IndexerException extends Exception {

  public IndexerException(String message) {
    super((message));
  }

  public IndexerException(String message, Throwable cause) {
    super(message, cause);
  }
}
