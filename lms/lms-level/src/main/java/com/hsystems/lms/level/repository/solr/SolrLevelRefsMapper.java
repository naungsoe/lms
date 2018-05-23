package com.hsystems.lms.level.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SolrLevelRefsMapper
    implements Mapper<SolrDocument, List<Level>> {

  private static final String ID_FIELD_PATTERN = "levels.([0-9]*).id";

  private static final String NAME_FIELD_FORMAT = "levels.%s.name";

  public SolrLevelRefsMapper() {

  }

  @Override
  public List<Level> from(SolrDocument source) {
    List<Level> levels = new ArrayList<>();
    Collection<String> fieldNames = source.getFieldNames();
    Pattern pattern = Pattern.compile(ID_FIELD_PATTERN);

    for (String fieldName : fieldNames) {
      Matcher matcher = pattern.matcher(fieldName);

      if (matcher.find()) {
        String index = matcher.group(1);
        String id = SolrUtils.getString(source, fieldName);
        String name = SolrUtils.getString(source,
            String.format(NAME_FIELD_FORMAT, index));
        Level level = new Level.Builder(id, name).build();
        levels.add(level);
      }
    }

    return levels;
  }
}