package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Subscription;
import com.hsystems.lms.repository.entity.User;
import com.hsystems.lms.repository.entity.course.Course;
import com.hsystems.lms.repository.entity.course.CourseResource;

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
public class HBaseSubscriptionMapper extends HBaseEntityMapper<Subscription> {

  @Override
  public List<Subscription> getEntities(List<Result> results) {
    if (CollectionUtils.isEmpty(results)) {
      return Collections.emptyList();
    }

    List<Subscription> subscriptions = new ArrayList<>();
    results.stream().filter(isMainResult()).forEach(result -> {
      Optional<Subscription> subscriptionOptional
          = getEntity(result, results);

      if (subscriptionOptional.isPresent()) {
        subscriptions.add(subscriptionOptional.get());
      }
    });

    return subscriptions;
  }

  private Optional<Subscription> getEntity(
      Result mainResult, List<Result> results) {

    String id = Bytes.toString(mainResult.getRow());
    Course course = new Course(
        getTitle(mainResult),
        getDescription(mainResult),
        Collections.emptyList()
    );
    CourseResource resource = new CourseResource.Builder(
        getId(mainResult),
        course
    ).build();

    Result subscribedByResult = results.stream()
        .filter(isSubscribedByResult(id)).findFirst().get();
    User subscribedBy = getSubscribedBy(subscribedByResult);
    LocalDateTime subscribedDateTime = getDateTime(subscribedByResult);

    Subscription subscription = new Subscription(
        id, resource, subscribedBy, subscribedDateTime);
    return Optional.of(subscription);
  }

  @Override
  public Optional<Subscription> getEntity(List<Result> results) {
    Result mainResult = results.stream()
        .filter(isMainResult()).findFirst().get();
    return getEntity(mainResult, results);
  }
}
