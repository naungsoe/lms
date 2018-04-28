package com.hsystems.lms.entity.component;

public interface CompositionStrategy<T extends Component> {

  void validate(T component);
}