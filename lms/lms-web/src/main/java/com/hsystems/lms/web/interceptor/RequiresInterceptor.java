package com.hsystems.lms.web.interceptor;

import com.google.inject.Provider;

import com.hsystems.lms.common.Permission;
import com.hsystems.lms.common.annotation.Requires;
import com.hsystems.lms.service.model.UserModel;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.servlet.ServletException;

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

    if (userModelProvider.get() != null) {
      Requires requires = invocation.getMethod().getAnnotation(Requires.class);
      Permission[] permissions = requires.value();
      UserModel model = userModelProvider.get();

      for (Permission permission : permissions) {
        if (model.getPermissions().contains(permission)) {
          return invocation.proceed();
        }
      }
    }

    throw new ServletException("access denied: "
        + invocation.getMethod().getName());
  }
}
