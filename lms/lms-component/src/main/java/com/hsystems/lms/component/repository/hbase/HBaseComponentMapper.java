package com.hsystems.lms.component.repository.hbase;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;

import org.apache.hadoop.hbase.client.Result;

import java.util.List;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface HBaseComponentMapper
    extends Mapper<List<Result>, Nested<Component>> {

}