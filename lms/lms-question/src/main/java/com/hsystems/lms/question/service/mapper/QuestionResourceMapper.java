package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.question.service.model.QuestionModel;
import com.hsystems.lms.question.service.model.QuestionResourceModel;
import com.hsystems.lms.user.repository.entity.SchoolUser;

public final class QuestionResourceMapper {

  private SchoolUser schoolUser;

  private QuestionMapper questionMapper;

  QuestionResourceMapper() {

  }

  public QuestionResourceMapper(SchoolUser schoolUser) {
    this.schoolUser = schoolUser;
    this.questionMapper = new QuestionMapper();
  }

  public QuestionResource map(QuestionResourceModel model) {
    Question question = questionMapper.map(model.getQuestion());
    QuestionResource resource
        = new QuestionResource.Builder(model.getId(), question)
        .school()
        .build();
    return resource;
  }

  class QuestionMapper {

    public QuestionMapper() {

    }

    public Question map(QuestionModel model) {
      Question question = new Question() {
        @Override
        public String getBody() {
          return null;
        }

        @Override
        public String getHint() {
          return null;
        }

        @Override
        public String getExplanation() {
          return null;
        }
      };
      return question;
    }
  }
}
