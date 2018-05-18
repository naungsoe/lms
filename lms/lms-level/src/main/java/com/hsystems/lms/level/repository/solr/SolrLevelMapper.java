package com.hsystems.lms.level.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.level.repository.entity.Level;
import com.hsystems.lms.school.repository.entity.School;
import com.hsystems.lms.school.repository.solr.SolrAuditableMapper;
import com.hsystems.lms.school.repository.solr.SolrSchoolRefMapper;
import com.hsystems.lms.solr.SolrUtils;

import org.apache.solr.common.SolrDocument;

import java.util.Optional;

public final class SolrLevelMapper
    implements Mapper<SolrDocument, Auditable<Level>> {

  private static final String ID_FIELD = "id";
  private static final String NAME_FIELD = "name";

  public SolrLevelMapper() {

  }

  @Override
  public Auditable<Level> from(SolrDocument source) {
    String id = SolrUtils.getString(source, ID_FIELD);
    String name = SolrUtils.getString(source, NAME_FIELD);
    Level.Builder builder = new Level.Builder(id, name);

    SolrSchoolRefMapper schoolRefMapper = new SolrSchoolRefMapper(id);
    Optional<School> schoolOptional = schoolRefMapper.from(source);

    if (schoolOptional.isPresent()) {
      builder.school(schoolOptional.get());
    }

    Level level = builder.build();
    SolrAuditableMapper<Level> auditableMapper
        = new SolrAuditableMapper<>(level, id);
    return auditableMapper.from(source);
  }
}