package com.hsystems.lms.component;

import java.util.Enumeration;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface CompositeComponent<T extends Component> extends Component {

  Enumeration<T> getComponents();

  void addComponent(T... components);

  void removeComponent(T component);
}