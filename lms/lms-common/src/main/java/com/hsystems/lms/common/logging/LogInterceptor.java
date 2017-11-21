package com.hsystems.lms.common.logging;

import com.hsystems.lms.common.LoggerType;
import com.hsystems.lms.common.annotation.Log;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * Created by naungsoe on 14/9/16.
 */
public class LogInterceptor implements MethodInterceptor {

  private final static String MESSAGE_FORMAT
      = "Invocation of %s.%s with parameters %s";

  private final static Logger rootLogger = LogManager.getRootLogger();

  private final static Logger signInLogger = LogManager.getLogger("SignInLog");

  public Object invoke(MethodInvocation invocation)
      throws Throwable {

    try {
      Object result = invocation.proceed();
      logInfo(invocation);
      return result;

    } catch(Exception e) {
      logError(invocation, e);
      throw e;
    }
  }

  private void logInfo(MethodInvocation invocation) {
    Log annotation = invocation.getMethod()
        .getDeclaredAnnotation(Log.class);
    String message = getMessage(invocation);

    if (annotation.value() == LoggerType.SIGNIN) {
      signInLogger.info(message);
    }

    rootLogger.info(message);
  }

  private String getMessage(MethodInvocation invocation) {
    String type = invocation.getThis().getClass().getName();
    String method = invocation.getMethod().getName();
    String arguments = Arrays.toString(invocation.getArguments());
    return String.format(MESSAGE_FORMAT, type, method, arguments);
  }

  private void logError(MethodInvocation invocation, Exception e) {
    Log annotation = invocation.getMethod()
        .getDeclaredAnnotation(Log.class);
    String message = getMessage(invocation);

    if (annotation.value() == LoggerType.SIGNIN) {
      signInLogger.error(message, e);
    }

    rootLogger.error(message, e);
  }
}
