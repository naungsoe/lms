package com.hsystems.lms.common.specification;

import java.util.List;

public abstract class CompositeSpecification<T> implements Specification<T> {

  protected List<Specification<T>> specifications;

  CompositeSpecification(List<Specification<T>> specifications) {
    this.specifications = specifications;
  }

  public List<Specification<T>> getSpecifications() {
    return specifications;
  }
}