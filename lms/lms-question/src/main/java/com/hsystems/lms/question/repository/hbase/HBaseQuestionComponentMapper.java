package com.hsystems.lms.question.repository.hbase;

import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentMapper;
import com.hsystems.lms.component.repository.hbase.HBaseNestedComponentMapper;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseQuestionComponentMapper
    implements HBaseComponentMapper<QuestionComponent> {

  private static final byte[] SCORE_QUALIFIER = Bytes.toBytes("score");

  private final HBaseQuestionMapperFactory mapperFactory;

  public HBaseQuestionComponentMapper() {
    this.mapperFactory = new HBaseQuestionMapperFactory();
  }

  @Override
  public Nested<QuestionComponent> from(Result source) {
    String id = Bytes.toString(source.getRow());
    long score = HBaseUtils.getLong(source, SCORE_QUALIFIER);
    HBaseQuestionMapper<Question> questionMapper = mapperFactory.create(source);
    Question question = questionMapper.from(source);
    QuestionComponent component = new QuestionComponent(id, question, score);

    HBaseNestedComponentMapper componentMapper
        = new HBaseNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}