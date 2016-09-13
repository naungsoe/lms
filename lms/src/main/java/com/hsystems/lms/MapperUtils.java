package com.hsystems.lms;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;

/**
 * Created by administrator on 10/9/16.
 */
public final class MapperUtils {

  public static Map<String, Object> getMap(Object entity) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(entity, Map.class);
  }
}
