package com.hsystems.lms.lesson.repository.hbase;

import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseNestedComponentMapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.lesson.repository.entity.ContentComponent;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseContentComponentMapper
    implements HBaseComponentMapper<ContentComponent> {

  private static final byte[] CONTENT_QUALIFIER = Bytes.toBytes("content");

  public HBaseContentComponentMapper() {

  }

  @Override
  public Nested<ContentComponent> from(Result source) {
    String id = Bytes.toString(source.getRow());
    String content = HBaseUtils.getString(source, CONTENT_QUALIFIER);
    ContentComponent component = new ContentComponent(id, content);

    HBaseNestedComponentMapper componentMapper
        = new HBaseNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}