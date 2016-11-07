package com.hsystems.lms.service;

import com.hsystems.lms.service.exception.ServiceException;
import com.hsystems.lms.service.query.Criterion;

import java.util.List;

/**
 * Created by naungsoe on 10/8/16.
 */
public interface SearchService {

  <T> List<T> query(List<Criterion> criteria, Class<T> type)
      throws ServiceException;
}
