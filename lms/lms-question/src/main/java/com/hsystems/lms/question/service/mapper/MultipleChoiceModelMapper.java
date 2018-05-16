package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.repository.entity.MultipleChoice;
import com.hsystems.lms.question.service.model.MultipleChoiceModel;
import com.hsystems.lms.question.service.model.QuestionType;

import java.util.Collections;
import java.util.List;

public final class MultipleChoiceModelMapper
    implements Mapper<MultipleChoice, MultipleChoiceModel> {

  private final ChoiceOptionModelsMapper optionsMapper;

  public MultipleChoiceModelMapper() {
    this.optionsMapper = new ChoiceOptionModelsMapper();
  }

  @Override
  public MultipleChoiceModel from(MultipleChoice source) {
    MultipleChoiceModel questionModel = new MultipleChoiceModel();
    questionModel.setType(QuestionType.MULTIPLE_CHOICE);
    questionModel.setBody(source.getBody());
    questionModel.setHint(source.getHint());
    questionModel.setExplanation(source.getExplanation());

    List<ChoiceOption> options = Collections.list(source.getOptions());
    questionModel.setOptions(optionsMapper.from(options));
    return questionModel;
  }
}