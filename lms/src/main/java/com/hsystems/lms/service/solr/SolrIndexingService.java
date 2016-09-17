package com.hsystems.lms.service.solr;

import com.hsystems.lms.MappingUtils;
import com.hsystems.lms.annotation.Log;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.IndexingService;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.Map;

/**
 * Created by administrator on 10/9/16.
 */
public class SolrIndexingService implements IndexingService {

  @Log
  public <T> void index(T entity)
      throws ServiceException {

    SolrClient client = getSolrClient();

    try {
      SolrInputDocument document = getDocument(entity);
      client.add(document);
      client.commit();
    } catch (IllegalAccessException | IOException
        | SolrServerException e) {

      throw new ServiceException(
          "error indexing entity", e);
    }
  }

  private <T> SolrInputDocument getDocument(T entity)
      throws IllegalAccessException {

    Map<String, Object> map = MappingUtils.getMap(entity);
    SolrInputDocument document = new SolrInputDocument();
    map.forEach((key, value) -> document.addField(key, value));
    return document;
  }

  private SolrClient getSolrClient() {
    String zkHostString = "vagrant-ubuntu-trusty-64:2181";
    return new CloudSolrClient.Builder().withZkHost(zkHostString).build();
  }
 }
