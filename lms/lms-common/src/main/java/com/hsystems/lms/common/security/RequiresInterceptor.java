package com.hsystems.lms.common.security;

import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;

/**
 * Created by naungsoe on 17/9/16.
 */
public class RequiresInterceptor implements MethodInterceptor {

  private final Provider<Principal> principalProvider;

  public RequiresInterceptor(Provider<Principal> principalProvider) {
    this.principalProvider = principalProvider;
  }

  public Object invoke(MethodInvocation invocation)
      throws Throwable {

    if (principalProvider.get() != null) {
      Requires requires = invocation.getMethod().getAnnotation(Requires.class);
      String[] permissions = requires.value();
      Principal principal = principalProvider.get();
      boolean hasPermissions = Arrays.asList(permissions).stream()
          .allMatch(x -> principal.hasPermission(x));

      if (hasPermissions) {
        return invocation.proceed();
      }
    }

    throw new IllegalAccessException("access denied: "
        + invocation.getMethod().getName());
  }
}
