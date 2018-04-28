package com.hsystems.lms.repository.entity.lesson;

import com.hsystems.lms.common.annotation.IndexField;
import com.hsystems.lms.repository.entity.Component;

/**
 * Created by naungsoe on 19/12/16.
 */
public final class ContentComponent implements Component {

  private static final long serialVersionUID = 3281066489580853143L;

  @IndexField
  protected String id;

  @IndexField
  private String content;

  @IndexField
  protected int order;

  ContentComponent() {

  }

  public ContentComponent(
      String id,
      String content,
      int order) {

    this.id = id;
    this.content = content;
    this.order = order;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  @Override
  public int getOrder() {
    return order;
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

    ContentComponent component = (ContentComponent) obj;
    return id.equals(component.getId());
  }

  @Override
  public String toString() {
    return String.format(
        "ContentComponent{id=%s, content=%s, order=%s}",
        id, content, order);
  }
}
