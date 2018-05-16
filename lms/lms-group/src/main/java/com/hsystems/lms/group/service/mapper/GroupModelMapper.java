package com.hsystems.lms.group.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.User;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.service.model.GroupModel;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.service.mapper.SchoolRefModelMapper;
import com.hsystems.lms.school.service.mapper.UserRefModelMapper;

import java.time.LocalDateTime;

public final class GroupModelMapper
    implements Mapper<Auditable<Group>, GroupModel> {

  private final SchoolRefModelMapper schoolRefMapper;

  private final UserRefModelMapper userRefMapper;

  public GroupModelMapper() {
    this.schoolRefMapper = new SchoolRefModelMapper();
    this.userRefMapper = new UserRefModelMapper();
  }

  @Override
  public GroupModel from(Auditable<Group> source) {
    GroupModel groupModel = new GroupModel();
    Group group = source.getEntity();
    groupModel.setId(group.getId());
    groupModel.setName(group.getName());

    School school = group.getSchool();
    groupModel.setSchool(schoolRefMapper.from(school));

    User createdBy = source.getCreatedBy();
    groupModel.setCreatedBy(userRefMapper.from(createdBy));

    LocalDateTime createdOn = source.getCreatedOn();
    groupModel.setCreatedOn(DateTimeUtils.toString(createdOn));

    User modifiedBy = source.getCreatedBy();
    groupModel.setModifiedBy(userRefMapper.from(modifiedBy));

    LocalDateTime modifiedOn = source.getModifiedOn();
    groupModel.setModifiedOn(DateTimeUtils.toString(modifiedOn));
    return groupModel;
  }
}