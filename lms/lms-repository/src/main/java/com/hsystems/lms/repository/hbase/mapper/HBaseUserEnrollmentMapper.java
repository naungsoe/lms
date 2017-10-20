package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Mutation;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.UserEnrollment;
import com.hsystems.lms.repository.entity.course.Course;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public class HBaseUserEnrollmentMapper
    extends HBaseAbstractMapper<UserEnrollment> {

  @Override
  public List<UserEnrollment> getEntities(
      List<Result> results, List<Mutation> mutations) {

    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<UserEnrollment> enrollments = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<UserEnrollment> enrollmentOptional
          = getEntity(result, results, 0);

      if (enrollmentOptional.isPresent()) {
        enrollments.add(enrollmentOptional.get());
      }
    });
    return enrollments;
  }

  private Optional<UserEnrollment> getEntity(
      Result mainResult, List<Result> results, long timestamp) {

    String id = Bytes.toString(mainResult.getRow());
    User user = new User.Builder(
        getId(mainResult, timestamp),
        getFirstName(mainResult, timestamp),
        getLastName(mainResult, timestamp)
    ).build();

    List<Level> levels = getLevels(results, id, timestamp);
    List<Subject> subjects = getSubjects(results, id, timestamp);
    List<Course> courses = Collections.emptyList();
    List<Group> groups = getGroups(results, id, timestamp);

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

    UserEnrollment enrollment = new UserEnrollment.Builder(id, user)
        .levels(levels)
        .subjects(subjects)
        .courses(courses)
        .groups(groups)
        .createdBy(createdBy)
        .createdDateTime(createdDateTime)
        .modifiedBy(modifiedBy)
        .modifiedDateTime(modifiedDateTime)
        .build();
    return Optional.of(enrollment);
  }

  @Override
  public Optional<UserEnrollment> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results, 0);
  }

  @Override
  public List<Put> getPuts(UserEnrollment entity, long timestamp) {
    return Collections.emptyList();
  }

  @Override
  public List<Delete> getDeletes(UserEnrollment entity, long timestamp) {
    return Collections.emptyList();
  }
}
