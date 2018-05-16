package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.question.service.model.CompositeQuestionModel;
import com.hsystems.lms.question.service.model.QuestionType;

import java.util.Collections;
import java.util.List;

public final class CompositeQuestionModelMapper
    implements Mapper<CompositeQuestion, CompositeQuestionModel> {

  private final QuestionComponentModelsMapper componentsMapper;

  public CompositeQuestionModelMapper() {
    this.componentsMapper = new QuestionComponentModelsMapper();
  }

  @Override
  public CompositeQuestionModel from(CompositeQuestion source) {
    CompositeQuestionModel questionModel = new CompositeQuestionModel();
    questionModel.setType(QuestionType.COMPOSITE);
    questionModel.setBody(source.getBody());
    questionModel.setHint(source.getHint());
    questionModel.setExplanation(source.getExplanation());

    List<QuestionComponent> components
        = Collections.list(source.getComponents());
    questionModel.setComponents(componentsMapper.from(components));
    return questionModel;
  }
}