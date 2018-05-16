package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.question.service.model.QuestionComponentModel;

import java.util.ArrayList;
import java.util.List;

public final class QuestionComponentModelsMapper
    implements Mapper<List<QuestionComponent>, List<QuestionComponentModel>> {

  private final QuestionModelMapper questionMapper;

  public QuestionComponentModelsMapper() {
    this.questionMapper = new QuestionModelMapper();
  }

  @Override
  public List<QuestionComponentModel> from(List<QuestionComponent> source) {
    List<QuestionComponentModel> componentModels = new ArrayList<>();

    for (QuestionComponent questionComponent : source) {
      QuestionComponentModel componentModel = new QuestionComponentModel();
      componentModel.setId(questionComponent.getId());

      Question question = questionComponent.getQuestion();
      componentModel.setQuestion(questionMapper.from(question));
      componentModel.setScore(questionComponent.getScore());
      componentModel.setOrder(questionComponent.getOrder());
      componentModels.add(componentModel);
    }

    return componentModels;
  }
}