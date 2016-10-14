package com.hsystems.lms;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.hsystems.lms.exception.IndexerException;
import com.hsystems.lms.indexer.UserIndexer;
import com.hsystems.lms.module.AppModule;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * HBase Sequence File Indexer
 *
 */
public class Indexer {

  private final static Logger logger
      = Logger.getLogger(Indexer.class);

  public static void main(String[] args)
      throws IOException {

    Injector injector = Guice.createInjector(new AppModule());
    indexUsers(injector.getInstance(UserIndexer.class));
  }

  private static void indexUsers(UserIndexer userIndexer) {
    try {
      userIndexer.index();
    } catch (IndexerException e) {
      logger.error("error indexing users", e);
    }
  }
}
