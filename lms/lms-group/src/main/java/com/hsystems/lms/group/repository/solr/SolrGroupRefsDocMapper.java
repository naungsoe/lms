package com.hsystems.lms.group.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;

import org.apache.solr.common.SolrInputDocument;

import java.util.List;

public final class SolrGroupRefsDocMapper
    implements Mapper<List<Group>, SolrInputDocument> {

  private static final String ID_FIELD_FORMAT = "groups.%s.id";
  private static final String NAME_FIELD_FORMAT = "groups.%s.name";

  private final SolrInputDocument document;

  public SolrGroupRefsDocMapper(SolrInputDocument document) {
    this.document = document;
  }

  @Override
  public SolrInputDocument from(List<Group> source) {
    for (int index = 0; index < source.size(); index++) {
      Group group = source.get(0);
      String idField = String.format(ID_FIELD_FORMAT, index);
      document.addField(idField, group.getId());

      String nameField = String.format(NAME_FIELD_FORMAT, index);
      document.addField(nameField, group.getName());
    }

    return document;
  }
}