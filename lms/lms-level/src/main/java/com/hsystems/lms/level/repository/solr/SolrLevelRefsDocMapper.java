package com.hsystems.lms.level.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.level.repository.entity.Level;

import org.apache.solr.common.SolrInputDocument;

import java.util.List;

public final class SolrLevelRefsDocMapper
    implements Mapper<List<Level>, SolrInputDocument> {

  private static final String ID_FIELD_FORMAT = "levels.%s.id";
  private static final String NAME_FIELD_FORMAT = "levels.%s.name";

  private final SolrInputDocument document;

  public SolrLevelRefsDocMapper(SolrInputDocument document) {
    this.document = document;
  }

  @Override
  public SolrInputDocument from(List<Level> source) {
    for (int index = 0; index < source.size(); index++) {
      Level level = source.get(0);
      String idField = String.format(ID_FIELD_FORMAT, index);
      document.addField(idField, level.getId());

      String nameField = String.format(NAME_FIELD_FORMAT, index);
      document.addField(nameField, level.getName());
    }

    return document;
  }
}