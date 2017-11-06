package com.hsystems.lms.repository.entity.lesson;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.entity.Auditable;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Resource;
import com.hsystems.lms.repository.entity.ShareEntry;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@IndexCollection(namespace = "lms", name = "lessons")
public final class LessonResource
    implements Entity, Resource, Auditable, Serializable {

  private static final long serialVersionUID = -4508940747701896418L;

  @IndexField
  private String id;

  @IndexField
  private Lesson lesson;

  @IndexField
  private School school;

  @IndexField
  private List<Level> levels;

  @IndexField
  private List<Subject> subjects;

  @IndexField
  private List<String> keywords;

  @IndexField
  private List<ShareEntry> shareEntries;

  @IndexField
  private User createdBy;

  @IndexField
  private LocalDateTime createdDateTime;

  @IndexField
  private User modifiedBy;

  @IndexField
  private LocalDateTime modifiedDateTime;

  LessonResource() {

  }

  LessonResource(
      String id,
      Lesson lesson,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
      List<ShareEntry> shareEntries,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.lesson = lesson;
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.shareEntries = shareEntries;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  public static class Builder {

    private String id;
    private Lesson lesson;

    private School school;
    private List<Level> levels;
    private List<Subject> subjects;
    private List<String> keywords;
    private List<ShareEntry> shareEntries;
    private User createdBy;
    private LocalDateTime createdDateTime;
    private User modifiedBy;
    private LocalDateTime modifiedDateTime;

    public Builder(String id, Lesson lesson) {
      this.id = id;
      this.lesson = lesson;
    }

    public Builder school(School school) {
      this.school = school;
      return this;
    }

    public Builder levels(List<Level> levels) {
      this.levels = levels;
      return this;
    }

    public Builder subjects(List<Subject> subjects) {
      this.subjects = subjects;
      return this;
    }

    public Builder keywords(List<String> keywords) {
      this.keywords = keywords;
      return this;
    }

    public Builder shareEntries(List<ShareEntry> shareEntries) {
      this.shareEntries = shareEntries;
      return this;
    }

    public Builder createdBy(User createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public Builder createdDateTime(LocalDateTime createdDateTime) {
      this.createdDateTime = createdDateTime;
      return this;
    }

    public Builder modifiedBy(User modifiedBy) {
      this.modifiedBy = modifiedBy;
      return this;
    }

    public Builder modifiedDateTime(LocalDateTime modifiedDateTime) {
      this.modifiedDateTime = modifiedDateTime;
      return this;
    }

    public LessonResource build() {
      return new LessonResource(
          this.id,
          this.lesson,
          this.school,
          this.levels,
          this.subjects,
          this.keywords,
          this.shareEntries,
          this.createdBy,
          this.createdDateTime,
          this.modifiedBy,
          this.modifiedDateTime
      );
    }
  }

  @Override
  public String getId() {
    return id;
  }

  public Lesson getLesson() {
    return lesson;
  }

  @Override
  public School getSchool() {
    return school;
  }

  @Override
  public Enumeration<Level> getLevels() {
    return CollectionUtils.isEmpty(levels)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(levels);
  }

  @Override
  public void addLevel(Level... levels) {
    if (CollectionUtils.isEmpty(this.levels)) {
      this.levels = new ArrayList<>();
    }

    Arrays.stream(levels).forEach(this.levels::add);
  }

  @Override
  public Enumeration<Subject> getSubjects() {
    return CollectionUtils.isEmpty(subjects)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(subjects);
  }

  @Override
  public void addSubject(Subject... subjects) {
    if (CollectionUtils.isEmpty(this.subjects)) {
      this.subjects = new ArrayList<>();
    }

    Arrays.stream(subjects).forEach(this.subjects::add);
  }

  @Override
  public Enumeration<String> getKeywords() {
    return CollectionUtils.isEmpty(keywords)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(keywords);
  }

  @Override
  public void addKeyword(String... keywords) {
    if (CollectionUtils.isEmpty(this.keywords)) {
      this.keywords = new ArrayList<>();
    }

    Arrays.stream(keywords).forEach(this.keywords::add);
  }

  @Override
  public Enumeration<ShareEntry> getShareEntries() {
    return CollectionUtils.isEmpty(shareEntries)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(shareEntries);
  }

  @Override
  public void addShareEntry(ShareEntry... shareEntries) {
    if (CollectionUtils.isEmpty(this.shareEntries)) {
      this.shareEntries = new ArrayList<>();
    }

    Arrays.stream(shareEntries).forEach(this.shareEntries::add);
  }

  @Override
  public User getCreatedBy() {
    return createdBy;
  }

  @Override
  public LocalDateTime getCreatedDateTime() {
    return createdDateTime;
  }

  @Override
  public User getModifiedBy() {
    return modifiedBy;
  }

  @Override
  public LocalDateTime getModifiedDateTime() {
    return modifiedDateTime;
  }
}