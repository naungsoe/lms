package com.hsystems.lms.question.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.component.Component;
import com.hsystems.lms.component.Nested;
import com.hsystems.lms.component.repository.hbase.HBaseComponentRepository;
import com.hsystems.lms.entity.Auditable;
import com.hsystems.lms.entity.Repository;
import com.hsystems.lms.hbase.HBaseClient;
import com.hsystems.lms.hbase.HBaseScanFactory;
import com.hsystems.lms.hbase.HBaseUtils;
import com.hsystems.lms.question.repository.entity.CompositeQuestion;
import com.hsystems.lms.question.repository.entity.Question;
import com.hsystems.lms.question.repository.entity.QuestionComponent;
import com.hsystems.lms.question.repository.entity.QuestionResource;

import org.apache.hadoop.hbase.TableName;
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

  private static final TableName QUESTION_TABLE
      = TableName.valueOf("lms:questions");

  private static final int MAX_VERSIONS = 1;

  private final HBaseClient hbaseClient;

  private final HBaseQuestionResponseMapper questionMapper;

  private final HBaseComponentRepository componentRepository;

  @Inject
  HBaseQuestionRepository(
      HBaseClient hbaseClient,
      HBaseComponentRepository componentRepository) {

    this.hbaseClient = hbaseClient;
    this.componentRepository = componentRepository;
    this.questionMapper = new HBaseQuestionResponseMapper();
  }

  public List<Auditable<QuestionResource>> findAllBy(String lastId, int limit)
      throws IOException {

    Scan scan = HBaseScanFactory.createExclStartRowKeyScan(lastId);
    scan.setMaxVersions(MAX_VERSIONS);
    scan.setCaching(limit);

    List<Result> results = hbaseClient.scan(scan, QUESTION_TABLE);
    List<Auditable<QuestionResource>> resources = new ArrayList<>();
    HBaseUtils.forEachRowSetResults(results, rowSetResults -> {
      Auditable<QuestionResource> resource
          = questionMapper.from(rowSetResults);
      populateComponents(resource);
      resources.add(resource);
    });

    return resources;
  }

  private void populateComponents(Auditable<QuestionResource> resource) {
    Question question = resource.getEntity().getQuestion();

    if (question instanceof CompositeQuestion) {
      CompositeQuestion compositeQuestion = (CompositeQuestion) question;
      String resourceId = resource.getId();
      HBaseQuestionComponentMapper componentMapper
          = new HBaseQuestionComponentMapper(resourceId);
      componentRepository.setComponentMapper(componentMapper);

      try {
        List<Nested<Component>> components
            = componentRepository.findAllBy(resourceId);
        components.forEach(component -> {
          QuestionComponent questionComponent
              = (QuestionComponent) component.getComponent();
          compositeQuestion.addComponent(questionComponent);
        });
      } catch (IOException e) {
        throw new IllegalArgumentException(
            "error retrieving components", e);
      }
    }
  }

  @Override
  public Optional<Auditable<QuestionResource>> findBy(String id)
      throws IOException {

    Scan scan = HBaseScanFactory.createRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    List<Result> results = hbaseClient.scan(scan, QUESTION_TABLE);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    scan = HBaseScanFactory.createRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    scan.setMaxVersions(MAX_VERSIONS);

    Auditable<QuestionResource> resource = questionMapper.from(results);
    populateComponents(resource);
    return Optional.of(resource);
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