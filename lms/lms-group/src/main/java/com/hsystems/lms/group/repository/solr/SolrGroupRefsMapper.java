package com.hsystems.lms.group.repository.solr;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SolrGroupRefsMapper
    implements Mapper<SolrDocument, List<Group>> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";
  private static final String GROUPS_FIELD = "groups";

  private final String parentId;

  public SolrGroupRefsMapper(String parentId) {
    this.parentId = parentId;
  }

  @Override
  public List<Group> from(SolrDocument source) {
    List<SolrDocument> childDocuments = source.getChildDocuments();
    Predicate<SolrDocument> groupDocument
        = SolrUtils.isChildDocument(parentId, GROUPS_FIELD);
    List<SolrDocument> documents = childDocuments.stream()
        .filter(groupDocument).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(documents)) {
      Collections.emptyList();
    }

    List<Group> groups = new ArrayList<>();

    for (SolrDocument document : documents) {
      String id = SolrUtils.getString(document, ID_FIELD);
      String name = SolrUtils.getString(document, NAME_FIELD);
      Group group = new Group.Builder(id, name).build();
      groups.add(group);
    }

    return groups;
  }
}