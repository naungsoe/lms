package com.hsystems.lms.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by naungsoe on 25/8/16.
 */
public final class JsonUtils {

  public static JsonNode parseJson(InputStream stream)
      throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    return mapper.readTree(stream);
  }
}