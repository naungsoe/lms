package com.hsystems.lms.repository.solr;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.hsystems.lms.common.annotation.Log;
import com.hsystems.lms.common.query.Criterion;
import com.hsystems.lms.common.query.Query;
import com.hsystems.lms.common.query.QueryResult;
import com.hsystems.lms.repository.IndexRepository;
import com.hsystems.lms.repository.exception.RepositoryException;
import com.hsystems.lms.repository.solr.provider.SolrClient;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 10/8/16.
 */
@Singleton
public class SolrIndexRepository implements IndexRepository {

  private Provider<SolrClient> solrClientProvider;

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
      SolrQuery solrQuery = getSolrQuery(query);
      return client.query(solrQuery, type);

    } catch (SolrServerException | IOException
        | InstantiationException | IllegalAccessException
        | InvocationTargetException | NoSuchFieldException e) {

      throw new RepositoryException("error executing query", e);
    }
  }

  private SolrQuery getSolrQuery(Query query) {
    SolrQuery solrQuery = new SolrQuery();

    solrQuery.setQuery("*:*");

    List<Criterion> criteria = query.getCriteria();
    criteria.stream().forEach(x -> {
      switch (x.getOperator()) {
        case EQUAL:
          solrQuery.addFilterQuery(x.getField() + ":" + x.getValue());
          break;

        default:
          solrQuery.addFilterQuery(x.getField() + ":" + x.getValue());
          break;
      }
    });

    List<String> fields = query.getFields();
    fields.stream().forEach(x -> solrQuery.addField(x));

    Optional<Criterion> criterionOptional = criteria.stream()
        .filter(x -> "typeName_s".equals(x.getField())).findFirst();

    if (criterionOptional.isPresent()) {
      Criterion criterion = criterionOptional.get();
      solrQuery.addField(criterion.getField() + ":" + criterion.getValue());
    }

    solrQuery.setStart(query.getOffset());
    return solrQuery;
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
