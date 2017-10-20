package com.hsystems.lms.repository.entity;

public interface CompositionStrategy<T> {

  void validate(T component)
      throws IllegalArgumentException;
}
