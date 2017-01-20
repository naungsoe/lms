package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.entity.QuestionOption;
import com.hsystems.lms.repository.entity.QuestionType;
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
public class HBaseQuestionMapper extends HBaseMapper<Question> {

  @Override
  public Question getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    String id = Bytes.toString(mainResult.getRow());
    QuestionType type = getType(mainResult, QuestionType.class);
    String body = getBody(mainResult);
    String hint = getHint(mainResult);
    String explanation = getExplanation(mainResult);

    List<QuestionOption> options = new ArrayList<>();
    results.stream().filter(isOptionResult(id))
        .forEach(x -> options.add(getQuestionOption(x)));

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

    return new Question(
        id,
        type,
        body,
        hint,
        explanation,
        options,
        school,
        createdBy,
        createdDateTime,
        modifiedBy,
        modifiedDateTime
    );
  }


  @Override
  public List<Put> getPuts(Question entity, long timestamp) {
    List<Put> questionPuts = new ArrayList<>();
    Put questionPut = new Put(
        Bytes.toBytes(entity.getId()), timestamp);
    addTypeColumn(questionPut, entity.getType());
    addBodyColumn(questionPut, entity.getBody());
    addHintColumn(questionPut, entity.getHint());
    addExplanationColumn(questionPut, entity.getExplanation());
    questionPuts.add(questionPut);

    entity.getOptions().stream().forEach(x -> {
      addQuestionOptionPut(questionPuts, x, entity.getId(), timestamp);
    });

    addCreatedByPut(questionPuts, entity.getCreatedBy(),
        entity.getId(), timestamp);
    addDateTimeColumn(questionPut, entity.getCreatedDateTime());

    addModifiedByPut(questionPuts, entity.getCreatedBy(),
        entity.getId(), timestamp);
    addDateTimeColumn(questionPut, entity.getModifiedDateTime());
    return questionPuts;
  }

  @Override
  public List<Delete> getDeletes(Question entity, long timestamp) {
    List<Delete> questionDeletes = new ArrayList<>();
    Delete questionDelete = new Delete(Bytes.toBytes(entity.getId()));
    questionDeletes.add(questionDelete);

    entity.getOptions().stream().forEach(x -> {
      String key = String.format("%s%s%s", entity.getId(),
          Constants.SEPARATOR_OPTION, x.getId());
      Delete optionDelete = new Delete(Bytes.toBytes(key));
      questionDeletes.add(optionDelete);
    });

    String key = String.format("%s%s%s", entity.getId(),
        Constants.SEPARATOR_CREATED_BY, entity.getCreatedBy().getId());
    Delete createdByDelete = new Delete(Bytes.toBytes(key));
    questionDeletes.add(createdByDelete);

    if (entity.getModifiedBy() != null) {
      key = String.format("%s%s%s", entity.getId(),
          Constants.SEPARATOR_CREATED_BY, entity.getModifiedBy().getId());
      Delete modifiedByDelete = new Delete(Bytes.toBytes(key));
      questionDeletes.add(modifiedByDelete);
    }

    return questionDeletes;
  }
}
