package com.hsystems.lms.lesson.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.lesson.repository.entity.Lesson;
import com.hsystems.lms.lesson.repository.entity.LessonResource;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRefsMapper;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;
import com.hsystems.lms.subject.repository.hbase.HBaseSubjectRefsMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

public final class HBaseLessonResourceMapper
    implements Mapper<Result, Auditable<LessonResource>> {

  private static final byte[] KEYWORDS_QUALIFIER = Bytes.toBytes("keywords");

  private final HBaseLessonMapper lessonMapper;

  public HBaseLessonResourceMapper(List<Component> components) {
    this.lessonMapper = new HBaseLessonMapper(components);
  }

  @Override
  public Auditable<LessonResource> from(Result source) {
    String id = Bytes.toString(source.getRow());
    Lesson lesson = lessonMapper.from(source);
    LessonResource.Builder builder = new LessonResource.Builder(id, lesson);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    HBaseLevelRefsMapper levelRefsMapper = new HBaseLevelRefsMapper();
    builder.levels(levelRefsMapper.from(source));

    HBaseSubjectRefsMapper subjectRefsMapper = new HBaseSubjectRefsMapper();
    builder.subjects(subjectRefsMapper.from(source));

    List<String> keywords = HBaseUtils.getStrings(source, KEYWORDS_QUALIFIER);
    builder.keywords(keywords);

    HBaseAuditableMapper<LessonResource> auditableMapper
        = new HBaseAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}