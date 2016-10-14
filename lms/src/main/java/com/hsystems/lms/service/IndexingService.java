package com.hsystems.lms.service;

import com.hsystems.lms.service.exception.ServiceException;

/**
 * Created by administrator on 10/8/16.
 */
public interface IndexingService {

  <T> void index(T model)
      throws ServiceException;
}
