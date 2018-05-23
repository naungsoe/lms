package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRefsMapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;
import com.hsystems.lms.subject.repository.hbase.HBaseSubjectRefsMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

public final class HBaseQuestionResourceMapper
    implements Mapper<Result, Auditable<QuestionResource>> {

  private static final byte[] KEYWORDS_QUALIFIER = Bytes.toBytes("keywords");

  private final List<Component> components;

  private final HBaseQuestionMapperFactory questionMapperFactory;

  public HBaseQuestionResourceMapper(List<Component> components) {
    this.components = components;
    this.questionMapperFactory = new HBaseQuestionMapperFactory();
  }

  @Override
  public Auditable<QuestionResource> from(Result source) {
    String id = Bytes.toString(source.getRow());
    HBaseQuestionMapper<Question> questionMapper
        = questionMapperFactory.create(source, components);
    Question question = questionMapper.from(source);
    QuestionResource.Builder builder
        = new QuestionResource.Builder(id, question);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    HBaseLevelRefsMapper levelRefsMapper = new HBaseLevelRefsMapper();
    builder.levels(levelRefsMapper.from(source));

    HBaseSubjectRefsMapper subjectRefsMapper = new HBaseSubjectRefsMapper();
    builder.subjects(subjectRefsMapper.from(source));

    List<String> keywords = HBaseUtils.getStrings(source, KEYWORDS_QUALIFIER);
    builder.keywords(keywords);

    HBaseAuditableMapper<QuestionResource> auditableMapper
        = new HBaseAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}