package com.hsystems.lms.component;

    import com.hsystems.lms.entity.Entity;

/**
 * Created by naungsoe on 6/1/17.
 */
public final class Nested<T extends Component> implements Entity {

  private T component;

  private String resourceId;

  private String parentId;

  Nested() {

  }

  public Nested(
      T component,
      String resourceId,
      String parentId) {

    this.component = component;
    this.resourceId = resourceId;
    this.parentId = parentId;
  }

  @Override
  public String getId() {
    return component.getId();
  }

  public T getComponent() {
    return component;
  }

  public String getResourceId() {
    return resourceId;
  }

  public String getParentId() {
    return parentId;
  }
}