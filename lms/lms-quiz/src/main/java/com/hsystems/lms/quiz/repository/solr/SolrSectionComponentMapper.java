package com.hsystems.lms.quiz.repository.solr;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.solr.SolrComponentMapper;
import com.hsystems.lms.component.repository.solr.SolrNestedComponentMapper;
import com.hsystems.lms.quiz.repository.entity.SectionComponent;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.Collections;
import java.util.List;

public final class SolrSectionComponentMapper
    implements SolrComponentMapper<SectionComponent> {

  private static final String ID_FIELD = "id";
  private static final String TITLE_FIELD = "title";
  private static final String INSTRUCTIONS_FIELD = "instructions";

  public SolrSectionComponentMapper() {

  }

  @Override
  public Nested<SectionComponent> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String title = SolrUtils.getString(source, TITLE_FIELD);
    String instructions = SolrUtils.getString(source, INSTRUCTIONS_FIELD);
    List<Component> components = Collections.emptyList();
    SectionComponent component
        = new SectionComponent(id, title, instructions, components);

    SolrNestedComponentMapper componentMapper
        = new SolrNestedComponentMapper(component);
    return componentMapper.from(source);
  }
}