package com.hsystems.lms.common.security;

/**
 * Created by naungsoe on 30/12/16.
 */
public interface Principal {

  boolean hasPermission(String permission);
}