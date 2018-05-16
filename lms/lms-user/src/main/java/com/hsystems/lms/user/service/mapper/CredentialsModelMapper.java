package com.hsystems.lms.user.service.mapper;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.user.repository.entity.Credentials;
import com.hsystems.lms.user.service.model.CredentialsModel;

public final class CredentialsModelMapper
    implements Mapper<Credentials, CredentialsModel> {

  public CredentialsModelMapper() {

  }

  @Override
  public CredentialsModel from(Credentials source) {
    CredentialsModel credentialsModel = new CredentialsModel();
    credentialsModel.setAccount(source.getAccount());
    credentialsModel.setPassword(source.getPassword());
    credentialsModel.setSalt(source.getSalt());
    return credentialsModel;
  }
}