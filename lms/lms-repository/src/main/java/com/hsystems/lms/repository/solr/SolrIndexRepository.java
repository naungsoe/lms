package com.hsystems.lms.repository.solr;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.solr.provider.SolrClient;

import java.io.IOException;

/**
 * Created by naungsoe on 10/8/16.
 */
public class SolrIndexRepository implements IndexRepository {

  private final Provider<SolrClient> solrClientProvider;

  @Inject
  SolrIndexRepository(Provider<SolrClient> solrClientProvider) {
    this.solrClientProvider = solrClientProvider;
  }

  @Override
  public <T> QueryResult<T> findAllBy(Query query, Class<T> type)
      throws IOException {


    SolrClient client = solrClientProvider.get();
    return client.query(query, type);
  }

  @Override
  public <T> void save(T model)
      throws IOException {

    SolrClient client = solrClientProvider.get();
    client.index(model);
  }
}
