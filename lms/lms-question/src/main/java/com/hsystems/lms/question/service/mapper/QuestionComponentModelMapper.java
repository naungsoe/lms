package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.question.service.model.QuestionComponentModel;

public final class QuestionComponentModelMapper
    implements Mapper<QuestionComponent, QuestionComponentModel> {

  private final QuestionModelMapperFactory questionMapperFactory;

  public QuestionComponentModelMapper() {
    this.questionMapperFactory = new QuestionModelMapperFactory();
  }

  @Override
  public QuestionComponentModel from(QuestionComponent source) {
    QuestionComponentModel componentModel = new QuestionComponentModel();
    componentModel.setId(source.getId());

    Question question = source.getQuestion();
    QuestionModelMapper<Question> questionMapper
        = questionMapperFactory.create(question);
    componentModel.setQuestion(questionMapper.from(question));
    componentModel.setScore(source.getScore());
    return componentModel;
  }
}