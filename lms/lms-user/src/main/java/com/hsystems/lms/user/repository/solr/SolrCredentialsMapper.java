package com.hsystems.lms.user.repository.solr;

import com.hsystems.lms.common.mapper.Mapper;
import com.hsystems.lms.solr.SolrUtils;
import com.hsystems.lms.user.repository.entity.Credentials;

import org.apache.solr.common.SolrDocument;

/**
 * Created by naungsoe on 12/10/16.
 */
public final class SolrCredentialsMapper
    implements Mapper<SolrDocument, Credentials> {

  private static final String ACCOUNT_FIELD = "account";
  private static final String PASSWORD_FIELD = "password";
  private static final String SALT_FIELD = "salt";

  public SolrCredentialsMapper() {

  }

  @Override
  public Credentials from(SolrDocument source) {
    String account = SolrUtils.getString(source, ACCOUNT_FIELD);
    String password = SolrUtils.getString(source, PASSWORD_FIELD);
    String salt = SolrUtils.getString(source, SALT_FIELD);
    return new Credentials(account, password, salt);
  }
}