package com.hsystems.lms.repository.entity;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface GradableComponent<T extends GradingStrategy>
    extends Component {

  T getGradingStrategy();
}