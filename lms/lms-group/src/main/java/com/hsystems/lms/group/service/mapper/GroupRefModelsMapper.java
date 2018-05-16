package com.hsystems.lms.group.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.group.repository.entity.Group;
import com.hsystems.lms.group.service.model.GroupModel;

import java.util.ArrayList;
import java.util.List;

public final class GroupRefModelsMapper
    implements Mapper<List<Group>, List<GroupModel>> {

  public GroupRefModelsMapper() {

  }

  @Override
  public List<GroupModel> from(List<Group> source) {
    List<GroupModel> groupModels = new ArrayList<>();

    for (Group group : source) {
      GroupModel groupModel = new GroupModel();
      groupModel.setId(group.getId());
      groupModel.setName(group.getName());
      groupModels.add(groupModel);
    }

    return groupModels;
  }
}