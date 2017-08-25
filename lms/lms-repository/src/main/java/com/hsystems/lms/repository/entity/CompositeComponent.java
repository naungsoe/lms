package com.hsystems.lms.repository.entity;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface CompositeComponent extends Component {

  Enumeration<Component> getComponents();

  void addComponent(Component... components);

  void removeComponent(Component component);
}
