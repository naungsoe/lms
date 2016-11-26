package com.hsystems.lms.common.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Created by naungsoe on 14/9/16.
 */
public class LogInterceptor implements MethodInterceptor {

  private final static Logger logger
      = LogManager.getLogger(LogInterceptor.class);

  private final static String messageFormat
      = "invocation of %s.%s with parameters %s";

  public Object invoke(MethodInvocation invocation)
      throws Throwable {

    try {
      return invocation.proceed();
    } finally {
      String type = invocation.getThis().getClass().getName();
      String method = invocation.getMethod().getName();
      String arguments = Arrays.toString(invocation.getArguments());
      logger.info(String.format(messageFormat, type, method, arguments));
    }
  }
}
