package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.file.FileObject;
import com.hsystems.lms.repository.entity.file.FileResource;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseFileMapper extends HBaseAbstractMapper<FileResource> {

  public String getChildId(Result result) {
    String rowKey = Bytes.toString(result.getRow());
    int endIndex = rowKey.indexOf(Constants.SEPARATOR);
    return rowKey.substring(0, endIndex);
  }

  public List<FileResource> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<FileResource> fileResources = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      String id = Bytes.toString(result.getRow());
      Optional<Mutation> mutationOptional = getMutationById(mutations, id);

      if (mutationOptional.isPresent()) {
        Mutation mutation = mutationOptional.get();
        long timestamp = mutation.getTimestamp();
        Optional<FileResource> resourceOptional
            = getEntity(result, results, timestamp);

        if (resourceOptional.isPresent()) {
          fileResources.add(resourceOptional.get());
        }
      }
    });

    return fileResources;
  }

  private Optional<FileResource> getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    FileObject file = getFileObject(mainResult, timestamp);

    Result parentFileResult = results.stream()
        .filter(isParentResult(id)).findFirst().get();
    String parentId = getId(parentFileResult, Constants.SEPARATOR_PARENT);
    FileObject parentFileObject = getFileObject(parentFileResult, timestamp);
    FileResource parentFile = new FileResource.Builder(
          parentId, parentFileObject, null).build();

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult, timestamp);
    List<Level> levels = getLevels(results, id, timestamp);
    List<Subject> subjects = getSubjects(results, id, timestamp);
    List<String> keywords = getKeywords(mainResult, timestamp);

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult, timestamp);
    LocalDateTime createdDateTime = getDateTime(createdByResult, timestamp);

    Optional<Result> resultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = resultOptional.isPresent()
        ? getModifiedBy(resultOptional.get(), timestamp) : null;
    LocalDateTime modifiedDateTime = resultOptional.isPresent()
        ? getDateTime(resultOptional.get(), timestamp) : null;

    FileResource fileResource
        = new FileResource.Builder(id, file, parentFile)
        .school(school)
        .levels(levels)
        .subjects(subjects)
        .keywords(keywords)
        .createdBy(createdBy)
        .createdDateTime(createdDateTime)
        .modifiedBy(modifiedBy)
        .modifiedDateTime(modifiedDateTime)
        .build();
    return Optional.of(fileResource);
  }

  @Override
  public Optional<FileResource> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
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
