package com.hsystems.lms.subject.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.solr.SolrUtils;
import com.hsystems.lms.subject.repository.entity.Subject;

import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SolrSubjectRefsMapper
    implements Mapper<SolrDocument, List<Subject>> {

  private static final String ID_FIELD_PATTERN = "subjects.([0-9]*).id";

  private static final String NAME_FIELD_FORMAT = "subjects.%s.name";

  public SolrSubjectRefsMapper() {

  }

  @Override
  public List<Subject> from(SolrDocument source) {
    List<Subject> subjects = new ArrayList<>();
    Collection<String> fieldNames = source.getFieldNames();
    Pattern pattern = Pattern.compile(ID_FIELD_PATTERN);

    for (String fieldName : fieldNames) {
      Matcher matcher = pattern.matcher(fieldName);

      if (matcher.find()) {
        String index = matcher.group(1);
        String id = SolrUtils.getString(source, fieldName);
        String name = SolrUtils.getString(source,
            String.format(NAME_FIELD_FORMAT, index));
        Subject subject = new Subject.Builder(id, name).build();
        subjects.add(subject);
      }
    }

    return subjects;
  }
}