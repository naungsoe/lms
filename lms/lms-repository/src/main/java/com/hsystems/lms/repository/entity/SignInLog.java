package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 2/11/16.
 */
@IndexCollection(name = "signinlogs")
public class SignInLog implements Entity, Serializable {

  private static final long serialVersionUID = 6681526004482435421L;

  private String id;

  private String sessionId;

  private String ipAddress;

  private LocalDateTime dateTime;

  private int fails;

  SignInLog() {

  }

  public SignInLog(
      String id,
      String sessionId,
      String ipAddress,
      LocalDateTime dateTime,
      int fails) {

    this.id = id;
    this.sessionId = sessionId;
    this.ipAddress = ipAddress;
    this.dateTime = dateTime;
    this.fails = fails;
  }

  @Override
  public String getId() {
    return id;
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

  public int getFails() {
    return fails;
  }

  @Override
  public int hashCode() {
    int prime = 31;
    int result = id.hashCode();
    result = result * prime + sessionId.hashCode();
    result = result * prime + ipAddress.hashCode();
    result = result * prime + dateTime.hashCode();
    return result * prime + Integer.hashCode(fails);
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    SignInLog signInLog = (SignInLog) obj;

    return id.equals(signInLog.getId())
        && sessionId.equals(signInLog.getSessionId())
        && ipAddress.equals(signInLog.getIpAddress())
        && (fails == signInLog.getFails())
        && dateTime.equals(signInLog.getDateTime());
  }

  @Override
  public String toString() {
    return String.format(
        "SignInLog{id=%s, sessionId=%s, ipAddress=%s, fails=%s, dateTime=%s}",
        id, sessionId, ipAddress, fails, dateTime);
  }
}
