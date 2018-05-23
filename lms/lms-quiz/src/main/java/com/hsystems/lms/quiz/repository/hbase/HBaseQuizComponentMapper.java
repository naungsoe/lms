package com.hsystems.lms.quiz.repository.hbase;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseNestedComponentMapper;
import com.hsystems.lms.quiz.repository.entity.Quiz;
import com.hsystems.lms.quiz.repository.entity.QuizComponent;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseQuizComponentMapper
    implements HBaseComponentMapper<QuizComponent> {

  private final HBaseQuizMapper quizMapper;

  public HBaseQuizComponentMapper() {
    List<Component> components = Collections.emptyList();
    this.quizMapper = new HBaseQuizMapper(components);
  }

  @Override
  public Nested<QuizComponent> from(Result source) {
    String id = Bytes.toString(source.getRow());
    Quiz quiz = quizMapper.from(source);
    QuizComponent component = new QuizComponent(id, quiz);

    HBaseNestedComponentMapper componentMapper
        = new HBaseNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}