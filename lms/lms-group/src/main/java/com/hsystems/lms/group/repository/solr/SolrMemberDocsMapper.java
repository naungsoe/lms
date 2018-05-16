package com.hsystems.lms.group.repository.solr;

import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.solr.SolrUserRefDocMapper;

import org.apache.solr.common.SolrInputDocument;

import java.util.ArrayList;
import java.util.List;

public final class SolrMemberDocsMapper
    implements Mapper<List<User>, List<SolrInputDocument>> {

  private static final String MEMBERS_FIELD = "members";

  private final String parentId;

  public SolrMemberDocsMapper(String parentId) {
    this.parentId = parentId;
  }

  @Override
  public List<SolrInputDocument> from(List<User> source) {
    List<SolrInputDocument> documents = new ArrayList<>();

    for (User member : source) {
      SolrUserRefDocMapper userRefDocMapper
          = new SolrUserRefDocMapper(parentId, MEMBERS_FIELD);
      SolrInputDocument document = userRefDocMapper.from(member);
      documents.add(document);
    }

    return documents;
  }
}