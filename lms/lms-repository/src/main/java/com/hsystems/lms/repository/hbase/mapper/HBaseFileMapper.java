package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.file.FileResource;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseFileMapper extends HBaseAbstractMapper<FileResource> {

  public List<FileResource> getEntities(
      List<Result> results, List<Mutation> mutations) {

    return Collections.emptyList();
  }

  private Optional<FileResource> getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    return Optional.empty();
  }

  @Override
  public Optional<FileResource> getEntity(List<Result> results) {
    return Optional.empty();
  }

  @Override
  public List<Put> getPuts(FileResource entity, long timestamp) {
    return Collections.emptyList();
  }

  @Override
  public List<Delete> getDeletes(FileResource entity, long timestamp) {
    return Collections.emptyList();
  }
}
