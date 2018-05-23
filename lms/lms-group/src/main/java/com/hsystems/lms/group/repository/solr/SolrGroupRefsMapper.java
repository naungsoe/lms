package com.hsystems.lms.group.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SolrGroupRefsMapper
    implements Mapper<SolrDocument, List<Group>> {

  private static final String ID_FIELD_PATTERN = "groups.([0-9]*).id";

  private static final String NAME_FIELD_FORMAT = "groups.%s.name";

  public SolrGroupRefsMapper() {

  }

  @Override
  public List<Group> from(SolrDocument source) {
    List<Group> groups = new ArrayList<>();
    Collection<String> fieldNames = source.getFieldNames();
    Pattern pattern = Pattern.compile(ID_FIELD_PATTERN);

    for (String fieldName : fieldNames) {
      Matcher matcher = pattern.matcher(fieldName);

      if (matcher.find()) {
        String index = matcher.group(1);
        String id = SolrUtils.getString(source, fieldName);
        String name = SolrUtils.getString(source,
            String.format(NAME_FIELD_FORMAT, index));
        Group group = new Group.Builder(id, name).build();
        groups.add(group);
      }
    }

    return groups;
  }
}