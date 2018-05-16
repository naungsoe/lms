package com.hsystems.lms.common.specification;

import java.util.Arrays;

public final class ConjunctionSpecification<T>
    extends CompositeSpecification<T> {

  public ConjunctionSpecification(
      Specification<T> firstSpecification,
      Specification<T> secondSpecification) {

    super(Arrays.asList(firstSpecification, secondSpecification));
  }

  @Override
  public boolean isSatisfiedBy(T candidate) {
    return specifications.get(0).isSatisfiedBy(candidate)
        && specifications.get(1).isSatisfiedBy(candidate);
  }
}