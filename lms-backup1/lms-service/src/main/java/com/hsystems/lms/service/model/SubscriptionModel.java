package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.service.model.course.CourseResourceModel;

import java.io.Serializable;

/**
 * Created by naungsoe on 8/8/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SubscriptionModel implements Serializable {

  private static final long serialVersionUID = 3851415348177755088L;

  private String id;

  private CourseResourceModel resource;

  private UserModel subscribedBy;

  private String subscribedDate;

  private String subscribedTime;

  private String subscribedDateTime;

  public SubscriptionModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CourseResourceModel getResource() {
    return resource;
  }

  public void setResource(
      CourseResourceModel resource) {
    this.resource = resource;
  }

  public UserModel getSubscribedBy() {
    return subscribedBy;
  }

  public void setSubscribedBy(UserModel subscribedBy) {
    this.subscribedBy = subscribedBy;
  }

  public String getSubscribedDate() {
    return subscribedDate;
  }

  public void setSubscribedDate(String subscribedDate) {
    this.subscribedDate = subscribedDate;
  }

  public String getSubscribedTime() {
    return subscribedTime;
  }

  public void setSubscribedTime(String subscribedTime) {
    this.subscribedTime = subscribedTime;
  }

  public String getSubscribedDateTime() {
    return subscribedDateTime;
  }

  public void setSubscribedDateTime(String subscribedDateTime) {
    this.subscribedDateTime = subscribedDateTime;
  }
}