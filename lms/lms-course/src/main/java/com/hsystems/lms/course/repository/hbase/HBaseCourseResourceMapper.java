package com.hsystems.lms.course.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.course.repository.entity.Course;
import com.hsystems.lms.course.repository.entity.CourseResource;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRefsMapper;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;
import com.hsystems.lms.subject.repository.hbase.HBaseSubjectRefsMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;

public final class HBaseCourseResourceMapper
    implements Mapper<Result, Auditable<CourseResource>> {

  private static final byte[] KEYWORDS_QUALIFIER = Bytes.toBytes("keywords");

  private final HBaseCourseMapper courseMapper;

  public HBaseCourseResourceMapper(List<Component> components) {
    this.courseMapper = new HBaseCourseMapper(components);
  }

  @Override
  public Auditable<CourseResource> from(Result source) {
    String id = Bytes.toString(source.getRow());
    Course course = courseMapper.from(source);
    CourseResource.Builder builder = new CourseResource.Builder(id, course);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper();
    builder.school(schoolRefMapper.from(source));

    HBaseLevelRefsMapper levelRefsMapper = new HBaseLevelRefsMapper();
    builder.levels(levelRefsMapper.from(source));

    HBaseSubjectRefsMapper subjectRefsMapper = new HBaseSubjectRefsMapper();
    builder.subjects(subjectRefsMapper.from(source));

    List<String> keywords = HBaseUtils.getStrings(source, KEYWORDS_QUALIFIER);
    builder.keywords(keywords);

    HBaseAuditableMapper<CourseResource> auditableMapper
        = new HBaseAuditableMapper<>(builder.build());
    return auditableMapper.from(source);
  }
}