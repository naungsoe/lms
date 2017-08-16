package com.hsystems.lms.repository.entity;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(namespace = "lms", name = "files")
public class FileResource extends Resource implements Serializable {

  private static final long serialVersionUID = -4200928490264612997L;

  @IndexField
  private String id;

  @IndexField
  private String name;

  @IndexField
  private String type;

  @IndexField
  private long size;

  @IndexField
  private boolean directory;

  @IndexField
  private List<FileResource> files;

  FileResource() {

  }

  public FileResource(
      String id,
      String name,
      String type,
      long size,
      boolean directory,
      List<FileResource> files) {

    this.id = id;
    this.name = name;
    this.type = type;
    this.size = size;
    this.directory = directory;
    this.files = files;
  }

  public FileResource(
      String id,
      String name,
      String type,
      long size,
      boolean directory,
      List<FileResource> files,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
      List<AccessControl> accessControls,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.name = name;
    this.type = type;
    this.size = size;
    this.directory = directory;
    this.files = files;
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.accessControls = accessControls;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public long getSize() {
    return size;
  }

  public boolean isDirectory() {
    return directory;
  }

  public Enumeration<FileResource> getFiles() {
    return CollectionUtils.isEmpty(files)
        ? Collections.emptyEnumeration()
        : Collections.enumeration(files);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }

    FileResource file = (FileResource) obj;
    return id.equals(file.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "FileResource{id=%s, name=%s, type=s%, size=%s, directory=%s, "
            + "files=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, name, type, size, directory, StringUtils.join(files, ","),
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
