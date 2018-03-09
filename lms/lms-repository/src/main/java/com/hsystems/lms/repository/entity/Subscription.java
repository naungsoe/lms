package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.course.CourseResource;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by naungsoe on 8/8/16.
 */
@IndexDocument(namespace = "lms", collection = "subscriptions")
public final class Subscription implements Entity, Serializable {

  private static final long serialVersionUID = 2408494988662970030L;

  @IndexField
  private String id;

  @IndexField
  private CourseResource resource;

  @IndexField
  private User subscribedBy;

  @IndexField
  private LocalDateTime subscribedDateTime;

  Subscription() {

  }

  public Subscription(
      String id,
      CourseResource resource,
      User subscribedBy,
      LocalDateTime subscribedDateTime) {

    this.id = id;
    this.resource = resource;
    this.subscribedBy =  subscribedBy;
    this.subscribedDateTime = subscribedDateTime;
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

  public LocalDateTime getSubscribedDateTime() {
    return subscribedDateTime;
  }
}