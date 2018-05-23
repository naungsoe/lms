package com.hsystems.lms.course.repository.hbase;

import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapperFactory;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.lesson.repository.hbase.HBaseActivityComponentMapper;
import com.hsystems.lms.lesson.repository.hbase.HBaseContentComponentMapper;
import com.hsystems.lms.lesson.repository.hbase.HBaseLessonComponentMapper;
import com.hsystems.lms.question.repository.hbase.HBaseChoiceOptionComponentMapper;
import com.hsystems.lms.question.repository.hbase.HBaseQuestionComponentMapper;
import com.hsystems.lms.quiz.repository.hbase.HBaseQuizComponentMapper;
import com.hsystems.lms.quiz.repository.hbase.HBaseSectionComponentMapper;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseCourseComponentMapperFactory
    implements HBaseComponentMapperFactory {

  private static final byte[] COMPONENT_TYPE_QUALIFIER
      = Bytes.toBytes("component_type");

  public HBaseCourseComponentMapperFactory() {

  }

  @Override
  public HBaseComponentMapper create(Result result) {
    switch (HBaseUtils.getString(result, COMPONENT_TYPE_QUALIFIER)) {
      case "TopicComponent":
        return new HBaseTopicComponentMapper();
      case "LessonComponent":
        return new HBaseLessonComponentMapper();
      case "ActivityComponent":
        return new HBaseActivityComponentMapper();
      case "ContentComponent":
        return new HBaseContentComponentMapper();
      case "QuizComponent":
        return new HBaseQuizComponentMapper();
      case "SectionComponent":
        return new HBaseSectionComponentMapper();
      case "QuestionComponent":
        return new HBaseQuestionComponentMapper();
      case "ChoiceOption":
        return new HBaseChoiceOptionComponentMapper();
      default:
        throw new IllegalArgumentException(
            "not supported component type");
    }
  }
}