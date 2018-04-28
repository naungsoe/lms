package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;

/**
 * Created by naungsoe on 6/1/17.
 */
@IndexDocument(namespace = "lms", collection = "components")
public interface Component extends Entity {

  int getOrder();
}