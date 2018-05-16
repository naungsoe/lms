package com.hsystems.lms.school.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public final class SolrSchoolRefMapper
    implements Mapper<SolrDocument, Optional<School>> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";
  private static final String SCHOOL_FIELD = "school";

  private final String parentId;

  public SolrSchoolRefMapper(String parentId) {
    this.parentId = parentId;
  }

  @Override
  public Optional<School> from(SolrDocument source) {
    List<SolrDocument> childDocuments = source.getChildDocuments();
    Predicate<SolrDocument> schoolDocument
        = SolrUtils.isChildDocument(parentId, SCHOOL_FIELD);
    Optional<SolrDocument> documentOptional = childDocuments.stream()
        .filter(schoolDocument).findFirst();

    if (documentOptional.isPresent()) {
      SolrDocument document = documentOptional.get();
      String id = SolrUtils.getString(document, ID_FIELD);
      String name = SolrUtils.getString(document, NAME_FIELD);
      School school = new School.Builder(id, name).build();
      return Optional.of(school);
    }

    return Optional.empty();
  }
}