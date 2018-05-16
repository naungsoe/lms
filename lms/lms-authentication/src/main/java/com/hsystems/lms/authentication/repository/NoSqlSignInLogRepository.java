package com.hsystems.lms.authentication.repository;

import com.google.inject.Inject;

import com.hsystems.lms.authentication.repository.entity.SignInLog;
import com.hsystems.lms.authentication.repository.hbase.HBaseSignInLogRepository;

import java.io.IOException;
import java.util.Optional;

public final class NoSqlSignInLogRepository implements SignInLogRepository {

  private final HBaseSignInLogRepository hbaseSignInLogRepository;

  @Inject
  public NoSqlSignInLogRepository(
      HBaseSignInLogRepository hbaseSignInLogRepository) {

    this.hbaseSignInLogRepository = hbaseSignInLogRepository;
  }

  @Override
  public Optional<SignInLog> findBy(String id)
      throws IOException {

    return hbaseSignInLogRepository.findBy(id);
  }

  @Override
  public void add(SignInLog entity)
      throws IOException {

  }

  @Override
  public void update(SignInLog entity)
      throws IOException {

  }

  @Override
  public void remove(SignInLog entity)
      throws IOException {

  }
}