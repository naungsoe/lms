package com.hsystems.lms.common.security;

import com.google.inject.Provider;

import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.common.util.CommonUtils;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
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

    CommonUtils.checkNotNull(principalProvider.get(),
        "error retrieving PrincipalProvider");

    Method method = invocation.getMethod();
    Requires requires = method.getAnnotation(Requires.class);
    String[] permissions = requires.value();
    Principal principal = principalProvider.get();
    boolean hasPermissions = Arrays.asList(permissions).stream()
        .allMatch(permission -> principal.hasPermission(permission));
    CommonUtils.checkAccessControl(hasPermissions,
        String.format("access denied: %s", method.getName()));
    return invocation.proceed();
  }
}
