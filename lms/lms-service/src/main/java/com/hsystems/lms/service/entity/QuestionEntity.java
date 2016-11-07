package com.hsystems.lms.service.entity;

import com.hsystems.lms.model.QuestionOption;
import com.hsystems.lms.model.QuestionType;
import com.hsystems.lms.model.School;
import com.hsystems.lms.model.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by administrator on 3/11/16.
 */
public class QuestionEntity implements Serializable {

  private static final long serialVersionUID = 553257369714695546L;

  private String id;

  private String type;

  private String body;

  private List<QuestionOptionEntity> options;

  private String schoolId;

  private String schoolName;

  private String createdById;

  private String createdByName;

  private String createdDateTime;

  private String modifiedById;

  private String modifiedByName;

  private String modifiedDateTime;
}
