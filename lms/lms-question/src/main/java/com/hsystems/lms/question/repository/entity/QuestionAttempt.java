package com.hsystems.lms.question.repository.entity;

/**
 * Created by naungsoe on 7/10/16.
 */
public interface QuestionAttempt<T extends Question> {

  T getQuestion();
}