package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseQuestionComponentMapper
    implements HBaseComponentMapper {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] SCORE_QUALIFIER = Bytes.toBytes("score");
  private static final byte[] ORDER_QUALIFIER = Bytes.toBytes("order");
  private static final byte[] PARENT_ID_QUALIFIER = Bytes.toBytes("parentId");

  private final String resourceId;

  public HBaseQuestionComponentMapper(String resourceId) {
    this.resourceId = resourceId;
  }

  @Override
  public Nested<Component> from(List<Result> source) {
    Optional<Result> mainResultOptional = source.stream()
        .filter(HBaseUtils.isMainResult()).findFirst();

    if (!mainResultOptional.isPresent()) {
      throw new IllegalArgumentException(
          "there is no main result found");
    }

    Result mainResult = mainResultOptional.get();
    String id = Bytes.toString(mainResult.getRow());
    HBaseQuestionMapper questionMapper
        = new HBaseQuestionMapper(mainResult);
    Question question = questionMapper.from(source);
    long score = HBaseUtils.getLong(
        mainResult, DATA_FAMILY, SCORE_QUALIFIER);
    int order = HBaseUtils.getInteger(
        mainResult, DATA_FAMILY, ORDER_QUALIFIER);
    String parentId = HBaseUtils.getString(
        mainResult, DATA_FAMILY, PARENT_ID_QUALIFIER);
    QuestionComponent questionComponent
        = new QuestionComponent(id, question, score, order);
    return new Nested<>(questionComponent, resourceId, parentId);
  }
}