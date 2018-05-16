package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.question.repository.entity.ChoiceOption;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseChoiceOptionsMapper
    implements Mapper<List<Result>, List<ChoiceOption>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] BODY_QUALIFIER = Bytes.toBytes("body");
  private static final byte[] FEEDBACK_QUALIFIER = Bytes.toBytes("feedback");
  private static final byte[] CORRECT_QUALIFIER = Bytes.toBytes("correct");
  private static final byte[] ORDER_QUALIFIER = Bytes.toBytes("order");

  private static final String OPTION_KEY_FORMAT = "%s_opt_";

  private final String parentKey;

  public HBaseChoiceOptionsMapper(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override
  public List<ChoiceOption> from(List<Result> source) {
    String optionKey = String.format(OPTION_KEY_FORMAT, parentKey);
    Predicate<Result> optionResult = HBaseUtils.isChildResult(optionKey);
    List<Result> results = source.stream()
        .filter(optionResult).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<ChoiceOption> options = new ArrayList<>();

    for (Result result : results) {
      String rowKey = Bytes.toString(result.getRow());
      int startIndex = rowKey.indexOf(optionKey);
      String id = rowKey.substring(startIndex);
      String body = HBaseUtils.getString(
          result, DATA_FAMILY, BODY_QUALIFIER);
      String feedbak = HBaseUtils.getString(
          result, DATA_FAMILY, FEEDBACK_QUALIFIER);
      boolean correct = HBaseUtils.getBoolean(
          result, DATA_FAMILY, CORRECT_QUALIFIER);
      int order = HBaseUtils.getInteger(
          result, DATA_FAMILY, ORDER_QUALIFIER);
      ChoiceOption option = new ChoiceOption(id, body,
          feedbak, correct, order);
      options.add(option);
    }

    return options;
  }
}