package com.hsystems.lms.lesson.repository.hbase;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseNestedComponentMapper;
import com.hsystems.lms.lesson.repository.entity.Lesson;
import com.hsystems.lms.lesson.repository.entity.LessonComponent;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseLessonComponentMapper
    implements HBaseComponentMapper<LessonComponent> {

  private final HBaseLessonMapper lessonMapper;

  public HBaseLessonComponentMapper() {
    List<Component> components = Collections.emptyList();
    this.lessonMapper = new HBaseLessonMapper(components);
  }

  @Override
  public Nested<LessonComponent> from(Result source) {
    String id = Bytes.toString(source.getRow());
    Lesson lesson = lessonMapper.from(source);
    LessonComponent component = new LessonComponent(id, lesson);

    HBaseNestedComponentMapper componentMapper
        = new HBaseNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}