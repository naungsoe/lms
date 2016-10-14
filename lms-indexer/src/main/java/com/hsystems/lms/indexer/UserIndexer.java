package com.hsystems.lms.indexer;

import com.hsystems.lms.exception.IndexerException;

/**
 * Created by administrator on 4/10/16.
 */
public interface UserIndexer {

  void index() throws IndexerException;
}
