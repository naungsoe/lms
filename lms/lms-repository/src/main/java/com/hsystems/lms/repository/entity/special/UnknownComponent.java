package com.hsystems.lms.repository.entity.special;

import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.ComponentType;

/**
 * Created by administrator on 24/5/17.
 */
public class UnknownComponent implements Component {

  public UnknownComponent() {

  }

  @Override
  public String getId() {
    return "";
  }

  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public ComponentType getType() {
    return ComponentType.UNKNOWN;
  }
}
