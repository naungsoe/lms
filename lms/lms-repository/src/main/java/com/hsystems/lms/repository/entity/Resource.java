package com.hsystems.lms.repository.entity;

import java.util.Enumeration;

/**
 * Created by naungsoe on 5/11/16.
 */
public interface Resource extends SchoolScoped, Auditable {

  Enumeration<Level> getLevels();

  void addLevel(Level... levels);

  Enumeration<Subject> getSubjects();

  void addSubject(Subject... subjects);

  Enumeration<String> getKeywords();

  void addKeyword(String... keywords);

  Enumeration<Permission> getPermissions();

  void addPermission(Permission... permissions);

  Status getStatus();
}