package com.hsystems.lms.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;

public final class AppAsyncListener implements AsyncListener {

  @Override
  public void onComplete(AsyncEvent asyncEvent)
      throws IOException {

    // Cleanup resource
  }

  @Override
  public void onError(AsyncEvent asyncEvent)
      throws IOException {

    // Return error response to client
  }

  @Override
  public void onStartAsync(AsyncEvent asyncEvent)
      throws IOException {

    // Log event
  }

  @Override
  public void onTimeout(AsyncEvent asyncEvent)
      throws IOException {

    AsyncContext asyncContext = asyncEvent.getAsyncContext();
    ServletResponse response = asyncContext.getResponse();
    PrintWriter printWriter = response.getWriter();
    printWriter.write("Timeout error in async processing");
  }
}