package com.hsystems.lms.authentication.repository.entity;

import com.hsystems.lms.entity.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 2/11/16.
 */
public final class SignInLog implements Entity, Serializable {

  private static final long serialVersionUID = -5612365617779627522L;

  private String id;

  private int fails;

  private String sessionId;

  private String ipAddress;

  private LocalDateTime dateTime;

  SignInLog() {

  }

  public SignInLog(
      String id,
      int fails,
      String sessionId,
      String ipAddress,
      LocalDateTime dateTime) {

    this.id = id;
    this.fails = fails;
    this.sessionId = sessionId;
    this.ipAddress = ipAddress;
    this.dateTime = dateTime;
  }

  @Override
  public String getId() {
    return id;
  }

  public int getFails() {
    return fails;
  }

  public String getSessionId() {
    return sessionId;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }
}