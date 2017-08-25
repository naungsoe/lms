package com.hsystems.lms.repository.entity.file;

import com.hsystems.lms.common.annotation.IndexCollection;
import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Level;
import com.hsystems.lms.repository.entity.Resource;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.Subject;
import com.hsystems.lms.repository.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by naungsoe on 5/11/16.
 */
@IndexCollection(namespace = "lms", name = "files")
public class FileResource extends Resource
    implements Serializable {

  private static final long serialVersionUID = 1687370342040613731L;

  @IndexField
  protected String name;

  @IndexField
  protected String type;

  @IndexField
  protected long size;

  FileResource() {

  }

  public FileResource(
      String id,
      String name,
      String type,
      long size) {

    this.id = id;
    this.name = name;
    this.type = type;
    this.size = size;
  }

  public FileResource(
      String id,
      String name,
      String type,
      long size,
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
    this.school = school;
    this.levels = levels;
    this.subjects = subjects;
    this.keywords = keywords;
    this.createdBy = createdBy;
    this.createdDateTime = createdDateTime;
    this.modifiedBy = modifiedBy;
    this.modifiedDateTime = modifiedDateTime;
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
        "FileResource{id=%s, name=%s, type=s%, size=%s, createdBy=%s, "
            + "createdDateTime=%s, modifiedBy=%s, modifiedDateTime=%s}",
        id, name, type, size, createdBy, createdDateTime,
        modifiedBy, modifiedDateTime);
  }
}
