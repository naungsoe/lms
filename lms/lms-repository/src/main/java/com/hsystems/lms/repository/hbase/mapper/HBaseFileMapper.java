package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.FileResource;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseFileMapper extends HBaseMapper<FileResource> {

  public List<FileResource> getEntities(
      List<Result> results, List<Mutation> mutations) {

    return Collections.emptyList();
  }

  private Level getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    return null;
  }

  @Override
  public FileResource getEntity(List<Result> results) {
    return null;
  }

  @Override
  public List<Put> getPuts(FileResource entity, long timestamp) {
    return new ArrayList<>();
  }

  @Override
  public List<Delete> getDeletes(FileResource entity, long timestamp) {
    return new ArrayList<>();
  }
}
