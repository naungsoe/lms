package com.hsystems.lms.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by administrator on 17/9/16.
 */
public class RequiresInterceptor implements MethodInterceptor {

  public Object invoke(MethodInvocation invocation)
      throws Throwable {

    return invocation.proceed();
  }
}
