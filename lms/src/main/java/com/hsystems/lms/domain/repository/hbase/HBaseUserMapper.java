package com.hsystems.lms.domain.repository.hbase;

import com.hsystems.lms.domain.model.NullUser;
import com.hsystems.lms.domain.model.User;
import com.hsystems.lms.domain.model.UserCredentials;
import com.hsystems.lms.domain.model.UserParticulars;
import com.hsystems.lms.domain.repository.mapping.ColumnMap;
import com.hsystems.lms.domain.repository.mapping.MappingUtils;
import com.hsystems.lms.exception.ApplicationException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by administrator on 13/8/16.
 */
public final class HBaseUserMapper extends HBaseMapper {

  public HBaseUserMapper() throws ApplicationException {
    super(User.class, "users");
    loadDataMap();
  }

  public void loadDataMap() throws ApplicationException {
    dataMap.addColumn("c", "id", "userCredentials.id");
    dataMap.addColumn("c", "password", "userCredentials.password");
    dataMap.addColumn("p", "fname", "userParticulars.firstName");
    dataMap.addColumn("p", "lname", "userParticulars.lastName");
  }

  public User findBy(String key)
      throws IOException, InstantiationException,
      InvocationTargetException, IllegalAccessException,
      NoSuchFieldException, ApplicationException {

    Get get = new Get(Bytes.toBytes(key));
    Result result = getResult(get);

    if (result.isEmpty()) {
      return new NullUser();
    }
    return loadUser(key, result);
  }

  public User loadUser(String key, Result result)
      throws InstantiationException, InvocationTargetException,
      IllegalAccessException, NoSuchFieldException,
      ApplicationException {

    User user = (User) MappingUtils.getInstance(dataMap.getDomainClass());
    MappingUtils.setField(user, "identity", key);
    loadFields(user, result);
    return user;
  }
}
