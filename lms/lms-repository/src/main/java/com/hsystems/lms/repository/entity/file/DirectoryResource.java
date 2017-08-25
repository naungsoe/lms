package com.hsystems.lms.repository.entity.file;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.common.util.StringUtils;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
public class DirectoryResource extends FileResource implements Serializable {

  private static final long serialVersionUID = -5311103954407482435L;

  @IndexField
  private List<FileResource> files;

  DirectoryResource() {

  }

  public DirectoryResource(
      String id,
      String name,
      String type,
      long size,
      List<FileResource> files) {

    this.id = id;
    this.name = name;
    this.type = type;
    this.size = size;
    this.files = files;
  }

  public DirectoryResource(
      String id,
      String name,
      String type,
      long size,
      List<FileResource> files,
      School school,
      List<Level> levels,
      List<Subject> subjects,
      List<String> keywords,
      User createdBy,
      LocalDateTime createdDateTime,
      User modifiedBy,
      LocalDateTime modifiedDateTime) {

    this.id = id;
    this.name = name;
    this.type = type;
    this.size = size;
    this.files = files;
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
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

    DirectoryResource file = (DirectoryResource) obj;
    return id.equals(file.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "DirectoryResource{id=%s, name=%s, type=s%, size=%s, "
            + "files=%s, createdBy=%s, createdDateTime=%s, "
            + "modifiedBy=%s, modifiedDateTime=%s}",
        id, name, type, size, StringUtils.join(files, ","),
        createdBy, createdDateTime, modifiedBy, modifiedDateTime);
  }
}
