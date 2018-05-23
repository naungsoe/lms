package com.hsystems.lms.course.repository.entity;

import com.hsystems.lms.entity.Entity;
import com.hsystems.lms.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 8/8/16.
 */
public final class CourseSubscription implements Entity, Serializable {

  private static final long serialVersionUID = -5684955774017521750L;

  private String id;

  private CourseResource resource;

  private User subscribedBy;

  private LocalDateTime subscribedOn;

  CourseSubscription() {

  }

  public CourseSubscription(
      String id,
      CourseResource resource,
      User subscribedBy,
      LocalDateTime subscribedOn) {

    this.id = id;
    this.resource = resource;
    this.subscribedBy = subscribedBy;
    this.subscribedOn = subscribedOn;
  }

  @Override
  public String getId() {
    return id;
  }

  public CourseResource getResource() {
    return resource;
  }

  public User getSubscribedBy() {
    return subscribedBy;
  }

  public LocalDateTime getSubscribedOn() {
    return subscribedOn;
  }
}