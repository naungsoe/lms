package com.hsystems.lms.group.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.service.model.GroupModel;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.AuditableModelMapper;
import com.hsystems.lms.school.service.mapper.SchoolRefModelMapper;

public final class GroupModelMapper
    implements Mapper<Auditable<Group>, GroupModel> {

  private final SchoolRefModelMapper schoolRefMapper;

  public GroupModelMapper() {
    this.schoolRefMapper = new SchoolRefModelMapper();
  }

  @Override
  public GroupModel from(Auditable<Group> source) {
    GroupModel groupModel = new GroupModel();
    Group group = source.getEntity();
    groupModel.setId(group.getId());
    groupModel.setName(group.getName());

    School school = group.getSchool();
    groupModel.setSchool(schoolRefMapper.from(school));

    AuditableModelMapper<GroupModel> auditableMapper
        = new AuditableModelMapper<>(groupModel);
    return auditableMapper.from(source);
  }
}