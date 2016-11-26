package com.hsystems.lms.indexer.solr;

import com.google.inject.Inject;
import com.google.inject.Provider;

//import com.hsystems.lms.HBaseUtils;
import com.hsystems.lms.exception.IndexerException;
import com.hsystems.lms.indexer.UserIndexer;
//import com.hsystems.lms.io.FileReader;
import com.hsystems.lms.provider.solr.SolrClient;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by administrator on 3/10/16.
 */
public class SolrUserIndexer implements UserIndexer {

  private static final String COLLECTION = "users";

  private static final String COLUMN_FAMILY = "a";

  private Provider<Properties> propertiesProvider;

  private Provider<SolrClient> solrClientProvider;

  @Inject
  SolrUserIndexer(
      Provider<Properties> propertiesProvider,
      Provider<SolrClient> solrClientProvider) {

    this.propertiesProvider = propertiesProvider;
    this.solrClientProvider = solrClientProvider;
  }

  public void index()
      throws IndexerException {

    Properties properties = propertiesProvider.get();
    String usersFiles = properties.getProperty("app.indexer.users.files");
    List<String> usersFileUris = Arrays.asList(usersFiles.split("\\,"));

    List<Result> results = new ArrayList<>();

//    try {
//      results = FileReader.read(usersFileUris);
//    } catch (IOException e) {
//      throw new IndexerException("error reading files", e);
//    }

    index(results);
  }

  private void index(List<Result> result)
      throws IndexerException {

    try {
      List<SolrInputDocument> documents = new ArrayList<>();
//      result.stream().forEach(x -> documents.add(convert(x)));

      SolrClient solrClient = solrClientProvider.get();
      solrClient.index(COLLECTION, documents);
    } catch (SolrServerException | IOException e) {
      throw new IndexerException("error indexing result", e);
    }
  }

//  private SolrInputDocument convert(Result result) {
//    SolrInputDocument document = new SolrInputDocument();
//    document.addField("id", Bytes.toString(result.getRow()));
//    document.addField("firstName_s",
//        HBaseUtils.getString(result, COLUMN_FAMILY, "fname"));
//    document.addField("lastName_s",
//        HBaseUtils.getString(result, COLUMN_FAMILY, "lname"));
//    return document;
//  }
}
