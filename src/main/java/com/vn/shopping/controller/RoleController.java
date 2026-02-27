package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.Role;
import com.vn.shopping.service.RoleService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách vai trò")
    public ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy vai trò theo id")
    public ResponseEntity<Role> getById(@PathVariable("id") long id) throws IdInvalidException {
        Role role = roleService.findById(id);
        if (role == null) {
            throw new IdInvalidException("Không tìm thấy vai trò: " + id);
        }
        return ResponseEntity.ok(role);
    }

    @PostMapping
    @ApiMessage("Tạo vai trò")
    public ResponseEntity<Role> create(@RequestBody Role role) throws IdInvalidException {
        if (roleService.findByName(role.getName()) != null) {
            throw new IdInvalidException("Vai trò đã tồn tại: " + role.getName());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.create(role));
    }

    @PutMapping
    @ApiMessage("Cập nhật vai trò")
    public ResponseEntity<Role> update(@RequestBody Role role) throws IdInvalidException {
        if (role.getId() == null || role.getId() == 0) {
            throw new IdInvalidException("Mã vai trò không được để trống");
        }
        return ResponseEntity.ok(roleService.update(role));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa vai trò")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (roleService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy vai trò: " + id);
        }
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
