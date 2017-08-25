package com.hsystems.lms.repository.hbase;

import com.hsystems.lms.repository.ShareLogRepository;
import com.hsystems.lms.repository.entity.Resource;
import com.hsystems.lms.repository.entity.ResourcePermission;
import com.hsystems.lms.repository.entity.ShareLog;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * Created by naungsoe on 14/10/16.
 */
public abstract class HBaseResourceRepository extends HBaseRepository {

  protected ShareLogRepository shareLogRepository;


  protected <T extends Resource> void populatePermissions(List<T> resources)
      throws IOException {

    for (Resource resource : resources) {
      populatePermissions(resource);
    }
  }

  protected <T extends Resource> void populatePermissions(T resource)
      throws IOException {

    Optional<ShareLog> shareLogOptional
        = shareLogRepository.findBy(resource.getId());

    if (shareLogOptional.isPresent()) {
      ShareLog shareLog = shareLogOptional.get();
      populatePermissions(resource, shareLog);
    }
  }

  private void populatePermissions(Resource resource, ShareLog shareLog) {
    Enumeration<ResourcePermission> enumeration = shareLog.getPermissions();

    while (enumeration.hasMoreElements()) {
      ResourcePermission permission = enumeration.nextElement();
      resource.addPermission(permission);
    }
  }
}