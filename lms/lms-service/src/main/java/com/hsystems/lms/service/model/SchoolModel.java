package com.hsystems.lms.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hsystems.lms.common.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by naungsoe on 7/10/16.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchoolModel extends AuditableModel
    implements Serializable {

  private static final long serialVersionUID = 1646439483406673648L;

  private String id;

  private String name;

  private String locale;

  private String dateFormat;

  private String dateTimeFormat;

  private List<String> permissions;

  public SchoolModel() {

  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  public String getDateTimeFormat() {
    return dateTimeFormat;
  }

  public void setDateTimeFormat(String dateTimeFormat) {
    this.dateTimeFormat = dateTimeFormat;
  }

  public List<String> getPermissions() {
    return CollectionUtils.isEmpty(permissions)
        ? Collections.emptyList() : permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = new ArrayList<>();
    this.permissions.addAll(permissions);
  }
}
