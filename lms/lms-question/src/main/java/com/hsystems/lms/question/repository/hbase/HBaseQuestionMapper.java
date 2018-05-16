package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.special.UnknownQuestion;
import com.hsystems.lms.question.service.model.QuestionType;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseQuestionMapper
    implements Mapper<List<Result>, Question> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] TYPE_QUALIFIER = Bytes.toBytes("type");

  private final Result result;

  public HBaseQuestionMapper(Result result) {
    this.result = result;
  }

  @Override
  public Question from(List<Result> source) {
    String type = HBaseUtils.getString(
        result, DATA_FAMILY, TYPE_QUALIFIER);

    switch (QuestionType.valueOf(type)) {
      case MULTIPLE_CHOICE:
        HBaseMultipleChoiceMapper multipleChoiceMapper
            = new HBaseMultipleChoiceMapper(result);
        return multipleChoiceMapper.from(source);
      case MULTIPLE_RESPONSE:
        HBaseMultipleResponseMapper multipleResponseMapper
            = new HBaseMultipleResponseMapper(result);
        return multipleResponseMapper.from(source);
      case COMPOSITE:
        HBaseCompositeQuestionMapper compositeQuestionMapper
            = new HBaseCompositeQuestionMapper(result);
        return compositeQuestionMapper.from(source);
      default:
        return new UnknownQuestion();
    }
  }
}