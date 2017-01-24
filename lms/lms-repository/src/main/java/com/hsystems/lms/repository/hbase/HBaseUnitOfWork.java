package com.hsystems.lms.repository.hbase;

import com.google.inject.Inject;

import com.hsystems.lms.common.TransactionStatus;
import com.hsystems.lms.common.util.DateTimeUtils;
import com.hsystems.lms.repository.Constants;
import com.hsystems.lms.repository.RepositoryRegistry;
import com.hsystems.lms.repository.UnitOfWork;
import com.hsystems.lms.repository.entity.Entity;
import com.hsystems.lms.repository.entity.EntityType;
import com.hsystems.lms.repository.hbase.provider.HBaseClient;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by naungsoe on 6/1/17.
 */
public class HBaseUnitOfWork implements UnitOfWork {

  private final HBaseClient client;

  private final RepositoryRegistry repositoryRegistry;

  private List<Entity> newEntities;

  private List<Entity> dirtyEntities;

  private List<Entity> deletedEntities;

  private Transaction transaction;

  @Inject
  HBaseUnitOfWork(
      HBaseClient client,
      RepositoryRegistry repositoryRegistry) {

    this.client = client;
    this.repositoryRegistry = repositoryRegistry;
    this.newEntities = new ArrayList<>();
    this.dirtyEntities = new ArrayList<>();
    this.deletedEntities = new ArrayList<>();
  }

  @Override
  public void registerNew(Entity entity) {
    newEntities.add(entity);
  }

  @Override
  public void registerDirty(Entity entity) {
    dirtyEntities.add(entity);
  }

  @Override
  public void registerDeleted(Entity entity) {
    deletedEntities.add(entity);
  }

  @Override
  public void commit()
      throws IOException {

    try {
      startTransaction();
      insertNewEntities();
      updateDirtyEntities();
      deleteDeletedEntities();
      commitTransaction();
      updateIndexes();

    } catch (IOException e) {
      rollbackTransaction();
    }
  }

  @Override
  public void rollback()
      throws IOException {

    rollbackTransaction();
  }

  @Override
  public long getTimestamp(String id)
      throws IOException {

    Get get = new Get(Bytes.toBytes(id));
    Result result = client.get(get, Constants.TABLE_OPERATIONS);

    if (result.isEmpty()) {
      return Long.MIN_VALUE;
    }

    List<Cell> cellList = result.getColumnCells(
        Constants.FAMILY_DATA, Constants.IDENTIFIER_TIMESTAMP);
    Stack<Cell> cellStack = new Stack<>();
    cellList.stream().forEach(x -> cellStack.push(x));

    while (!cellStack.isEmpty()) {
      Cell cell = cellStack.pop();
      long timestamp = cell.getTimestamp();

      get = new Get(Bytes.toBytes(timestamp));
      result = client.get(get, Constants.TABLE_TRANSACTIONS);

      if (result.isEmpty()) {
        return Long.MIN_VALUE;
      }

      String value = Bytes.toString(result.getValue(
          Constants.FAMILY_DATA, Constants.IDENTIFIER_STATUS));
      TransactionStatus status = TransactionStatus.valueOf(value);

      if (TransactionStatus.COMMITTED.equals(status)) {
        return timestamp;
      }
    }

    return Long.MIN_VALUE;
  }

  private void startTransaction()
      throws IOException {

    transaction = new Transaction(
        DateTimeUtils.getCurrentMilliseconds(),
        TransactionStatus.STARTED,
        getAllOperations()
    );

    saveTransaction();
    saveChangeLogs();
  }

  private List<Operation> getAllOperations() {
    List<Operation> operations = new ArrayList<>();

    newEntities.stream().forEach(x -> {
      Operation operation = new Operation(
          x.getId(), getType(x), TransactionStatus.STARTED);
      operations.add(operation);
    });

    dirtyEntities.stream().forEach(x -> {
      Operation operation = new Operation(
          x.getId(), getType(x), TransactionStatus.STARTED);
      operations.add(operation);
    });

    deletedEntities.stream().forEach(x -> {
      Operation operation = new Operation(
          x.getId(), getType(x), TransactionStatus.STARTED);
      operations.add(operation);
    });

    return operations;
  }

  private EntityType getType(Entity entity) {
    String typeName = entity.getClass().getName().toUpperCase();
    return EntityType.valueOf(typeName);
  }

  private void insertNewEntities()
      throws IOException {

    for (Entity newEntity : newEntities) {
      Class<?> type = newEntity.getClass();
      repositoryRegistry.getRepository(type)
          .save(newEntity, transaction.getId());
    }
  }

  private void updateDirtyEntities()
      throws IOException {

    for (Entity dirtyEntity : dirtyEntities) {
      Class<?> type = dirtyEntity.getClass();
      repositoryRegistry.getRepository(type)
          .save(dirtyEntity, transaction.getId());
    }
  }

