package com.hsystems.lms.service.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 10/8/16.
 */
public final class Query {

  private final String docType;

  private final String keyword;

  private final List<Criterion> criteria;

  public Query(String docType, String keyword) {
    this(docType, keyword, new ArrayList<Criterion>());
  }

  public Query(String docType, String keyword, List<Criterion> criteria) {
    this.docType = docType;
    this.keyword = keyword;
    this.criteria = criteria;
  }

  public void addCriterion(Criterion criterion) {
    criteria.add(criterion);
  }
}
