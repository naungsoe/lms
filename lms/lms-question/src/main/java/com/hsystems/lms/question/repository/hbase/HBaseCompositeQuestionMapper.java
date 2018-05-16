package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseCompositeQuestionMapper
    implements Mapper<List<Result>, Question> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] BODY_QUALIFIER = Bytes.toBytes("body");
  private static final byte[] HINT_QUALIFIER = Bytes.toBytes("hint");
  private static final byte[] EXPLANATION_QUALIFIER
      = Bytes.toBytes("explanation");

  private final Result result;

  public HBaseCompositeQuestionMapper(Result result) {
    this.result = result;
  }

  @Override
  public Question from(List<Result> source) {
    String body = HBaseUtils.getString(
        result, DATA_FAMILY, BODY_QUALIFIER);
    String hint = HBaseUtils.getString(
        result, DATA_FAMILY, HINT_QUALIFIER);
    String explanation = HBaseUtils.getString(
        result, DATA_FAMILY, EXPLANATION_QUALIFIER);

    List<QuestionComponent> components = Collections.emptyList();
    return new CompositeQuestion(body, hint, explanation, components);
  }
}