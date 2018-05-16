package com.hsystems.lms.web;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import com.hsystems.lms.web.module.ServiceModule;
import com.hsystems.lms.web.module.WebAPIModule;
import com.hsystems.lms.web.module.WebModule;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * Created by naungsoe on 9/8/16.
 */
public class AppContextListener extends GuiceServletContextListener {

  private static final int CORE_THREAD_POOL_SIZE = 100;
  private static final int MAX_THREAD_POOL_SIZE = 200;
  private static final long THREAD_KEEP_ALIVE_TIME = 50000L;
  private static final int MAX_QUEUE_SIZE = 100;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    super.contextInitialized(servletContextEvent);

    ServletContext servletContext = servletContextEvent.getServletContext();
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        CORE_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE, THREAD_KEEP_ALIVE_TIME,
        TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(MAX_QUEUE_SIZE));
    servletContext.setAttribute("executor", executor);

  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    super.contextDestroyed(servletContextEvent);

    ServletContext servletContext = servletContextEvent.getServletContext();
    Object executor = servletContext.getAttribute("executor");
    ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) executor;
    poolExecutor.shutdown();
  }

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(
        new ServiceModule(),
        new WebAPIModule(),
        new WebModule());
  }
}