package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.Permission;
import com.vn.shopping.repository.PermissionRepository;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission permission) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(), permission.getApiPath(), permission.getMethod());
    }

    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission update(Permission permission) {
        Permission existing = permissionRepository.findById(permission.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền: " + permission.getId()));
        existing.setName(permission.getName());
        existing.setApiPath(permission.getApiPath());
        existing.setMethod(permission.getMethod());
        existing.setModule(permission.getModule());
        return permissionRepository.save(existing);
    }

    public void delete(long id) {
        // Xóa permission khỏi tất cả roles trước khi xóa
        Permission permission = permissionRepository.findById(id).orElse(null);
        if (permission != null) {
            permission.getRoles().forEach(role -> role.getPermissions().remove(permission));
            permissionRepository.delete(permission);
        }
    }

    public Permission findById(long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }
}
