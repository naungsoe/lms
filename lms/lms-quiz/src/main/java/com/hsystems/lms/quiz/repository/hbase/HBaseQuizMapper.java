package com.hsystems.lms.quiz.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.quiz.repository.entity.Quiz;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

public final class HBaseQuizMapper implements Mapper<Result, Quiz> {

  private static final byte[] TITLE_QUALIFIER = Bytes.toBytes("title");
  private static final byte[] DESCRIPTION_QUALIFIER
      = Bytes.toBytes("description");

  private final List<Component> components;

  public HBaseQuizMapper(List<Component> components) {
    this.components = components;
  }

  @Override
  public Quiz from(Result source) {
    String title = HBaseUtils.getString(source, TITLE_QUALIFIER);
    String description = HBaseUtils.getString(source, DESCRIPTION_QUALIFIER);
    return new Quiz(title, description, components);
  }
}