package com.hsystems.lms.entity.component;

/**
 * Created by naungsoe on 6/1/17.
 */
public interface GradableComponent<T extends GradingStrategy>
    extends Component {

  T getGradingStrategy();
}