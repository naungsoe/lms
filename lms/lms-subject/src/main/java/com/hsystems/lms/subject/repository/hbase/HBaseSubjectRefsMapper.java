package com.hsystems.lms.subject.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.subject.repository.entity.Subject;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseSubjectRefsMapper
    implements Mapper<Result, List<Subject>> {

  private static final byte[] SUBJECTS_QUALIFIER = Bytes.toBytes("subjects");

  public HBaseSubjectRefsMapper() {

  }

  @Override
  public List<Subject> from(Result source) {
    if (!HBaseUtils.containsColumn(source, SUBJECTS_QUALIFIER)) {
      return Collections.emptyList();
    }

    List<Subject> subjects = new ArrayList<>();
    List<String> subjectIds = HBaseUtils.getStrings(source, SUBJECTS_QUALIFIER);

    for (String subjectId : subjectIds) {
      byte[] columnQualifier = Bytes.toBytes(subjectId);
      String name = HBaseUtils.getString(source, columnQualifier);
      Subject subject = new Subject.Builder(subjectId, name).build();
      subjects.add(subject);
    }

    return subjects;
  }
}