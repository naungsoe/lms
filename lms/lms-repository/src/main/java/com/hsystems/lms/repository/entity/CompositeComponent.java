package com.hsystems.lms.repository.entity;

import java.util.List;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface CompositeComponent extends Component {

  List<Component> getComponents();

  void addComponent(Component... component);
}