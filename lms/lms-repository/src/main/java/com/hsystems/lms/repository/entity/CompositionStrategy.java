package com.hsystems.lms.repository.entity;

public interface CompositionStrategy<T extends Component> {

  void validate(T component);
}