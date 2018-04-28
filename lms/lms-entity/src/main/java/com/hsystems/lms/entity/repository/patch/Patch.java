package com.hsystems.lms.entity.repository.patch;

import com.hsystems.lms.common.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public final class Patch {

  private String documentId;

  private List<Operation> operations;

  Patch() {

  }

  public Patch(
      String documentId,
      List<Operation> operations) {

    this.documentId = documentId;
    this.operations = operations;
  }

  public String getDocumentId() {
    return documentId;
  }

  public List<Operation> getOperations() {
    return CollectionUtils.isEmpty(operations)
        ? Collections.emptyList() : operations;
  }
}
