package com.hsystems.lms.component;

import com.hsystems.lms.entity.Entity;

import java.io.Serializable;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class Nested<T extends Component>
    implements Entity, Serializable {

  private static final long serialVersionUID = -8899119052394850988L;

  private T component;

  private String parentId;

  private String resourceId;

  Nested() {

  }

  Nested(
      T component,
      String parentId,
      String resourceId) {

    this.component = component;
    this.parentId = parentId;
    this.resourceId = resourceId;
  }

  public static class Builder<T extends Component> {

    private T component;
    private String parentId;
    private String resourceId;

    public Builder(T component) {
      this.component = component;
    }

    public Builder parentId(String parentId) {
      this.parentId = parentId;
      return this;
    }

    public Builder resourceId(String resourceId) {
      this.resourceId = resourceId;
      return this;
    }

    public Nested build() {
      return new Nested(
          this.component,
          this.parentId,
          this.resourceId
      );
    }
  }

  @Override
  public String getId() {
    return component.getId();
  }

  public T getComponent() {
    return component;
  }

  public String getParentId() {
    return parentId;
  }

  public String getResourceId() {
    return resourceId;
  }
}