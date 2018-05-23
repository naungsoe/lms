package com.hsystems.lms.question.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.question.repository.entity.Question;

import org.apache.solr.common.SolrInputDocument;

public abstract class SolrQuestionDocMapper<T extends Question>
    implements Mapper<T, SolrInputDocument> {

}