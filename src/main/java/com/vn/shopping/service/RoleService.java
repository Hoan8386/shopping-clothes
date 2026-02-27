package com.vn.shopping.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vn.shopping.domain.Permission;
import com.vn.shopping.domain.Role;
import com.vn.shopping.repository.PermissionRepository;
import com.vn.shopping.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role create(Role role) {
        // Lọc permissions hợp lệ
        if (role.getPermissions() != null) {
            List<Long> permIds = role.getPermissions().stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = permissionRepository.findAllById(permIds);
            role.setPermissions(dbPermissions);
        }
        return roleRepository.save(role);
    }

    public Role update(Role role) {
        Role existing = roleRepository.findById(role.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò: " + role.getId()));
        existing.setName(role.getName());
        existing.setDescription(role.getDescription());
        existing.setActive(role.isActive());
        // Cập nhật permissions
        if (role.getPermissions() != null) {
            List<Long> permIds = role.getPermissions().stream()
                    .map(Permission::getId)
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = permissionRepository.findAllById(permIds);
            existing.setPermissions(dbPermissions);
        }
        return roleRepository.save(existing);
    }

    public void delete(long id) {
        roleRepository.deleteById(id);
    }

    public Role findById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
