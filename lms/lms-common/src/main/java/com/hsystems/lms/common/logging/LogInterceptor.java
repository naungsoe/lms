package com.hsystems.lms.common.logging;

import com.hsystems.lms.common.logging.annotation.Log;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Created by naungsoe on 14/9/16.
 */
public final class LogInterceptor implements MethodInterceptor {

  private final static String MESSAGE_FORMAT
      = "%s invocation of %s.%s with parameters %s";

  private final static Logger appLogger = LogManager.getRootLogger();

  public Object invoke(MethodInvocation invocation)
      throws Throwable {

    String message = getMessage(invocation);

    try {
      Object result = invocation.proceed();
      appLogger.info(message);
      return result;

    } catch(Throwable e) {
      appLogger.error(message, e);
      throw e;
    }
  }

  private String getMessage(MethodInvocation invocation) {
    Log annotation = invocation.getMethod()
        .getDeclaredAnnotation(Log.class);
    String type = invocation.getThis().getClass().getName();
    String method = invocation.getMethod().getName();
    String arguments = Arrays.toString(invocation.getArguments());
    return String.format(MESSAGE_FORMAT,
        annotation.value(), type, method, arguments);
  }
}