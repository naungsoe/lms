package com.hsystems.lms.school.repository.solr;

    import com.hsystems.lms.common.mapper.Mapper;
    import com.hsystems.lms.school.repository.entity.School;

    import org.apache.solr.common.SolrInputDocument;

public final class SolrSchoolRefDocMapper
    implements Mapper<School, SolrInputDocument> {

  private static final String ID_FIELD = "school.id";
  private static final String NAME_FIELD = "school.name";

  private final SolrInputDocument document;

  public SolrSchoolRefDocMapper(SolrInputDocument document) {
    this.document = document;
  }

  @Override
  public SolrInputDocument from(School source) {
    document.addField(ID_FIELD, source.getId());
    document.addField(NAME_FIELD, source.getName());
    return document;
  }
}