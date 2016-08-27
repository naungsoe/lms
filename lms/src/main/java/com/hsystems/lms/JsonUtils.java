package com.hsystems.lms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Created by administrator on 25/8/16.
 */
public final class JsonUtils {

  public static JsonObject parseJson(InputStream stream) {
    JsonReader jsonReader = Json.createReader(stream);
    JsonObject object = jsonReader.readObject();
    jsonReader.close();
    return object;
  }
}
