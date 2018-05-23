package com.hsystems.lms.course.repository.solr;

import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.component.repository.solr.SolrComponentMapperFactory;
import com.hsystems.lms.lesson.repository.solr.SolrActivityComponentMapper;
import com.hsystems.lms.lesson.repository.solr.SolrContentComponentMapper;
import com.hsystems.lms.lesson.repository.solr.SolrLessonComponentMapper;
import com.hsystems.lms.question.repository.solr.SolrQuestionComponentMapper;
import com.hsystems.lms.quiz.repository.solr.SolrQuizComponentMapper;
import com.hsystems.lms.quiz.repository.solr.SolrSectionComponentMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrCourseComponentMapperFactory
    implements SolrComponentMapperFactory {

  private static final String COMPONENT_TYPE_FIELD = "component_type";

  public SolrCourseComponentMapperFactory() {

  }

  @Override
  public SolrComponentMapper create(SolrDocument document) {
    switch (SolrUtils.getString(document, COMPONENT_TYPE_FIELD)) {
      case "LessonComponent":
        return new SolrLessonComponentMapper();
      case "ActivityComponent":
        return new SolrActivityComponentMapper();
      case "ContentComponent":
        return new SolrContentComponentMapper();
      case "QuizComponent":
        return new SolrQuizComponentMapper();
      case "SectionComponent":
        return new SolrSectionComponentMapper();
      case "QuestionComponent":
        return new SolrQuestionComponentMapper();
      default:
        throw new IllegalArgumentException(
            "not supported component type");
    }
  }
}