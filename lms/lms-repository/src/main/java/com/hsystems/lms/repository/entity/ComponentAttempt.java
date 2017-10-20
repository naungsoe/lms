package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;

import java.time.LocalDateTime;

/**
 * Created by naungsoe on 6/1/17.
 */
@IndexCollection(namespace = "lms", name = "attempts")
public interface ComponentAttempt extends Entity {

  LocalDateTime getAttemptedDateTime();
}