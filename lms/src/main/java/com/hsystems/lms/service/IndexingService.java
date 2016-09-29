package com.hsystems.lms.service;

import com.hsystems.lms.exception.ServiceException;
import com.hsystems.lms.service.query.Query;

/**
 * Created by administrator on 10/8/16.
 */
public interface IndexingService {

  <T> void index(T entity)
      throws ServiceException;
}
