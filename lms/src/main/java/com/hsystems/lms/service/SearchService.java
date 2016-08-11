package com.hsystems.lms.service;

import com.hsystems.lms.service.search.Query;

/**
 * Created by administrator on 10/8/16.
 */
public interface SearchService {

  String query(Query searchQuery);
}
