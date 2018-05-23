package com.hsystems.lms.course.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentRepository;
import com.hsystems.lms.course.repository.entity.CourseResource;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.lesson.repository.LessonComponentUtils;

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
public final class HBaseCourseRepository
    implements Repository<Auditable<CourseResource>> {

  private static final String COURSE_TABLE = "lms:courses";

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseComponentRepository componentRepository;

  @Inject
  HBaseCourseRepository(
      HBaseClient hbaseClient,
      HBaseComponentRepository componentRepository) {

    this.hbaseClient = hbaseClient;
    this.componentRepository = componentRepository;
  }

  public List<Auditable<CourseResource>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, COURSE_TABLE);
    List<Auditable<CourseResource>> resources = new ArrayList<>();

    for (Result result : results) {
      resources.add(process(result));
    }

    return resources;
  }

  private Auditable<CourseResource> process(Result result)
      throws IOException {

    String id = Bytes.toString(result.getRow());
    HBaseCourseComponentMapperFactory mapperFactory
        = new HBaseCourseComponentMapperFactory();
    componentRepository.setMapperFactory(mapperFactory);

    List<Nested<Component>> components
        = componentRepository.findAllBy(id);
    List<Component> organizedComponents
        = LessonComponentUtils.organize(id, components);

    HBaseCourseResourceMapper resourceMapper
        = new HBaseCourseResourceMapper(organizedComponents);
    return resourceMapper.from(result);
  }

  @Override
  public Optional<Auditable<CourseResource>> findBy(String id)
      throws IOException {

    return Optional.empty();
  }

  @Override
  public void add(Auditable<CourseResource> entity)
      throws IOException {

  }

  @Override
  public void update(Auditable<CourseResource> entity)
      throws IOException {

  }

  @Override
  public void remove(Auditable<CourseResource> entity)
      throws IOException {

  }
}