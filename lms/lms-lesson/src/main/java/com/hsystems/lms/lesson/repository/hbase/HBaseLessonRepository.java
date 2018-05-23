package com.hsystems.lms.lesson.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.lesson.repository.LessonComponentUtils;
import com.hsystems.lms.lesson.repository.entity.LessonResource;

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
public final class HBaseLessonRepository
    implements Repository<Auditable<LessonResource>> {

  private static final String LESSON_TABLE = "lms:lessons";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseComponentRepository componentRepository;

  @Inject
  HBaseLessonRepository(
      HBaseClient hbaseClient,
      HBaseComponentRepository componentRepository) {

    this.hbaseClient = hbaseClient;
    this.componentRepository = componentRepository;
  }

  public List<Auditable<LessonResource>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, LESSON_TABLE);
    List<Auditable<LessonResource>> resources = new ArrayList<>();

    for (Result result : results) {
      resources.add(process(result));
    }

    return resources;
  }

  private Auditable<LessonResource> process(Result result)
      throws IOException {

    String id = Bytes.toString(result.getRow());
    HBaseLessonComponentMapperFactory mapperFactory
        = new HBaseLessonComponentMapperFactory();
    componentRepository.setMapperFactory(mapperFactory);

    List<Nested<Component>> components
        = componentRepository.findAllBy(id);
    List<Component> organizedComponents
        = LessonComponentUtils.organize(id, components);

    HBaseLessonResourceMapper resourceMapper
        = new HBaseLessonResourceMapper(organizedComponents);
    return resourceMapper.from(result);
  }

  @Override
  public Optional<Auditable<LessonResource>> findBy(String id)
      throws IOException {

    return Optional.empty();
  }

  @Override
  public void add(Auditable<LessonResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<LessonResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<LessonResource> entity)
      throws IOException {

  }
}