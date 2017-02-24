package com.hsystems.lms.repository.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 2/11/16.
 */
public class SignInLog implements Entity, Serializable {

  private static final long serialVersionUID = 6681526004482435421L;

  private String id;

  private String sessionId;

  private String ipAddress;

  private LocalDateTime dateTime;

  SignInLog() {

  }

  public SignInLog(
      String id,
      String sessionId,
      String ipAddress,
      LocalDateTime dateTime) {

    this.id = id;
    this.sessionId = sessionId;
    this.ipAddress = ipAddress;
    this.dateTime = dateTime;
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

  @Override
  public int hashCode() {
    int prime = 31;
    int result = id.hashCode();
    result = result * prime + sessionId.hashCode();
    result = result * prime + ipAddress.hashCode();
    return result * prime + dateTime.hashCode();
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
        && dateTime.equals(signInLog.getDateTime());
  }

  @Override
  public String toString() {
    return String.format(
        "SignInLog{id=%s, sessionId=%s, ipAddress=%s, dateTime=%s}",
        id, sessionId, ipAddress, dateTime);
  }
}
