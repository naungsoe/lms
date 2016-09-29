package com.hsystems.lms.service.solr;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.annotation.Log;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.provider.solr.SolrClient;
import com.hsystems.lms.service.IndexingService;

/**
 * Created by administrator on 10/8/16.
 */
@Singleton
public class SolrIndexingService implements IndexingService {

  private Provider<SolrClient> clientProvider;

  @Inject
  SolrIndexingService(Provider<SolrClient> clientProvider) {
    this.clientProvider = clientProvider;
  }

  @Log
  @Override
  public <T> void index(T entity)
      throws ServiceException {

  }
}
