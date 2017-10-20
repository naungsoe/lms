package com.hsystems.lms.repository.entity.question;

/**
 * Created by naungsoe on 7/10/16.
 */
public interface Question {

  String getBody();

  String getHint();

  String getExplanation();

  QuestionGradingStrategy getGradingStrategy();
}