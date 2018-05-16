package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.level.repository.hbase.HBaseLevelRefsMapper;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionResource;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.hbase.HBaseAuditableMapper;
import com.hsystems.lms.school.repository.hbase.HBaseSchoolRefMapper;
import com.hsystems.lms.subject.repository.hbase.HBaseSubjectRefsMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseQuestionResponseMapper
    implements Mapper<List<Result>, Auditable<QuestionResource>> {

  private static final byte[] DATA_FAMILY = Bytes.toBytes("d");

  private static final byte[] KEYWORDS_QUALIFIER = Bytes.toBytes("keywords");

  public HBaseQuestionResponseMapper() {

  }

  @Override
  public Auditable<QuestionResource> from(List<Result> source) {
    Optional<Result> mainResultOptional = source.stream()
        .filter(HBaseUtils.isMainResult()).findFirst();

    if (!mainResultOptional.isPresent()) {
      throw new IllegalArgumentException(
          "there is no main result found");
    }

    Result mainResult = mainResultOptional.get();
    String id = Bytes.toString(mainResult.getRow());
    HBaseQuestionMapper questionMapper
        = new HBaseQuestionMapper(mainResult);
    Question question = questionMapper.from(source);
    QuestionResource.Builder builder
        = new QuestionResource.Builder(id, question);

    HBaseSchoolRefMapper schoolRefMapper = new HBaseSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      builder.school(school);
    }

    HBaseLevelRefsMapper levelRefsMapper = new HBaseLevelRefsMapper(id);
    builder.levels(levelRefsMapper.from(source));

    HBaseSubjectRefsMapper subjectRefsMapper = new HBaseSubjectRefsMapper(id);
    builder.subjects(subjectRefsMapper.from(source));

    List<String> keywords = HBaseUtils.getStrings(
        mainResult, DATA_FAMILY, KEYWORDS_QUALIFIER);
    builder.keywords(keywords);

    QuestionResource resource = builder.build();
    HBaseAuditableMapper<QuestionResource> auditableMapper
        = new HBaseAuditableMapper<>(resource, id);
    return auditableMapper.from(source);
  }
}