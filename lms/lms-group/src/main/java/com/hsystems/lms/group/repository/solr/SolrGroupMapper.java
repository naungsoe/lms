package com.hsystems.lms.group.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.List;
import java.util.Optional;

public final class SolrGroupMapper
    implements Mapper<SolrDocument, Auditable<Group>> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";

  public SolrGroupMapper() {

  }

  @Override
  public Auditable<Group> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String name = SolrUtils.getString(source, NAME_FIELD);
    Group.Builder builder = new Group.Builder(id, name);

    SolrMembersMapper membersMapper = new SolrMembersMapper(id);
    List<User> members = membersMapper.from(source);

    if (CollectionUtils.isNotEmpty(members)) {
      builder.members(members);
    }

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      School school = schoolOptional.get();
      builder.school(school);
    }

    Group group = builder.build();
    SolrAuditableMapper<Group> auditableMapper
        = new SolrAuditableMapper<>(group, id);
    return auditableMapper.from(source);
  }
}