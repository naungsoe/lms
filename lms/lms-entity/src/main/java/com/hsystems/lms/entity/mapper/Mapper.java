package com.hsystems.lms.entity.mapper;

public interface Mapper<T,S> {

  T from(S source);
}