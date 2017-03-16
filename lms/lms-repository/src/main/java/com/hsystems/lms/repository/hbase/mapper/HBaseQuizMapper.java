package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.question.Question;
import com.hsystems.lms.repository.entity.question.QuestionOption;
import com.hsystems.lms.repository.entity.question.QuestionType;
import com.hsystems.lms.repository.entity.Quiz;
import com.hsystems.lms.repository.entity.QuizSection;
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
    results.stream().filter(isSectionResult(id))
        .forEach(sectionResult -> {
          String sectionId = getSectionId(sectionResult);
          String sectionInstructions = getInstructions(sectionResult);

          List<Question> questions = new ArrayList<>();
          results.stream().filter(isQuestionResult(sectionId))
              .forEach(questionResult -> {
                String questionId = getQuestionId(questionResult);
                QuestionType questionType
                    = getType(questionResult, QuestionType.class);
                String questionBody = getBody(questionResult);
                String questionHint = getHint(questionResult);
                String questionExplanation = getExplanation(questionResult);

                List<QuestionOption> questionOptions = new ArrayList<>();
                results.stream().filter(isOptionResult(questionId))
                    .forEach(optionResult -> {
                      QuestionOption questionOption
                          = getQuestionOption(optionResult);
                      questionOptions.add(questionOption);
                    });

                Question question = new Question(
                    questionId,
                    questionType,
                    questionBody,
                    questionHint,
                    questionExplanation,
                    questionOptions
                );
                questions.add(question);
              });

          QuizSection section = new QuizSection(
              sectionId,
              sectionInstructions,
              questions
          );
          sections.add(section);
        });

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult);
    LocalDateTime createdDateTime = getDateTime(createdByResult);

    Optional<Result> resultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = resultOptional.isPresent()
        ? getModifiedBy(resultOptional.get()) : null;
    LocalDateTime modifiedDateTime = resultOptional.isPresent()
        ? getDateTime(resultOptional.get()) : null;

    return new Quiz(
        id,
        title,
        instructions,
        sections,
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
