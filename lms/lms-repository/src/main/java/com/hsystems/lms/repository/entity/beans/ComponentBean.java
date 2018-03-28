package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.repository.entity.Component;
import com.hsystems.lms.repository.entity.Entity;

public interface ComponentBean<T extends Component> extends Entity {

  int getOrder();

  String getResourceId();

  String getParentId();

  T getComponent();
}
