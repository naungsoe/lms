package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.common.util.ReflectionUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.entity.Group;
import com.hsystems.lms.repository.entity.School;
import com.hsystems.lms.repository.entity.User;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 13/10/16.
 */
public abstract class HBaseRepository {

  public static final String SCAN_KEY_FORMAT = "^%s[_0-9a-zA-Z]*$";

  protected Scan getRowFilterScan(String key)
      throws IOException {

    String keyRegex = String.format(SCAN_KEY_FORMAT, key);
    Scan scan = new Scan(Bytes.toBytes(key));
    RegexStringComparator comparator = new RegexStringComparator(keyRegex);
    RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, comparator);
    scan.setFilter(filter);
    return scan;
  }
}
