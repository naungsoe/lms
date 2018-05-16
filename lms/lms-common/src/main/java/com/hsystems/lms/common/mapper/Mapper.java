package com.hsystems.lms.common.mapper;

public interface Mapper<S, T> {

  T from(S source);
}