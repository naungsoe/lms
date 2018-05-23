package com.hsystems.lms.group.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.hbase.HBaseUtils;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseGroupRefsMapper
    implements Mapper<Result, List<Group>> {

  private static final byte[] GROUPS_QUALIFIER = Bytes.toBytes("groups");

  public HBaseGroupRefsMapper() {

  }

  @Override
  public List<Group> from(Result source) {
    if (!HBaseUtils.containsColumn(source, GROUPS_QUALIFIER)) {
      return Collections.emptyList();
    }

    List<Group> groups = new ArrayList<>();
    List<String> groupIds = HBaseUtils.getStrings(source, GROUPS_QUALIFIER);

    for (String groupId : groupIds) {
      byte[] columnQualifier = Bytes.toBytes(groupId);
      String name = HBaseUtils.getString(source, columnQualifier);
      Group group = new Group.Builder(groupId, name).build();
      groups.add(group);
    }

    return groups;
  }
}