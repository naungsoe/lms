package com.hsystems.lms.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by administrator on 2/11/16.
 */
public class AuditLog implements Serializable {

  private static final long serialVersionUID = -3408481188461757227L;

  private User user;

  private LocalDateTime dateTime;

  private Action action;

  AuditLog() {

  }

  public AuditLog(
      User user,
      LocalDateTime dateTime,
      Action action) {

    this.user = user;
    this.dateTime = dateTime;
    this.action = action;
  }

  public User getUser() {
    return user;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public Action getAction() {
    return action;
  }
}
