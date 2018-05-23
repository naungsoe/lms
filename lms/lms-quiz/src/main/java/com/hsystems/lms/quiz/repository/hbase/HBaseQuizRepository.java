package com.hsystems.lms.quiz.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.quiz.repository.QuizComponentUtils;
import com.hsystems.lms.quiz.repository.entity.QuizResource;

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
public final class HBaseQuizRepository
    implements Repository<Auditable<QuizResource>> {

  private static final String QUIZ_TABLE = "lms:quizzes";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseComponentRepository componentRepository;

  @Inject
  HBaseQuizRepository(
      HBaseClient hbaseClient,
      HBaseComponentRepository componentRepository) {

    this.hbaseClient = hbaseClient;
    this.componentRepository = componentRepository;
  }

  public List<Auditable<QuizResource>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, QUIZ_TABLE);
    List<Auditable<QuizResource>> resources = new ArrayList<>();

    for (Result result : results) {
      resources.add(process(result));
    }

    return resources;
  }

  private Auditable<QuizResource> process(Result result)
      throws IOException {

    String id = Bytes.toString(result.getRow());
    HBaseQuizComponentMapperFactory mapperFactory
        = new HBaseQuizComponentMapperFactory();
    componentRepository.setMapperFactory(mapperFactory);

    List<Nested<Component>> components
        = componentRepository.findAllBy(id);
    List<Component> organizedComponents
        = QuizComponentUtils.organize(id, components);

    HBaseQuizResourceMapper resourceMapper
        = new HBaseQuizResourceMapper(organizedComponents);
    return resourceMapper.from(result);
  }

  @Override
  public Optional<Auditable<QuizResource>> findBy(String id)
      throws IOException {

    return Optional.empty();
  }

  @Override
  public void add(Auditable<QuizResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<QuizResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<QuizResource> entity)
      throws IOException {

  }
}