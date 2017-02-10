package com.hsystems.lms.repository.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 2/11/16.
 */
public class SignInLog implements Entity, Serializable {

  private static final long serialVersionUID = 6681526004482435421L;

  private String id;

  private String ipAddress;

  private LocalDateTime dateTime;

  SignInLog() {

  }

  public SignInLog(
      String id,
      String ipAddress,
      LocalDateTime dateTime) {

    this.id = id;
    this.ipAddress = ipAddress;
    this.dateTime = dateTime;
  }

  @Override
  public String getId() { return id; }

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
        && ipAddress.equals(signInLog.getIpAddress())
        && dateTime.equals(signInLog.getDateTime());
  }

  @Override
  public String toString() {
    return String.format(
        "SignInLog{id=%s, ipAddress=%s, dateTime=%s}",
        id, ipAddress, dateTime);
  }
}
