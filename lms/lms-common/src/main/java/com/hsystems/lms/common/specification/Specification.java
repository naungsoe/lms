package com.hsystems.lms.common.specification;

public interface Specification<T> {

  boolean isSatisfiedBy(T candidate);

  default Specification<T> and(Specification<T> other) {
    return new ConjunctionSpecification<>(this, other);
  }

  default Specification<T> or(Specification<T> other) {
    return new DisjunctionSpecification<>(this, other);
  }

  default Specification<T> not() {
    return new NegationSpecification<>(this);
  }
}