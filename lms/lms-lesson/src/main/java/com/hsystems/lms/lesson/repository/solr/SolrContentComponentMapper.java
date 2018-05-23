package com.hsystems.lms.lesson.repository.solr;

import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.component.repository.solr.SolrNestedComponentMapper;
import com.hsystems.lms.lesson.repository.entity.ContentComponent;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

public final class SolrContentComponentMapper
    implements SolrComponentMapper<ContentComponent> {

  private static final String ID_FIELD = "id";
  private static final String CONTENT_FIELD = "content";

  public SolrContentComponentMapper() {

  }

  @Override
  public Nested<ContentComponent> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String content = SolrUtils.getString(source, CONTENT_FIELD);
    ContentComponent component = new ContentComponent(id, content);

    SolrNestedComponentMapper componentMapper
        = new SolrNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}