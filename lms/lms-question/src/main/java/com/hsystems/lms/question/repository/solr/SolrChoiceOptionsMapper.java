package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SolrChoiceOptionsMapper
    implements Mapper<SolrDocument, List<ChoiceOption>> {

  private static final String ID_FIELD = "id";
  private static final String BODY_FIELD = "body";
  private static final String FEEDBACK_FIELD = "feedback";
  private static final String CORRECT_FIELD = "correct";
  private static final String ORDER_FIELD = "order";

  private static final String OPTIONS_FIELD_FORMAT = "%s.options";

  private final String parentId;

  private final String fieldName;

  public SolrChoiceOptionsMapper(String parentId, String fieldName) {
    this.parentId = parentId;
    this.fieldName = fieldName;
  }

  @Override
  public List<ChoiceOption> from(SolrDocument source) {
    List<SolrDocument> childDocuments = source.getChildDocuments();
    String optionsFieldName = String.format(OPTIONS_FIELD_FORMAT, fieldName);
    Predicate<SolrDocument> optionDocument
        = SolrUtils.isChildDocument(parentId, optionsFieldName);
    List<SolrDocument> documents = childDocuments.stream()
        .filter(optionDocument).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(documents)) {
      return Collections.emptyList();
    }

    List<ChoiceOption> options = new ArrayList<>();

    for (SolrDocument document : documents) {
      String id = SolrUtils.getString(document, ID_FIELD);
      String body = SolrUtils.getString(document, BODY_FIELD);
      String feedback = SolrUtils.getString(document, FEEDBACK_FIELD);
      boolean correct = SolrUtils.getBoolean(document, CORRECT_FIELD);
      int order = SolrUtils.getInteger(document, ORDER_FIELD);
      ChoiceOption option = new ChoiceOption(id, body,
          feedback, correct, order);
      options.add(option);
    }

    return options;
  }
}