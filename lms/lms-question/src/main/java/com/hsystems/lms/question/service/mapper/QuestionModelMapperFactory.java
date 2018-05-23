package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.MultipleChoice;
import com.hsystems.lms.question.repository.entity.MultipleResponse;
import com.hsystems.lms.question.repository.entity.Question;

public final class QuestionModelMapperFactory {

  public QuestionModelMapperFactory() {

  }

  public QuestionModelMapper create(Question source) {
    if (source instanceof MultipleChoice) {
      return new MultipleChoiceModelMapper();

    } else if (source instanceof MultipleResponse) {
      return new MultipleResponseModelMapper();

    } else if (source instanceof CompositeQuestion) {
      return new CompositeQuestionModelMapper();
    }

    throw new IllegalArgumentException(
        "not supported question type");
  }
}