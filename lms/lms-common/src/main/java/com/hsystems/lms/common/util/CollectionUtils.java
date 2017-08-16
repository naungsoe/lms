package com.hsystems.lms.common.util;

import java.util.Enumeration;
import java.util.List;
import java.util.Queue;

/**
 * Created by naungsoe on 12/9/16.
 */
public class CollectionUtils {

  public static <T> boolean isEmpty(Enumeration<T> enumeration) {
    return (enumeration == null) || !enumeration.hasMoreElements();
  }

  public static <T> boolean isEmpty(List<T> list) {
    return (list == null) || list.isEmpty();
  }

  public static <T> boolean isEmpty(Queue<T> queue) {
    return (queue == null) || queue.isEmpty();
  }

  public static <T> boolean isNotEmpty(Enumeration<T> enumeration) {
    return !isEmpty(enumeration);
  }

  public static <T> boolean isNotEmpty(List<T> list) {
    return !isEmpty(list);
  }

  public static <T> boolean isNotEmpty(Queue<T> queue) {
    return !isEmpty(queue);
  }

  public static <T,S> boolean equals(List<T> list1, List<S> list2) {
    return list1.stream().anyMatch(item1 -> list2.stream()
        .anyMatch(item2 -> item2.equals(item1)));
  }
}
