package com.hsystems.lms.subject.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.subject.repository.entity.Subject;

import org.apache.solr.common.SolrInputDocument;

import java.util.List;

public final class SolrSubjectRefsDocMapper
    implements Mapper<List<Subject>, SolrInputDocument> {

  private static final String ID_FIELD_FORMAT = "subjects.%s.id";
  private static final String NAME_FIELD_FORMAT = "subjects.%s.name";

  private final SolrInputDocument document;

  public SolrSubjectRefsDocMapper(SolrInputDocument document) {
    this.document = document;
  }

  @Override
  public SolrInputDocument from(List<Subject> source) {
    for (int index = 0; index < source.size(); index++) {
      Subject subject = source.get(0);
      String idField = String.format(ID_FIELD_FORMAT, index);
      document.addField(idField, subject.getId());

      String nameField = String.format(NAME_FIELD_FORMAT, index);
      document.addField(nameField, subject.getName());
    }

    return document;
  }
}