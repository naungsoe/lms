package com.hsystems.lms.service.solr;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.provider.solr.SolrClient;
import com.hsystems.lms.service.IndexingService;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

/**
 * Created by naungsoe on 10/8/16.
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
  public <T> void index(T model)
      throws ServiceException {

    try {
      SolrClient client = clientProvider.get();
      client.index(model);

    } catch (SolrServerException | IOException
        | NoSuchFieldException | IllegalAccessException e) {

      throw new ServiceException("error indexing model", e);
    }
  }
}
