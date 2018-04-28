package com.hsystems.lms.entity.component.special;

import com.hsystems.lms.entity.component.Component;

import java.io.Serializable;

/**
 * Created by administrator on 24/5/17.
 */
public final class UnknownComponent implements Component, Serializable {

  private static final long serialVersionUID = -5050719080303793799L;

  public UnknownComponent() {

  }

  @Override
  public int getOrder() {
    return 0;
  }
}