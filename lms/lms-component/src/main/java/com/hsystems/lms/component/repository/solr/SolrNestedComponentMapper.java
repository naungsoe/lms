package com.hsystems.lms.component.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

public final class SolrNestedComponentMapper<T extends Component>
    implements Mapper<SolrDocument, Nested<T>> {

  private static final String PARENT_ID_FIELD = "parent_id";
  private static final String RESOURCE_ID_FIELD = "resource_id";

  private final T component;

  public SolrNestedComponentMapper(T component) {
    this.component = component;
  }

  @Override
  public Nested<T> from(SolrDocument source) {
    String parentId = SolrUtils.getString(source, PARENT_ID_FIELD);
    String resourceId = SolrUtils.getString(source, RESOURCE_ID_FIELD);
    Nested.Builder<T> builder = new Nested.Builder<>(component);
    return builder.parentId(parentId).resourceId(resourceId).build();
  }
}