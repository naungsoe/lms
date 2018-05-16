package com.hsystems.lms.subject.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.subject.repository.entity.Subject;

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
public final class HBaseSubjectRefsMapper
    implements Mapper<List<Result>, List<Subject>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] NAME_QUALIFIER = Bytes.toBytes("name");

  private static final String SUBJECT_KEY_FORMAT = "%s_sub_";

  private final String parentKey;

  public HBaseSubjectRefsMapper(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override
  public List<Subject> from(List<Result> source) {
    String subjectKey = String.format(SUBJECT_KEY_FORMAT, parentKey);
    Predicate<Result> subjectResult = HBaseUtils.isChildResult(subjectKey);
    List<Result> results = source.stream()
        .filter(subjectResult).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Subject> subjects = new ArrayList<>();

    for (Result result : results) {
      String rowKey = Bytes.toString(result.getRow());
      int startIndex = rowKey.indexOf(rowKey);
      String id = rowKey.substring(startIndex);
      String name = HBaseUtils.getString(result, DATA_FAMILY, NAME_QUALIFIER);
      Subject subject = new Subject.Builder(id, name).build();
      subjects.add(subject);
    }

    return subjects;
  }
}