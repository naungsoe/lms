package com.hsystems.lms.repository.hbase.patch;

import com.hsystems.lms.repository.entity.User;

import java.io.Serializable;
import java.util.List;

public class HBasePatch implements Serializable {

  private List<HBaseOperation> operations;

  private User modifiedBy;
}