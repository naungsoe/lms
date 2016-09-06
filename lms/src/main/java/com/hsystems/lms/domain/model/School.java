package com.hsystems.lms.domain.model;

import java.time.LocalDateTime;

/**
 * Created by administrator on 8/8/16.
 */
public class School {

  protected String id;

  protected String name;

  protected School() {

  }

  public School(String id, String name) {
    this.id = id;
    this.name = name;
  }
}
