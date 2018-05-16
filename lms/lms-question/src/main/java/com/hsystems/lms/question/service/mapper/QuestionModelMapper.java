package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.MultipleChoice;
import com.hsystems.lms.question.repository.entity.MultipleResponse;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.service.model.QuestionModel;
import com.hsystems.lms.question.service.model.special.UnknownQuestionModel;

public final class QuestionModelMapper
    implements Mapper<Question, QuestionModel> {

  private final MultipleChoiceModelMapper multipleChoiceMapper;

  private final MultipleResponseModelMapper multipleResponseMapper;

  private final CompositeQuestionModelMapper compositeQuestionMapper;

  public QuestionModelMapper() {
    this.multipleChoiceMapper = new MultipleChoiceModelMapper();
    this.multipleResponseMapper = new MultipleResponseModelMapper();
    this.compositeQuestionMapper = new CompositeQuestionModelMapper();
  }

  @Override
  public QuestionModel from(Question source) {
    if (source instanceof MultipleChoice) {
      return multipleChoiceMapper.from((MultipleChoice) source);

    } else if (source instanceof MultipleResponse) {
      return multipleResponseMapper.from((MultipleResponse) source);

    } else if (source instanceof CompositeQuestion) {
      return compositeQuestionMapper.from((CompositeQuestion) source);
    }

    return new UnknownQuestionModel();
  }
}