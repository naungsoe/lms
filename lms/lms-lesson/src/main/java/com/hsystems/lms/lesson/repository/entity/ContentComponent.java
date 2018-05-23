package com.hsystems.lms.lesson.repository.entity;


import com.hsystems.lms.component.Component;

import java.io.Serializable;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class ContentComponent implements Component, Serializable {

  private static final long serialVersionUID = 6875653818728737014L;

  protected String id;

  private String content;

  ContentComponent() {

  }

  public ContentComponent(
      String id,
      String content) {

    this.id = id;
    this.content = content;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getContent() {
    return content;
  }
}