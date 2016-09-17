package com.hsystems.lms.service;

import com.hsystems.lms.exception.ServiceException;

/**
 * Created by administrator on 10/9/16.
 */
public interface IndexingService {

  <T> void index(T entity)
      throws ServiceException;
}
