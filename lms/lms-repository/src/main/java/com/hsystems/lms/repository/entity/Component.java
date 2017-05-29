package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;

/**
 * Created by naungsoe on 6/1/17.
 */
@IndexCollection(name = "components")
public interface Component extends Entity {

  int getOrder();

  ComponentType getType();
}
