package com.hsystems.lms.repository.hbase.mapper;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.StringUtils;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/12/16.
 */
public abstract class HBaseAbstractMapper {

  protected static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  protected boolean getBoolean(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return StringUtils.isEmpty(value) ? false : Boolean.parseBoolean(value);
  }

  protected int getInteger(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
  }

  protected long getLong(
      Result result, byte[] family, byte[] qualifier) {

    String value = getString(result, family, qualifier);
    return StringUtils.isEmpty(value) ? 0 : Long.parseLong(value);
  }

  protected <T extends Enum<T>> T getEnum(
      Result result, byte[] family, byte[] qualifier, Class<T> type) {

    String value = getString(result, family, qualifier);
    return StringUtils.isEmpty(value) ? null : Enum.valueOf(type, value);
  }

  protected String getString(
      Result result, byte[] family, byte[] qualifier) {

    byte[] value = result.getValue(family, qualifier);
    return (value == null) ? "" : Bytes.toString(value);
  }

  protected LocalDateTime getLocalDateTime(
      Result result, byte[] family, byte[] qualifier) {

    String dateTime = getString(result, family, qualifier);
    return (StringUtils.isEmpty(dateTime)) ? LocalDateTime.MIN
        : DateTimeUtils.toLocalDateTime(dateTime, DATE_TIME_FORMAT);
  }

  protected Optional<Cell> getCellByTimestamp(
      List<Cell> cells, long timestamp) {

    return cells.stream().filter(cell -> cell.getTimestamp() == timestamp)
        .findFirst();
  }

  protected boolean getBoolean(List<Cell> cells, long timestamp) {
    String value = getString(cells, timestamp);
    return StringUtils.isEmpty(value) ? false : Boolean.parseBoolean(value);
  }

  protected int getInteger(List<Cell> cells, long timestamp) {
    String value = getString(cells, timestamp);
    return StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
  }

  protected long getLong(List<Cell> cells, long timestamp) {
    String value = getString(cells, timestamp);
    return StringUtils.isEmpty(value) ? 0 : Long.parseLong(value);
  }

  protected <T extends Enum<T>> T getEnum(
      List<Cell> cells, long timestamp, Class<T> type) {

    String value = getString(cells, timestamp);
    return StringUtils.isEmpty(value) ? null : Enum.valueOf(type, value);
  }

  protected String getString(List<Cell> cells, long timestamp) {
    Optional<Cell> cellOptional = getCellByTimestamp(cells, timestamp);

    if (!cellOptional.isPresent()) {
      return "";
    }

    byte[] value = CellUtil.cloneValue(cellOptional.get());
    return (value == null) ? "" : Bytes.toString(value);
  }

  protected LocalDateTime getLocalDateTime(List<Cell> cells, long timestamp) {
    String dateTime = getString(cells, timestamp);
    return (StringUtils.isEmpty(dateTime)) ? LocalDateTime.MIN
        : DateTimeUtils.toLocalDateTime(dateTime, DATE_TIME_FORMAT);
  }
}