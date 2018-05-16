package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.Preferences;

import java.util.HashMap;
import java.util.Map;

public final class SolrPreferencesMapMapper
    implements Mapper<Preferences, Map<String, Object>> {

  private static final String LOCALE_FIELD = "locale";
  private static final String TIME_FORMAT_FIELD = "tformat";
  private static final String DATE_FORMAT_FIELD = "dformat";
  private static final String DATE_TIME_FORMAT_FIELD = "dtformat";

  public SolrPreferencesMapMapper() {

  }

  @Override
  public Map<String, Object> from(Preferences source) {
    Map<String, Object> fieldMap = new HashMap<>();
    fieldMap.put(LOCALE_FIELD, source.getLocale());
    fieldMap.put(TIME_FORMAT_FIELD, source.getTimeFormat());
    fieldMap.put(DATE_FORMAT_FIELD, source.getDateFormat());
    fieldMap.put(DATE_TIME_FORMAT_FIELD, source.getDateTimeFormat());
    return fieldMap;
  }
}