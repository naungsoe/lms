package com.hsystems.lms.quiz.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRefsMapper;
import com.hsystems.lms.quiz.repository.entity.Quiz;
import com.hsystems.lms.quiz.repository.entity.QuizResource;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;
import com.hsystems.lms.subject.repository.hbase.HBaseSubjectRefsMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

public final class HBaseQuizResourceMapper
    implements Mapper<Result, Auditable<QuizResource>> {

  private static final byte[] KEYWORDS_QUALIFIER = Bytes.toBytes("keywords");

  private final HBaseQuizMapper quizMapper;

  public HBaseQuizResourceMapper(List<Component> components) {
    this.quizMapper = new HBaseQuizMapper(components);
  }

  @Override
  public Auditable<QuizResource> from(Result source) {
    String id = Bytes.toString(source.getRow());
    Quiz quiz = quizMapper.from(source);
    QuizResource.Builder builder = new QuizResource.Builder(id, quiz);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    HBaseLevelRefsMapper levelRefsMapper = new HBaseLevelRefsMapper();
    builder.levels(levelRefsMapper.from(source));

    HBaseSubjectRefsMapper subjectRefsMapper = new HBaseSubjectRefsMapper();
    builder.subjects(subjectRefsMapper.from(source));

    List<String> keywords = HBaseUtils.getStrings(source, KEYWORDS_QUALIFIER);
    builder.keywords(keywords);

    HBaseAuditableMapper<QuizResource> auditableMapper
        = new HBaseAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}