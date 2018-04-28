package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;

import java.time.LocalDateTime;

/**
 * Created by naungsoe on 6/1/17.
 */
@IndexDocument(namespace = "lms", collection = "attempts")
public interface ComponentAttempt extends Entity {

  LocalDateTime getAttemptedDateTime();
}