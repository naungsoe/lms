package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.util.CollectionUtils;
import com.hsystems.lms.repository.ShareRepository;
import com.hsystems.lms.repository.entity.Share;
import com.hsystems.lms.repository.hbase.mapper.HBaseShareMapper;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public class HBaseShareRepository
    extends HBaseRepository implements ShareRepository {

  private final HBaseClient client;

  private final HBaseShareMapper shareMapper;

  @Inject
  HBaseShareRepository(
      HBaseClient client,
      HBaseShareMapper shareMapper) {

    this.client = client;
    this.shareMapper = shareMapper;
  }

  @Override
  public Optional<Share> findBy(String id)
      throws IOException {

    Scan scan = getRowKeyFilterScan(id);
    scan.setStartRow(Bytes.toBytes(id));
    List<Result> results = client.scan(scan, Share.class);

    if (CollectionUtils.isEmpty(results)) {
      return Optional.empty();
    }

    Share share = shareMapper.getEntity(results);
    return Optional.of(share);
  }

  @Override
  public void save(Share entity)
      throws IOException {

  }

  @Override
  public void delete(Share entity)
      throws IOException {

  }
}
