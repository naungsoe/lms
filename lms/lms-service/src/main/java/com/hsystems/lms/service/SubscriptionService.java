package com.hsystems.lms.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.entity.Subscription;
import com.hsystems.lms.service.mapper.Configuration;
import com.hsystems.lms.service.model.SubscriptionModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by naungsoe on 8/8/16.
 */
public class SubscriptionService extends AbstractService {

  private final Provider<Properties> propertiesProvider;

  private final IndexRepository indexRepository;

  @Inject
  SubscriptionService(
      Provider<Properties> propertiesProvider,
      IndexRepository indexRepository) {

    this.propertiesProvider = propertiesProvider;
    this.indexRepository = indexRepository;
  }

  @Log
  public List<SubscriptionModel> findAllBy(String id, Principal principal)
      throws IOException {

    Query query = Query.create();
    query.addCriterion(Criterion.createEqual("subscribedBy.id", id));
    QueryResult<Subscription> queryResult
        = indexRepository.findAllBy(query, Subscription.class);
    List<Subscription> subscriptions = queryResult.getItems();

    if (CollectionUtils.isEmpty(subscriptions)) {
      return Collections.emptyList();
    }

    Configuration configuration = Configuration.create(principal);
    return getSubscriptionModels(subscriptions, configuration);
  }

  private List<SubscriptionModel> getSubscriptionModels(
      List<Subscription> subscriptions, Configuration configuration) {

    List<SubscriptionModel> subscriptionModels = new ArrayList<>();

    for (Subscription subscription : subscriptions) {
      SubscriptionModel subscriptionModel
          = getSubscriptionModel(subscription, configuration);
      subscriptionModels.add(subscriptionModel);
    }

    return subscriptionModels;
  }

  private SubscriptionModel getSubscriptionModel(
      Subscription subscription, Configuration configuration) {

    SubscriptionModel subscriptionModel = getModel(subscription,
        SubscriptionModel.class, configuration);
    String timeFormat = configuration.getTimeFormat();
    String dateFormat = configuration.getDateFormat();
    LocalDateTime subscribedDateTime = subscription.getSubscribedDateTime();
    String subscribedTime = DateTimeUtils.toString(
        subscribedDateTime, timeFormat);
    String subscribedDate = DateTimeUtils.toString(
        subscribedDateTime, dateFormat);
    subscriptionModel.setSubscribedTime(subscribedTime);
    subscriptionModel.setSubscribedDate(subscribedDate);
    return subscriptionModel;
  }
}