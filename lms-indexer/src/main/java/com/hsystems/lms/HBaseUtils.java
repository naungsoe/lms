//package com.hsystems.lms;
//
//import org.apache.hadoop.hbase.client.Result;
//import org.apache.hadoop.hbase.util.Bytes;
//
//import java.time.LocalDate;
//
///**
// * Created by administrator on 3/10/16.
// */
//public class HBaseUtils {
//
//  public static LocalDate getLocalDate(
//      Result result, String columnFamily, String identifier) {
//
//    String value = getString(result, columnFamily, identifier);
//    return DateTimeUtils.getDate(value);
//  }
//
//  public static String getString(
//      Result result, String columnFamily, String identifier) {
//
//    byte[] value = getValue(result, columnFamily, identifier);
//    return Bytes.toString(value);
//  }
//
//  public static boolean getBoolean(
//      Result result, String columnFamily, String identifier) {
//
//    byte[] value = getValue(result, columnFamily, identifier);
//    return Bytes.toBoolean(value);
//  }
//
//  public static int getInt(
//      Result result, String columnFamily, String identifier) {
//
//    byte[] value = getValue(result, columnFamily, identifier);
//    return Bytes.toInt(value);
//  }
//
//  public static byte[] getValue(
//      Result result, String columnFamily, String identifier) {
//
//    return result.getValue(Bytes.toBytes(columnFamily),
//        Bytes.toBytes(identifier));
//  }
//}
