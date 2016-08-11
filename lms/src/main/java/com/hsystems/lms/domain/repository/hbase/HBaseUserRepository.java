package com.hsystems.lms.domain.repository.hbase;

import com.hsystems.lms.domain.model.UserCredentials;
import com.hsystems.lms.domain.model.UserEmpty;
import com.hsystems.lms.domain.model.UserParticulars;
import com.hsystems.lms.domain.repository.UserRepository;
import com.hsystems.lms.domain.model.User;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by administrator on 8/8/16.
 */
public final class HBaseUserRepository 
    extends HBaseRepository implements UserRepository {

  public HBaseUserRepository() {
    super("users");
  }

  public User findBy(String key) throws IOException {
    Get get = new Get(Bytes.toBytes(key));
    Result result = getResult(get);
    if (result.isEmpty()) {
      return new UserEmpty();
    }

    UserCredentials credentials = getCredientialsFrom(result);
    UserParticulars particulars = getParticularsFrom(result);
    return new User(credentials, particulars);
  }

  protected UserCredentials getCredientialsFrom(Result result) {
    return new UserCredentials(
      HBaseUtils.getString(result, "c", "id"), 
      HBaseUtils.getString(result, "c", "password"));
  }

  protected UserParticulars getParticularsFrom(Result result) {
    return new UserParticulars(
      HBaseUtils.getString(result, "p", "fname"),
      HBaseUtils.getString(result, "p", "lname"));
  }
}
