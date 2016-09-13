package com.hsystems.lms.service.solr;

import com.hsystems.lms.MapperUtils;
import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.IndexingService;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.util.Map;

/**
 * Created by administrator on 10/9/16.
 */
public class SolrIndexingService implements IndexingService {

  public void index(Object entity) throws ServiceException {
    SolrClient client = getSolrClient();

    try {
      SolrInputDocument document = getDocument(entity);
      client.add(document);
      client.commit();
    } catch (Exception e) {
      throw new ServiceException("indexing failed", e);
    }
  }
  private SolrInputDocument getDocument(Object entity)
      throws IllegalAccessException {

    Map<String, Object> map = MapperUtils.getMap(entity);
    SolrInputDocument document = new SolrInputDocument();
    map.forEach((key, value) -> document.addField(key, value));
    return document;
  }

  private SolrClient getSolrClient() {
    String zkHostString = "vagrant-ubuntu-trusty-64:2181";
    return new CloudSolrClient.Builder().withZkHost(zkHostString).build();
  }
 }
