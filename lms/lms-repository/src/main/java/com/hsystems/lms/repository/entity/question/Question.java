package com.hsystems.lms.repository.entity.question;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Resource;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(namespace = "lms", name = "questions")
public abstract class Question extends Resource
    implements Serializable {

  @IndexField
  protected String body;

  @IndexField
  protected String hint;

  @IndexField
  protected String explanation;

  public String getBody() {
    return body;
  }

  public String getHint() {
    return hint;
  }

  public String getExplanation() {
    return explanation;
  }
}
