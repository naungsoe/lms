package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;

import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(namespace = "lms", name = "assignments")
public interface Assignment extends Entity, SchoolScoped {

  Enumeration<Group> getGroups();

  void addGroup(Group... groups);

  void removeGroup(Group group);

  LocalDateTime getStartDateTime();

  LocalDateTime getEndDateTime();

  LocalDateTime getReleaseDateTime();
}
