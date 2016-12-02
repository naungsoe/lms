package com.hsystems.lms.web.security;

import com.google.inject.Provider;

import com.hsystems.lms.service.model.UserModel;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by naungsoe on 17/9/16.
 */
public class RequiresInterceptor implements MethodInterceptor {

  private final Provider<UserModel> userModelProvider;

  public RequiresInterceptor(Provider<UserModel> userModelProvider) {
    this.userModelProvider = userModelProvider;
  }

  public Object invoke(MethodInvocation invocation)
      throws Throwable {
    
    return invocation.proceed();
  }
}
