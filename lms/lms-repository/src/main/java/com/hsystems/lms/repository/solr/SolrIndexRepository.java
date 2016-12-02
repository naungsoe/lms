package com.hsystems.lms.repository.solr;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.solr.provider.SolrClient;

import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by naungsoe on 10/8/16.
 */
public class SolrIndexRepository implements IndexRepository {

  private final Provider<SolrClient> solrClientProvider;

  @Inject
  SolrIndexRepository(Provider<SolrClient> solrClientProvider) {
    this.solrClientProvider = solrClientProvider;
  }

  @Log
  @Override
  public <T> QueryResult<T> findAllBy(Query query, Class<T> type)
      throws RepositoryException {

    try {
      SolrClient client = solrClientProvider.get();
      return client.query(query, type);

    } catch (SolrServerException | IOException
        | InstantiationException | IllegalAccessException
        | InvocationTargetException | NoSuchFieldException e) {

      throw new RepositoryException("error executing query", e);
    }
  }

  @Log
  @Override
  public <T> void save(T model)
      throws RepositoryException {

    try {
      SolrClient client = solrClientProvider.get();
      client.index(model);

    } catch (SolrServerException | IOException
        | NoSuchFieldException | IllegalAccessException e) {

      throw new RepositoryException("error indexing model", e);
    }
  }
}
