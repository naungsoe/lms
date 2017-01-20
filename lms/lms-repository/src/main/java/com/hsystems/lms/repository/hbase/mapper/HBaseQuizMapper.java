package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.entity.QuestionOption;
import com.hsystems.lms.repository.entity.QuestionType;
import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.repository.entity.QuizSection;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseQuizMapper extends HBaseMapper<Quiz> {

  @Override
  public Quiz getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    String id = Bytes.toString(mainResult.getRow());
    String title = getTitle(mainResult);
    String instructions = getInstructions(mainResult);

    List<QuizSection> sections = new ArrayList<>();
    results.stream().filter(isSectionResult(id)).forEach(x -> {
      String sectionId = getId(x, Constants.SEPARATOR_SECTION);
      String sectionInstructions = getInstructions(x);

      List<Question> questions = new ArrayList<>();
      results.stream().filter(isQuestionResult(sectionId))
          .forEach(y -> {
            String questionId = getId(y, Constants.SEPARATOR_QUESTION);
            QuestionType questionType = getType(y, QuestionType.class);
            String questionBody = getBody(y);
            String questionHint = getHint(y);
            String questionExplanation = getExplanation(y);

            List<QuestionOption> options = new ArrayList<>();
            results.stream().filter(isOptionResult(questionId))
                .forEach(z -> options.add(getQuestionOption(z)));

            Question question = new Question(questionId, questionType,
                questionBody, questionHint, questionExplanation, options);
            questions.add(question);
          });

      QuizSection section = new QuizSection(
          sectionId, sectionInstructions, questions);
      sections.add(section);
    });

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult);

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult);
    LocalDateTime createdDateTime = getDateTime(createdByResult);

    Optional<Result> modifiedByResultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = modifiedByResultOptional.isPresent()
        ? getModifiedBy(modifiedByResultOptional.get()) : null;
    LocalDateTime modifiedDateTime = modifiedByResultOptional.isPresent()
        ? getDateTime(modifiedByResultOptional.get()) : LocalDateTime.MIN;

    return new Quiz(
        id,
        title,
        instructions,
        sections,
        school,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }

  @Override
  public List<Put> getPuts(Quiz entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(Quiz entity, long timestamp) {
    return new ArrayList<>();
  }
}
