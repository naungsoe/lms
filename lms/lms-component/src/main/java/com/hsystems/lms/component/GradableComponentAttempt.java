package com.hsystems.lms.component;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface GradableComponentAttempt<T extends Component>
    extends ComponentAttempt<T> {

  void gradeAttempt();
}