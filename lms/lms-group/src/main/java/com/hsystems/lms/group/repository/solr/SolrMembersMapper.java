package com.hsystems.lms.group.repository.solr;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.school.repository.solr.SolrUserRefMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SolrMembersMapper
    implements Mapper<SolrDocument, List<User>> {

  private static final String MEMBERS_FIELD = "members";

  private final String parentId;

  private final SolrUserRefMapper userRefMapper;

  public SolrMembersMapper(String parentId) {
    this.parentId = parentId;
    this.userRefMapper = new SolrUserRefMapper();
  }

  @Override
  public List<User> from(SolrDocument source) {
    List<SolrDocument> childDocuments = source.getChildDocuments();
    Predicate<SolrDocument> memberDocument
        = SolrUtils.isChildDocument(parentId, MEMBERS_FIELD);
    List<SolrDocument> documents = childDocuments.stream()
        .filter(memberDocument).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(documents)) {
      return Collections.emptyList();
    }

    List<User> members =  new ArrayList<>();

    for (SolrDocument document : documents) {
      User member = userRefMapper.from(document);
      members.add(member);
    }

    return members;
  }
}