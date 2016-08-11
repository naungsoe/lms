package com.hsystems.lms.domain.repository;

import com.hsystems.lms.domain.model.User;

import java.io.IOException;

/**
 * Created by administrator on 8/8/16.
 */
public interface UserRepository {

  User findBy(String key) throws IOException;
}
