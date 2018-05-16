package com.hsystems.lms.group.repository.hbase;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.school.repository.hbase.HBaseUserRefMapper;

import org.apache.hadoop.hbase.client.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class HBaseMembersMapper
    implements Mapper<List<Result>, List<User>> {

  private static final String MEMBER_KEY_FORMAT = "%s_mem_";

  private final String parentKey;

  public HBaseMembersMapper(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override
  public List<User> from(List<Result> source) {
    String memberKey = String.format(MEMBER_KEY_FORMAT, parentKey);
    Predicate<Result> memberResult = HBaseUtils.isChildResult(memberKey);
    List<Result> results = source.stream()
        .filter(memberResult).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<User> members = new ArrayList<>();
    HBaseUserRefMapper userRefMapper = new HBaseUserRefMapper(memberKey);

    for (Result result : results) {
      User member = userRefMapper.from(result);
      members.add(member);
    }

    return members;
  }
}