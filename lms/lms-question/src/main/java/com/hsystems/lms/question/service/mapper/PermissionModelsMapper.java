package com.hsystems.lms.question.service.mapper;

import com.hsystems.lms.entity.User;
import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.resource.Permission;
import com.hsystems.lms.question.service.model.PermissionModel;
import com.hsystems.lms.school.service.mapper.UserRefModelMapper;

import java.util.ArrayList;
import java.util.List;

public final class PermissionModelsMapper
    implements Mapper<List<Permission>, List<PermissionModel>> {

  private final UserRefModelMapper userRefMapper;


  public PermissionModelsMapper() {
    this.userRefMapper = new UserRefModelMapper();
  }

  @Override
  public List<PermissionModel> from(List<Permission> source) {
    List<PermissionModel> permissionModels = new ArrayList<>();

    for (Permission permission : source) {
      PermissionModel permissionModel = new PermissionModel();
      User user = permission.getUser();
      permissionModel.setUser(userRefMapper.from(user));
      permissionModel.setPrivilege(permission.getPrivilege());
      permissionModels.add(permissionModel);
    }

    return permissionModels;
  }
}