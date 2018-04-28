package com.hsystems.lms.web.webapi;

import com.google.inject.Inject;
import com.google.inject.Provider;

import com.hsystems.lms.common.security.Principal;
import com.hsystems.lms.service.AttemptService;

import javax.ws.rs.Path;

/**
 * Created by naungsoe on 8/2/18.
 */
@Path("/attempts")
public class AttemptController extends AbstractController {

  private final Provider<Principal> principalProvider;

  private final AttemptService attemptService;

  @Inject
  AttemptController(
      Provider<Principal> principalProvider,
      AttemptService attemptService) {

    this.principalProvider = principalProvider;
    this.attemptService = attemptService;
  }
}