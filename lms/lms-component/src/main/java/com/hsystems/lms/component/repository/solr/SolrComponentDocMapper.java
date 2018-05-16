package com.hsystems.lms.component.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;

import org.apache.solr.common.SolrInputDocument;

/**
 * Created by naungsoe on 12/10/16.
 */
public interface SolrComponentDocMapper
    extends Mapper<Nested<Component>, SolrInputDocument> {

}