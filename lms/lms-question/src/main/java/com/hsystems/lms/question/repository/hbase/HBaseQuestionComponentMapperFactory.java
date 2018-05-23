package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapperFactory;
import com.hsystems.lms.hbase.HBaseUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseQuestionComponentMapperFactory
    implements HBaseComponentMapperFactory {

  private static final byte[] COMPONENT_TYPE_QUALIFIER
      = Bytes.toBytes("component_type");

  public HBaseQuestionComponentMapperFactory() {

  }

  @Override
  public HBaseComponentMapper create(Result result) {
    switch (HBaseUtils.getString(result, COMPONENT_TYPE_QUALIFIER)) {
      case "QuestionComponent":
        return new HBaseQuestionComponentMapper();
      case "ChoiceOption":
        return new HBaseChoiceOptionComponentMapper();
      default:
        throw new IllegalArgumentException(
            "not supported component type");
    }
  }
}