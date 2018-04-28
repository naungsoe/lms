package com.hsystems.lms.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by naungsoe on 13/8/16.
 */
public final class ReflectionUtils {

  public static <T> boolean isInstantiable(Class<T> type) {
    int modifiers = type.getModifiers();
    return !Modifier.isInterface(modifiers)
        && !Modifier.isAbstract(modifiers);
  }

  public static Map<String, Class<?>> getClasses(String packageName)
      throws ClassNotFoundException, IOException {

    String packagePath = packageName.replace('.', '/');
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    String resourcePath = classLoader.getResource(packagePath).getPath();
    String jarPath = resourcePath.substring(5, resourcePath.indexOf('!'));
    InputStream inputStream = new FileInputStream(jarPath);
    JarInputStream jarFile = new JarInputStream(inputStream);
    Map<String, Class<?>> classMap = new HashMap<>();

    while (true) {
      JarEntry jarEntry = jarFile.getNextJarEntry();

      if (jarEntry == null) {
        break;
      }

      String entryPath = jarEntry.getName();

      if (entryPath.startsWith(packagePath) && entryPath.endsWith(".class")) {
        String typeName = entryPath.replace('/', '.')
            .substring(0, entryPath.indexOf(".class"));
        String simpleName = typeName.substring(typeName.lastIndexOf('.') + 1);
        classMap.put(simpleName, Class.forName(typeName));
      }
    }

    return classMap;
  }

  public static Object getInstance(String typeName, Object... initargs) {
    try {
      Class<?> type = Class.forName(typeName);
      return getInstance(type, initargs);

    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(
          "error creating instance", e);
    }
  }

  public static Object getInstance(Class type, Object... initargs) {
    Optional<Constructor> constructor = getParamLessConstructor(type);

    try {
      constructor.get().setAccessible(true);
      return constructor.get().newInstance(initargs);

    } catch (InstantiationException | IllegalAccessException
        | InvocationTargetException e) {

      throw new IllegalArgumentException(
          "error creating instance", e);
    }
  }

  public static Optional<Constructor> getParamLessConstructor(Class type) {
    Constructor[] constructors = type.getDeclaredConstructors();
    return Arrays.asList(constructors).stream()
        .filter(isParameterlessConstructor()).findFirst();
  }

  private static Predicate<Constructor> isParameterlessConstructor() {
    return constructor -> constructor.getGenericParameterTypes().length == 0;
  }

  public static <T> String getString(T instance, String fieldName) {
    return ReflectionUtils.getValue(instance, fieldName, String.class);
  }

  public static <T> Object getValue(T instance, String fieldName) {
    return getValue(instance, fieldName, Object.class);
  }

  public static <T, S> S getValue(T instance, String fieldName, Class<S> type) {
    Optional<Field> fieldOptional = getField(instance.getClass(), fieldName);
    return getValue(instance, fieldOptional.get(), type);
  }

  public static <T, S> S getValue(T instance, Field field, Class<S> type) {
    field.setAccessible(true);

    try {
      Object value = field.get(instance);
      return type.cast(value);

    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(
          "error getting field value", e);
    }
  }

  public static <T, S> void setValue(T instance, String fieldName, S value) {
    Optional<Field> fieldOptional = getField(instance.getClass(), fieldName);

    if (fieldOptional.isPresent()) {
      try {
        Field field = fieldOptional.get();
        field.setAccessible(true);
        field.set(instance, value);

      } catch (IllegalAccessException e) {
        throw new IllegalArgumentException(
            "error setting field value", e);
      }
    }
  }

  public static <T, S> void setValue(T instance, Field field, S value) {
    try {
      field.setAccessible(true);
      field.set(instance, value);

    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(
          "error setting field value", e);
    }
  }

  public static <T> Optional<Field> getField(Class<T> type, String name) {
    List<Field> fields = getFields(type);
    return fields.stream().filter(isField(name)).findFirst();
  }

  private static Predicate<Field> isField(String name) {
    return field -> field.getName().equals(name);
  }

  public static <T> List<Field> getFields(Class<T> type) {
    List<Field> fields = new ArrayList<>();
    List<Field> declaredFields = Arrays.asList(type.getDeclaredFields());
    declaredFields.stream().filter(isNonStatic()).forEach(fields::add);

    if (type.getSuperclass() != null) {
      fields.addAll(getFields(type.getSuperclass()));
    }

    return fields;
  }

  private static Predicate<Field> isNonStatic() {
    return field -> !Modifier.isStatic(field.getModifiers());
  }

  public static <T> Class<?> getListType(Field field) {
    ParameterizedType paramType = (ParameterizedType) field.getGenericType();
    return (Class<?>) paramType.getActualTypeArguments()[0];
  }

  public static <T> Class<?> getEnumerationType(Field field) {
    ParameterizedType paramType = (ParameterizedType) field.getGenericType();
    return (Class<?>) paramType.getActualTypeArguments()[0];
  }
}