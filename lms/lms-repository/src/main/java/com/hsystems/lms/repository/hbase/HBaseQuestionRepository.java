package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.QuestionRepository;
import com.hsystems.lms.repository.entity.Question;
import com.hsystems.lms.repository.hbase.mapper.HBaseQuestionMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseQuestionRepository
    extends HBaseRepository implements QuestionRepository {

  private final HBaseClient client;

  private final HBaseQuestionMapper mapper;

  @Inject
  HBaseQuestionRepository(
      HBaseClient client,
      HBaseQuestionMapper mapper) {

    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Optional<Question> findBy(String id, long timestamp)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setTimeStamp(timestamp);

    List<Result> results = client.scan(scan,
        Constants.TABLE_QUESTIONS);

    if (results.isEmpty()) {
      return Optional.empty();
    }

    Question question = mapper.getEntity(results);
    return Optional.of(question);
  }

  @Override
  public void save(Question question, long timestamp)
      throws IOException {

    List<Put> puts = mapper.getPuts(question, timestamp);
    client.put(puts, Constants.TABLE_QUESTIONS);
  }

  @Override
  public void delete(Question question, long timestamp)
      throws IOException {

    List<Delete> deletes = mapper.getDeletes(question, timestamp);
    client.delete(deletes, Constants.TABLE_QUESTIONS);
  }
}