  private void deleteDeletedEntities()
      throws IOException {

    for (Entity deletedEntity : deletedEntities) {
      Class<?> type = deletedEntity.getClass();
      repositoryRegistry.getRepository(type)
          .delete(deletedEntity, transaction.getId());
    }
  }

  private void commitTransaction()
      throws IOException {

    transaction.getOperations().stream()
        .forEach(x -> x.commit());
    transaction.commit();
    saveChangeLogs();
    saveTransaction();
  }

  private void saveTransaction()
      throws IOException {

    Put put = new Put(Bytes.toBytes(transaction.getId()), transaction.getId());
    put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_ACTION,
        Bytes.toBytes(transaction.getStatus().toString()));

    transaction.getOperations().stream().forEach(x -> {
      put.addColumn(Constants.FAMILY_DATA, Bytes.toBytes(x.getId()),
          Bytes.toBytes(transaction.getStatus().toString()));
    });

    client.put(put, Constants.TABLE_TRANSACTIONS);
  }

  private void saveChangeLogs()
      throws IOException {

    List<Put> puts = new ArrayList<>();

    for (Operation operation : transaction.getOperations()) {
      Put put = new Put(Bytes.toBytes(operation.getId()), transaction.getId());
      put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_TYPE,
          Bytes.toBytes(operation.getType().toString()));
      put.addColumn(Constants.FAMILY_DATA, Constants.IDENTIFIER_ACTION,
          Bytes.toBytes(operation.getStatus().toString()));
      puts.add(put);
    }

    client.put(puts, Constants.TABLE_OPERATIONS);
  }

  private void updateIndexes()
      throws IOException {

    saveNewEntitiesIndex();
    saveDirtyEntitiesIndex();
    deleteDeletedEntitiesIndex();
  }

  private void saveNewEntitiesIndex()
      throws IOException {

    for (Entity newEntity : newEntities) {
      repositoryRegistry.getIndexRepository()
          .save(newEntity);
    }
  }

  private void saveDirtyEntitiesIndex()
      throws IOException {

    for (Entity dirtyEntity : dirtyEntities) {
      repositoryRegistry.getIndexRepository()
          .save(dirtyEntity);
    }
  }

  private void deleteDeletedEntitiesIndex()
      throws IOException {

    for (Entity dirtyEntity : dirtyEntities) {
      repositoryRegistry.getIndexRepository()
          .delete(dirtyEntity);
    }
  }

  private void rollbackTransaction()
      throws IOException {

    rollbackNewEntities();
    rollbackDirtyEntities();
    rollbackDeletedEntities();
    rollbackTransaction();

    transaction.getOperations().stream()
        .forEach(x -> x.rollback());
    transaction.rollback();
    saveChangeLogs();
    saveTransaction();
  }

  private void rollbackNewEntities()
      throws IOException {

    for (Entity newEntity : newEntities) {
      Class<?> type = newEntity.getClass();
      repositoryRegistry.getRepository(type)
          .delete(newEntity, transaction.getId());
    }
  }

  private void rollbackDirtyEntities()
      throws IOException {

    for (Entity dirtyEntity : dirtyEntities) {
      Class<?> type = dirtyEntity.getClass();
      repositoryRegistry.getRepository(type)
          .delete(dirtyEntity, transaction.getId());
    }
  }

  private void rollbackDeletedEntities()
      throws IOException {

    for (Entity deletedEntity : deletedEntities) {
      Class<?> type = deletedEntity.getClass();
      repositoryRegistry.getRepository(type)
          .delete(deletedEntity, transaction.getId());
    }
  }

  class Transaction {

    private long id;

    private TransactionStatus status;

    private List<Operation> operations;

    Transaction(
        long id,
        TransactionStatus status,
        List<Operation> operations) {

      this.id = id;
      this.status = status;
      this.operations = operations;
    }

    public long getId() {
      return id;
    }

    public TransactionStatus getStatus() {
      return status;
    }

    public List<Operation> getOperations() {
      return Collections.unmodifiableList(operations);
    }

    public void commit() {
      status = TransactionStatus.COMMITTED;
    }

    public void rollback() {
      status = TransactionStatus.ROLLBACKED;
    }
  }

  class Operation {

    private String id;

    private EntityType type;

    private TransactionStatus status;

    Operation(
        String id,
        EntityType type,
        TransactionStatus status) {

      this.id = id;
      this.type = type;
      this.status = status;
    }

    public String getId() {
      return id;
    }

    public EntityType getType() {
      return type;
    }

    public TransactionStatus getStatus() {
      return status;
    }

    public void commit() {
      status = TransactionStatus.COMMITTED;
    }

    public void rollback() {
      status = TransactionStatus.ROLLBACKED;
    }
  }
}
