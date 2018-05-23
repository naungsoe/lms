package com.hsystems.lms.lesson.repository.hbase;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseNestedComponentMapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.lesson.repository.entity.ActivityComponent;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseActivityComponentMapper
    implements HBaseComponentMapper<ActivityComponent> {

  private static final byte[] TITLE_QUALIFIER = Bytes.toBytes("title");
  private static final byte[] INSTRUCTIONS_QUALIFIER
      = Bytes.toBytes("instructions");

  public HBaseActivityComponentMapper() {

  }

  @Override
  public Nested<ActivityComponent> from(Result source) {
    String id = Bytes.toString(source.getRow());
    String title = HBaseUtils.getString(source, TITLE_QUALIFIER);
    String instructions = HBaseUtils.getString(source, INSTRUCTIONS_QUALIFIER);
    List<Component> components = Collections.emptyList();
    ActivityComponent component
        = new ActivityComponent(id, title, instructions, components);

    HBaseNestedComponentMapper componentMapper
        = new HBaseNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}