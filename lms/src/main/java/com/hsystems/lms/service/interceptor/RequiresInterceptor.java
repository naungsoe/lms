package com.hsystems.lms.service.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by naungsoe on 17/9/16.
 */
public class RequiresInterceptor implements MethodInterceptor {

  public Object invoke(MethodInvocation invocation)
      throws Throwable {

    return invocation.proceed();
  }
}
