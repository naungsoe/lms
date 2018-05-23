package com.hsystems.lms.quiz.repository.hbase;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseNestedComponentMapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.quiz.repository.entity.SectionComponent;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSectionComponentMapper
    implements HBaseComponentMapper<SectionComponent> {

  private static final byte[] TITLE_QUALIFIER = Bytes.toBytes("title");
  private static final byte[] INSTRUCTIONS_QUALIFIER
      = Bytes.toBytes("instructions");

  public HBaseSectionComponentMapper() {

  }

  @Override
  public Nested<SectionComponent> from(Result source) {
    String id = Bytes.toString(source.getRow());
    String title = HBaseUtils.getString(source, TITLE_QUALIFIER);
    String instructions = HBaseUtils.getString(source, INSTRUCTIONS_QUALIFIER);
    List<Component> components = Collections.emptyList();
    SectionComponent component
        = new SectionComponent(id, title, instructions, components);

    HBaseNestedComponentMapper componentMapper
        = new HBaseNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}