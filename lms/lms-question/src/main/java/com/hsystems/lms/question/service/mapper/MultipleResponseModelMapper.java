package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.repository.entity.MultipleResponse;
import com.hsystems.lms.question.service.model.MultipleResponseModel;
import com.hsystems.lms.question.service.model.QuestionType;

import java.util.Collections;
import java.util.List;

public final class MultipleResponseModelMapper
    implements Mapper<MultipleResponse, MultipleResponseModel> {

  private final ChoiceOptionModelsMapper optionsMapper;

  public MultipleResponseModelMapper() {
    this.optionsMapper = new ChoiceOptionModelsMapper();
  }

  @Override
  public MultipleResponseModel from(MultipleResponse source) {
    MultipleResponseModel questionModel = new MultipleResponseModel();
    questionModel.setType(QuestionType.MULTIPLE_RESPONSE);
    questionModel.setBody(source.getBody());
    questionModel.setHint(source.getHint());
    questionModel.setExplanation(source.getExplanation());

    List<ChoiceOption> options = Collections.list(source.getOptions());
    questionModel.setOptions(optionsMapper.from(options));
    return questionModel;
  }
}