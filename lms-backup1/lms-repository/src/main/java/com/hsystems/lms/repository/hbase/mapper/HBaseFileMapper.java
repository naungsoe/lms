package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.file.FileObject;
import com.hsystems.lms.repository.entity.file.FileResource;

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
public class HBaseFileMapper extends HBaseEntityMapper<FileResource> {

  public List<FileResource> getEntities(List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<FileResource> resources = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<FileResource> resourceOptional = getEntity(result, results);

      if (resourceOptional.isPresent()) {
        resources.add(resourceOptional.get());
      }
    });

    return resources;
  }

  private Optional<FileResource> getEntity(
      Result mainResult, List<Result> results) {

    String id = Bytes.toString(mainResult.getRow());
    FileObject file = getFileObject(mainResult);

    Result fileResult = results.stream()
        .filter(isParentResult(id)).findFirst().get();
    String parentId = getParentId(fileResult);
    FileObject parentFileObject = getFileObject(fileResult);
    FileResource parentFile = new FileResource.Builder(
          parentId, parentFileObject, null).build();

    Result schoolResult = results.stream()
        .filter(isSchoolResult(id)).findFirst().get();
    School school = getSchool(schoolResult);
    List<Level> levels = getLevels(results, id);
    List<Subject> subjects = getSubjects(results, id);
    List<String> keywords = getKeywords(mainResult);

    Result createdByResult = results.stream()
        .filter(isCreatedByResult(id)).findFirst().get();
    User createdBy = getCreatedBy(createdByResult);
    LocalDateTime createdDateTime = getDateTime(createdByResult);

    Optional<Result> resultOptional = results.stream()
        .filter(isModifiedByResult(id)).findFirst();
    User modifiedBy = resultOptional.isPresent()
        ? getModifiedBy(resultOptional.get()) : null;
    LocalDateTime modifiedDateTime = resultOptional.isPresent()
        ? getDateTime(resultOptional.get()) : null;

    FileResource resource
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
    return Optional.of(resource);
  }

  @Override
  public Optional<FileResource> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }
}
