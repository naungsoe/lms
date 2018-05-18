package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.question.repository.entity.Question;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public abstract class HBaseQuestionMapper<T extends Question>
    implements Mapper<Result, T> {

  private static final byte[] BODY_QUALIFIER = Bytes.toBytes("body");
  private static final byte[] HINT_QUALIFIER = Bytes.toBytes("hint");
  private static final byte[] EXPLANATION_QUALIFIER
      = Bytes.toBytes("explanation");

  protected String getBody(Result result) {
    return HBaseUtils.getString(result, BODY_QUALIFIER);
  }

  protected String getHint(Result result) {
    return HBaseUtils.getString(result, HINT_QUALIFIER);
  }

  protected String getExplanation(Result result) {
    return HBaseUtils.getString(result, EXPLANATION_QUALIFIER);
  }
}