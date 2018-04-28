package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexDocument;

import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexDocument(namespace = "lms", collection = "assignments")
public interface Assignment extends Entity, SchoolScoped {

  Enumeration<Group> getGroups();

  void addGroup(Group... groups);

  void removeGroup(Group group);

  LocalDateTime getStartDateTime();

  LocalDateTime getEndDateTime();

  LocalDateTime getReleaseDateTime();
}
