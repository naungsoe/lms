package com.hsystems.lms.component.repository.solr;

import org.apache.solr.common.SolrDocument;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface SolrComponentMapperFactory {

  SolrComponentMapper create(SolrDocument document);
}