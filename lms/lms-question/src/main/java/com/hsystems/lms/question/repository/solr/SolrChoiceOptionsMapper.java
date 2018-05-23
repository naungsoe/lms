package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.ChoiceOption;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SolrChoiceOptionsMapper
    implements Mapper<SolrDocument, List<ChoiceOption>> {

  private static final String ID_FIELD_PATTERN = "options.([0-9]*).id";

  private static final String BODY_FIELD_FORMAT = "options.%s.body";
  private static final String CORRECT_FIELD_FORMAT = "options.%s.correct";

  public SolrChoiceOptionsMapper() {

  }

  @Override
  public List<ChoiceOption> from(SolrDocument source) {
    List<ChoiceOption> options = new ArrayList<>();
    Collection<String> fieldNames = source.getFieldNames();
    Pattern pattern = Pattern.compile(ID_FIELD_PATTERN);

    for (String fieldName : fieldNames) {
      Matcher matcher = pattern.matcher(fieldName);

      if (matcher.find()) {
        String index = matcher.group(1);
        String id = SolrUtils.getString(source, fieldName);
        String body = SolrUtils.getString(source,
            String.format(BODY_FIELD_FORMAT, index));
        boolean correct = SolrUtils.getBoolean(source,
            String.format(CORRECT_FIELD_FORMAT, index));
        ChoiceOption option = new ChoiceOption(id, body, "", correct);
        options.add(option);
      }
    }

    return options;
  }
}