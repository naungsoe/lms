package com.hsystems.lms.domain.repository.hbase;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.MappingUtils;
import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.mapping.DataMap;
import com.hsystems.lms.provider.hbase.HBaseClient;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Created by administrator on 13/8/16.
 */
public final class HBaseUserMapper extends HBaseMapper {

  private final static String TABLE_NAME = "users";

  private Provider<HBaseClient> hBaseClientProvider;

  private DataMap dataMap;

  @Inject
  HBaseUserMapper(Provider<HBaseClient> hBaseClientProvider)
      throws NoSuchFieldException {

    this.hBaseClientProvider = hBaseClientProvider;
    loadDataMap();
  }

  private void loadDataMap()
      throws NoSuchFieldException {

    dataMap = new DataMap(User.class, TABLE_NAME);
    dataMap.addColumn("a", "password", "password");
    dataMap.addColumn("a", "salt", "salt");
    dataMap.addColumn("a", "fname", "firstName");
    dataMap.addColumn("a", "lname", "lastName");
    dataMap.addColumn("a", "birthday", "birthday");
    dataMap.addColumn("a", "gender", "gender");
    dataMap.addColumn("a", "mobile", "mobile");
    dataMap.addColumn("a", "email", "email");
    dataMap.addColumn("p", "*", "permissions");
  }

  public Optional<User> findBy(String key)
      throws IOException, InstantiationException,
      InvocationTargetException, IllegalAccessException,
      NoSuchFieldException {

    HBaseClient hBaseClient = hBaseClientProvider.get();
    Get get = new Get(Bytes.toBytes(key));
    Optional<Result> result = hBaseClient.getResult(get, TABLE_NAME);

    if (result.isPresent()) {
      User user = loadUser(result.get());
      return Optional.of(user);
    }
    return Optional.empty();
  }

  protected User loadUser(Result result)
      throws InstantiationException, InvocationTargetException,
      IllegalAccessException, NoSuchFieldException {

    User user = (User) MappingUtils.getInstance(dataMap.getDomainClass());
    MappingUtils.setField(user, "id", Bytes.toString(result.getRow()));
    loadFields(user, result, dataMap);
    return user;
  }

  public void save(User user) {
    //lockRow()
    //Put put = new Put
    //Objects.hashCode(user.getId());
  }
}
