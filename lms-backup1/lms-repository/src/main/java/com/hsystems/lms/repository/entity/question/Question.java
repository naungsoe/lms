package com.hsystems.lms.repository.entity.question;

import java.io.Serializable;

/**
 * Created by naungsoe on 7/10/16.
 */
public interface Question extends Serializable {

  String getBody();

  String getHint();

  String getExplanation();
}