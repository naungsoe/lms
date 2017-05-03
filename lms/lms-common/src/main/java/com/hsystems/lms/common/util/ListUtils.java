package com.hsystems.lms.common.util;

import java.util.List;

/**
 * Created by naungsoe on 12/9/16.
 */
public class ListUtils {

  public static <T> boolean isEmpty(List<T> list) {
    return (list == null) || list.isEmpty();
  }

  public static <T> boolean isNotEmpty(List<T> list) {
    return !isEmpty(list);
  }

  public static <T,S> boolean equals(List<T> list1, List<S> list2) {
    return list1.stream().anyMatch(item1 -> list2.stream()
        .anyMatch(item2 -> item2.equals(item1)));
  }
}
