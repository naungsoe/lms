package com.hsystems.lms.service.solr;

import com.hsystems.lms.annotation.Log;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.search.Query;

/**
 * Created by administrator on 10/8/16.
 */
public final class SolrSearchService implements SearchService {

  SolrSearchService() {

  }

  @Log
  public String query(Query searchQuery)
      throws ServiceException {

    return "";
  }
}
