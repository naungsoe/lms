package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.ResourceAttempt;
import com.hsystems.lms.repository.entity.User;

import java.io.Serializable;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(namespace = "lms", name = "attempts")
public abstract class MultipleChoiceAttempt
    extends ResourceAttempt implements Serializable {

  @IndexField
  protected long score;
}