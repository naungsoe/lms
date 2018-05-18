package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.hbase.HBaseUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Collections;
import java.util.List;

public final class HBaseQuestionMapperFactory {

  private static final byte[] TYPE_QUALIFIER = Bytes.toBytes("type");

  public HBaseQuestionMapperFactory() {

  }

  public HBaseQuestionMapper create(Result result) {
    return create(result, Collections.emptyList());
  }

  public HBaseQuestionMapper create(
      Result result, List<Component> components) {

    switch (HBaseUtils.getString(result, TYPE_QUALIFIER)) {
      case "MultipleChoice":
        return new HBaseMultipleChoiceMapper(components);
      case "MultipleResponse":
        return new HBaseMultipleResponseMapper(components);
      case "CompositeQuestion":
        return new HBaseCompositeQuestionMapper(components);
      default:
        throw new IllegalArgumentException(
            "not supported question type");
    }
  }
}