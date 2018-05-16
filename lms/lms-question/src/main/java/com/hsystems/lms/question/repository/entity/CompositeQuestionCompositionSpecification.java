package com.hsystems.lms.question.repository.entity;

import com.hsystems.lms.common.specification.Specification;

public final class CompositeQuestionCompositionSpecification
    implements Specification<QuestionComponent> {

  @Override
  public boolean isSatisfiedBy(QuestionComponent candidate) {
    Question question = candidate.getQuestion();
    boolean isCompositeQuestion = question instanceof CompositeQuestion;
    return !isCompositeQuestion;
  }
}