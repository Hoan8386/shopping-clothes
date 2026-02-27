package com.vn.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.shopping.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByModuleAndApiPathAndMethod(String module, String apiPath, String method);
}
