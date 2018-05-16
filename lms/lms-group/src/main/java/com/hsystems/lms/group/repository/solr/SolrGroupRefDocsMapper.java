package com.hsystems.lms.group.repository.solr;

import com.hsystems.lms.common.util.CommonUtils;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;

import org.apache.solr.common.SolrInputDocument;

import java.util.ArrayList;
import java.util.List;

public final class SolrGroupRefDocsMapper
    implements Mapper<List<Group>, List<SolrInputDocument>> {

  private static final String ID_FIELD = "id";
  private static final String ENTITY_ID_FIELD = "entityId";
  private static final String PARENT_ID_FIELD = "parentId";
  private static final String FIELD_NAME_FIELD = "fieldName";
  private static final String GROUPS_FIELD = "groups";
  private static final String NAME_FIELD = "name";

  private final String parentId;

  public SolrGroupRefDocsMapper(String parentId) {
    this.parentId = parentId;
  }

  @Override
  public List<SolrInputDocument> from(List<Group> source) {
    List<SolrInputDocument> documents = new ArrayList<>();

    for (Group group : source) {
      SolrInputDocument document = new SolrInputDocument();
      document.addField(ID_FIELD, CommonUtils.genUniqueKey());
      document.addField(ENTITY_ID_FIELD, group.getId());
      document.addField(PARENT_ID_FIELD, parentId);
      document.addField(FIELD_NAME_FIELD, GROUPS_FIELD);
      document.addField(NAME_FIELD, group.getName());
      documents.add(document);
    }

    return documents;
  }
}