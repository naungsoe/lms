package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.question.repository.entity.MultipleResponse;

import org.apache.solr.common.SolrDocument;

import java.util.List;

public final class SolrMultipleResponseMapper
    extends SolrQuestionMapper<MultipleResponse> {

  private final SolrChoiceOptionsMapper optionsMapper;

  public SolrMultipleResponseMapper() {
    this.optionsMapper = new SolrChoiceOptionsMapper();
  }

  @Override
  public MultipleResponse from(SolrDocument source) {
    String body = getBody(source);
    List<ChoiceOption> options = optionsMapper.from(source);
    return new MultipleResponse(body, "", "", options);
  }
}