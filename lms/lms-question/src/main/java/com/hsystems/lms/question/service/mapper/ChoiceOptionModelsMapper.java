package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.service.model.ChoiceOptionModel;

import java.util.ArrayList;
import java.util.List;

public final class ChoiceOptionModelsMapper
    implements Mapper<List<ChoiceOption>, List<ChoiceOptionModel>> {

  public ChoiceOptionModelsMapper() {

  }

  @Override
  public List<ChoiceOptionModel> from(List<ChoiceOption> source) {
    List<ChoiceOptionModel> optionModels = new ArrayList<>();

    for (ChoiceOption option : source) {
      ChoiceOptionModel optionModel = new ChoiceOptionModel();
      optionModel.setId(option.getId());
      optionModel.setBody(option.getBody());
      optionModel.setFeedback(option.getFeedback());
      optionModel.setCorrect(option.isCorrect());
      optionModel.setOrder(option.getOrder());
      optionModels.add(optionModel);
    }

    return optionModels;
  }
}