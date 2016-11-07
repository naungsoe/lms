package com.hsystems.lms.service.solr;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.service.annotation.Log;
import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.provider.solr.SolrClient;
import com.hsystems.lms.service.SearchService;
import com.hsystems.lms.service.query.Criterion;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by naungsoe on 10/8/16.
 */
@Singleton
public class SolrSearchService implements SearchService {

  private Provider<SolrClient> solrClientProvider;

  @Inject
  SolrSearchService(Provider<SolrClient> solrClientProvider) {
    this.solrClientProvider = solrClientProvider;
  }

  @Log
  @Override
  public <T> List<T> query(List<Criterion> criteria, Class<T> type)
      throws ServiceException {

    try {
      SolrClient client = solrClientProvider.get();
      SolrQuery query = getSolrQuery(criteria);
      return client.query(query, type);

    } catch (SolrServerException | IOException
        | InstantiationException | IllegalAccessException
        | InvocationTargetException e) {

      throw new ServiceException("error executing query", e);
    }
  }

  private SolrQuery getSolrQuery(List<Criterion> criteria) {
    SolrQuery query = new SolrQuery();
    return query;
  }
}
