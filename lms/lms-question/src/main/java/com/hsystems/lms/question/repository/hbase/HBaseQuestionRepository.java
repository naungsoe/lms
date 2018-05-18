package com.hsystems.lms.question.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.ComponentUtils;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.question.repository.entity.QuestionResource;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class HBaseQuestionRepository
    implements Repository<Auditable<QuestionResource>> {

  private static final String QUESTION_TABLE = "lms:questions";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseComponentRepository componentRepository;

  @Inject
  HBaseQuestionRepository(
      HBaseClient hbaseClient,
      HBaseComponentRepository componentRepository) {

    this.hbaseClient = hbaseClient;
    this.componentRepository = componentRepository;
  }

  public List<Auditable<QuestionResource>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, QUESTION_TABLE);
    List<Auditable<QuestionResource>> resources = new ArrayList<>();

    for (Result result : results) {
      resources.add(process(result));
    }

    return resources;
  }

  private Auditable<QuestionResource> process(Result result)
      throws IOException {

    String id = Bytes.toString(result.getRow());
    HBaseQuestionComponentMapperFactory mapperFactory
        = new HBaseQuestionComponentMapperFactory();
    componentRepository.setMapperFactory(mapperFactory);

    List<Nested<Component>> components
        = componentRepository.findAllBy(id);
    List<Component> organizedComponents
        = ComponentUtils.organize(id, components);

    HBaseQuestionResourceMapper resourceMapper
        = new HBaseQuestionResourceMapper(organizedComponents);
    return resourceMapper.from(result);
  }

  @Override
  public Optional<Auditable<QuestionResource>> findBy(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    get.setMaxVersions(MAX_VERSIONS);

    Result result = hbaseClient.get(get, QUESTION_TABLE);

    if (result.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(process(result));
  }

  @Override
  public void add(Auditable<QuestionResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<QuestionResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<QuestionResource> entity)
      throws IOException {

  }
}