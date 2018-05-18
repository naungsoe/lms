package com.hsystems.lms.component;

import java.util.ArrayList;
import java.util.List;

public final class ComponentUtils {

  public static List<Component> organize(
      String parentId, List<Nested<Component>> components) {

    List<Component> list = new ArrayList<>();

    for (Nested<Component> component : components) {
      if (parentId.equals(component.getParentId())) {
        Component item = component.getComponent();
        list.add(item);

        if (item instanceof CompositeComponent) {
          CompositeComponent composite = (CompositeComponent) item;
          List<Component> elements = organize(item.getId(), components);
          composite.addComponent(elements.toArray(new Component[]{}));
        }
      }
    }

    return list;
  }
}
