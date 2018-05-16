package com.hsystems.lms.group.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.hbase.HBaseUtils;

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
public final class HBaseGroupRefsMapper
    implements Mapper<List<Result>, List<Group>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  private static final String GROUP_KEY_FORMAT = "%s_grp_";

  private final String parentKey;

  public HBaseGroupRefsMapper(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override
  public List<Group> from(List<Result> source) {
    String groupKey = String.format(GROUP_KEY_FORMAT, parentKey);
    Predicate<Result> memberResult = HBaseUtils.isChildResult(groupKey);
    List<Result> results = source.stream()
        .filter(memberResult).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Group> groups = new ArrayList<>();

    for (Result result : results) {
      String rowKey = Bytes.toString(result.getRow());
      int startIndex = rowKey.indexOf(rowKey);
      String id = rowKey.substring(startIndex);
      String name = HBaseUtils.getString(result, DATA_FAMILY, NAME_QUALIFIER);
      Group group = new Group.Builder(id, name).build();
      groups.add(group);
    }

    return groups;
  }
}