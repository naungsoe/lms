package com.hsystems.lms.component.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.hbase.HBaseUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public final class HBaseNestedMapper<T extends Component>
    implements Mapper<Result, Nested<T>> {

  private static final byte[] PARENT_ID_QUALIFIER
      = Bytes.toBytes("parent_id");
  private static final byte[] RESOURCE_ID_QUALIFIER
      = Bytes.toBytes("resource_id");

  private final T component;

  public HBaseNestedMapper(T component) {
    this.component = component;
  }

  @Override
  public Nested<T> from(Result source) {
    String parentId = HBaseUtils.getString(source, PARENT_ID_QUALIFIER);
    String resourceId = HBaseUtils.getString(source, RESOURCE_ID_QUALIFIER);
    Nested.Builder<T> builder = new Nested.Builder<>(component);
    return builder.parentId(parentId).resourceId(resourceId).build();
  }
}