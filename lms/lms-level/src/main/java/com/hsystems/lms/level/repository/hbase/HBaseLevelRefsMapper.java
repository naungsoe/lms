package com.hsystems.lms.level.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.level.repository.entity.Level;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseLevelRefsMapper
    implements Mapper<List<Result>, List<Level>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  private static final String LEVEL_KEY_FORMAT = "%s_lvl_";

  private final String parentKey;

  public HBaseLevelRefsMapper(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override
  public List<Level> from(List<Result> source) {
    String levelKey = String.format(LEVEL_KEY_FORMAT, parentKey);
    Predicate<Result> levelResult = HBaseUtils.isChildResult(levelKey);
    List<Result> results = source.stream()
        .filter(levelResult).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Level> levels = new ArrayList<>();

    for (Result result : results) {
      String rowKey = Bytes.toString(result.getRow());
      int startIndex = rowKey.indexOf(rowKey);
      String id = rowKey.substring(startIndex);
      String name = HBaseUtils.getString(result, DATA_FAMILY, NAME_QUALIFIER);
      Level level = new Level.Builder(id, name).build();
      levels.add(level);
    }

    return levels;
  }
}