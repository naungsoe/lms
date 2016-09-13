package com.hsystems.lms.domain.repository.hbase;

import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.repository.mapping.MappingUtils;
import com.hsystems.lms.exception.ApplicationException;

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

  public HBaseUserMapper() throws ApplicationException {
    super(User.class, "users");
    loadDataMap();
  }

  private void loadDataMap() throws ApplicationException {
    dataMap.addColumn("a", "password", "password");
    dataMap.addColumn("a", "fname", "firstName");
    dataMap.addColumn("a", "lname", "lastName");
    dataMap.addColumn("a", "birthday", "birthday");
    dataMap.addColumn("a", "gender", "gender");
    dataMap.addColumn("a", "mobile", "mobile");
    dataMap.addColumn("a", "email", "email");
  }

  public Optional<User> findBy(String key)
      throws IOException, InstantiationException,
      InvocationTargetException, IllegalAccessException,
      NoSuchFieldException, ApplicationException {

    Get get = new Get(Bytes.toBytes(key));
    Optional<Result> result = getResult(get);

    if (result.isPresent()) {
      User user = loadUser(result.get());
      return Optional.of(user);
    }
    return Optional.empty();
  }

  protected User loadUser(Result result)
      throws InstantiationException, InvocationTargetException,
      IllegalAccessException, NoSuchFieldException,
      ApplicationException {

    User user = (User) MappingUtils.getInstance(dataMap.getDomainClass());
    MappingUtils.setField(user, "id", Bytes.toString(result.getRow()));
    loadFields(user, result);
    return user;
  }

  public void save(User user) {
    //lockRow()
    //Put put = new Put
    //Objects.hashCode(user.getId());
  }
}
