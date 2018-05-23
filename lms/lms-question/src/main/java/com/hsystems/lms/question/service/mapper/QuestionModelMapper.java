package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.service.model.QuestionModel;

public abstract class QuestionModelMapper<T extends Question>
    implements Mapper<T, QuestionModel> {

}