package com.hsystems.lms.repository.entity.beans;

import com.hsystems.lms.repository.entity.Component;

public interface ComponentBean extends Component {

  String getResourceId();

  String getParentId();
}
