package com.hsystems.lms.component;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface ComponentAttempt<T extends Component> {

  T getComponent();
}