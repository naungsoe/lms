package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.Preferences;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrPreferencesMapper
    implements Mapper<SolrDocument, Preferences> {

  private static final String LOCALE_FIELD = "locale";
  private static final String TIME_FORMAT_FIELD = "timeFormat";
  private static final String DATE_FORMAT_FIELD = "dateFormat";
  private static final String DATE_TIME_FORMAT_FIELD = "dateTimeFormat";

  public SolrPreferencesMapper() {

  }

  @Override
  public Preferences from(SolrDocument source) {
    String locale = SolrUtils.getString(source, LOCALE_FIELD);
    String timeFormat = SolrUtils.getString(source, TIME_FORMAT_FIELD);
    String dateFormat = SolrUtils.getString(source, DATE_FORMAT_FIELD);
    String dateTimeFormat = SolrUtils.getString(source, DATE_TIME_FORMAT_FIELD);
    return new Preferences(locale, timeFormat, dateFormat, dateTimeFormat);
  }
}