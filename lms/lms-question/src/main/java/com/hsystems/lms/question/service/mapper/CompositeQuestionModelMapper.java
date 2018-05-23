package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.question.service.model.CompositeQuestionModel;
import com.hsystems.lms.question.service.model.QuestionComponentModel;
import com.hsystems.lms.question.service.model.QuestionType;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class CompositeQuestionModelMapper
    extends QuestionModelMapper<CompositeQuestion> {

  private final QuestionComponentModelMapper componentMapper;

  public CompositeQuestionModelMapper() {
    this.componentMapper = new QuestionComponentModelMapper();
  }

  @Override
  public CompositeQuestionModel from(CompositeQuestion source) {
    CompositeQuestionModel questionModel = new CompositeQuestionModel();
    questionModel.setType(QuestionType.COMPOSITE);
    questionModel.setBody(source.getBody());
    questionModel.setHint(source.getHint());
    questionModel.setExplanation(source.getExplanation());

    List<QuestionComponentModel> componentModels = new ArrayList<>();
    Enumeration<QuestionComponent> enumeration = source.getComponents();

    while (enumeration.hasMoreElements()) {
      QuestionComponent element = enumeration.nextElement();
      componentModels.add(componentMapper.from(element));
    }

    questionModel.setComponents(componentModels);
    return questionModel;
  }
}