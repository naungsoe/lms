package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.repository.entity.MultipleChoice;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrMultipleChoiceMapper
    extends SolrQuestionMapper<MultipleChoice> {

  private final SolrChoiceOptionsMapper optionsMapper;

  public SolrMultipleChoiceMapper() {
    this.optionsMapper = new SolrChoiceOptionsMapper();
  }

  @Override
  public MultipleChoice from(SolrDocument source) {
    String body = getBody(source);
    List<ChoiceOption> options = optionsMapper.from(source);
    return new MultipleChoice(body, "", "", options);
  }
}