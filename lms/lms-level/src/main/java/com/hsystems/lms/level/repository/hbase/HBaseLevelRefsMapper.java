package com.hsystems.lms.level.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.level.repository.entity.Level;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseLevelRefsMapper
    implements Mapper<Result, List<Level>> {

  private static final byte[] LEVELS_QUALIFIER = Bytes.toBytes("levels");

  public HBaseLevelRefsMapper() {

  }

  @Override
  public List<Level> from(Result source) {
    if (!HBaseUtils.containsColumn(source, LEVELS_QUALIFIER)) {
      return Collections.emptyList();
    }

    List<Level> levels = new ArrayList<>();
    List<String> levelIds = HBaseUtils.getStrings(source, LEVELS_QUALIFIER);
    levelIds.forEach(levelId -> {
      String name = HBaseUtils.getString(source, Bytes.toBytes(levelId));
      Level level = new Level.Builder(levelId, name).build();
      levels.add(level);
    });

    return levels;
  }
}