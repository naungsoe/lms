package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.question.repository.entity.ChoiceOption;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseChoiceOptionMapper
    implements Mapper<Result, ChoiceOption> {

  private static final byte[] BODY_QUALIFIER = Bytes.toBytes("body");
  private static final byte[] FEEDBACK_QUALIFIER = Bytes.toBytes("feedback");
  private static final byte[] CORRECT_QUALIFIER = Bytes.toBytes("correct");

  public HBaseChoiceOptionMapper() {

  }

  @Override
  public ChoiceOption from(Result source) {
    String id = Bytes.toString(source.getRow());
    String body = HBaseUtils.getString(source, BODY_QUALIFIER);
    String feedbak = HBaseUtils.getString(source, FEEDBACK_QUALIFIER);
    boolean correct = HBaseUtils.getBoolean(source, CORRECT_QUALIFIER);
    return new ChoiceOption(id, body, feedbak, correct);
  }
}