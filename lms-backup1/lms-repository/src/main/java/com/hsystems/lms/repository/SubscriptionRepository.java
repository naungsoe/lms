package com.hsystems.lms.repository;

import com.hsystems.lms.repository.entity.Subscription;

import java.io.IOException;
import java.util.List;

/**
 * Created by naungsoe on 8/8/16.
 */
public interface SubscriptionRepository
    extends Repository<Subscription> {

  List<Subscription> findAllBy(String userId)
      throws IOException;
}