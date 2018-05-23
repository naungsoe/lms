package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseNestedComponentMapper;
import com.hsystems.lms.question.repository.entity.ChoiceOption;

import org.apache.hadoop.hbase.client.Result;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseChoiceOptionComponentMapper
    implements HBaseComponentMapper<ChoiceOption> {

  private final HBaseChoiceOptionMapper optionMapper;

  public HBaseChoiceOptionComponentMapper() {
    this.optionMapper = new HBaseChoiceOptionMapper();
  }

  @Override
  public Nested<ChoiceOption> from(Result source) {
    ChoiceOption option = optionMapper.from(source);

    HBaseNestedComponentMapper componentMapper
        = new HBaseNestedComponentMapper(option);
    return componentMapper.from(source);
  }
}