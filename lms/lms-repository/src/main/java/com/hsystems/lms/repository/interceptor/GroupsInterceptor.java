package com.hsystems.lms.repository.interceptor;

import com.hsystems.lms.repository.GroupRepository;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by naungsoe on 14/9/16.
 */
public class GroupsInterceptor implements MethodInterceptor {

  private final GroupRepository groupRepository;

  public GroupsInterceptor(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  public Object invoke(MethodInvocation invocation)
      throws Throwable {


  }
}
