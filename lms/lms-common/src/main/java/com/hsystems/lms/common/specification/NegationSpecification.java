package com.hsystems.lms.common.specification;

public final class NegationSpecification<T> implements Specification<T> {

  private Specification<T> specification;

  public NegationSpecification(Specification<T> specification) {
    this.specification = specification;
  }

  @Override
  public boolean isSatisfiedBy(T candidate) {
    return !specification.isSatisfiedBy(candidate);
  }
}