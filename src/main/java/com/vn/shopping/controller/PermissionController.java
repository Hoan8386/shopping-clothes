package com.vn.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.domain.Permission;
import com.vn.shopping.service.PermissionService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách quyền")
    public ResponseEntity<List<Permission>> getAll() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy quyền theo id")
    public ResponseEntity<Permission> getById(@PathVariable("id") long id) throws IdInvalidException {
        Permission permission = permissionService.findById(id);
        if (permission == null) {
            throw new IdInvalidException("Không tìm thấy quyền: " + id);
        }
        return ResponseEntity.ok(permission);
    }

    @PostMapping
    @ApiMessage("Tạo quyền")
    public ResponseEntity<Permission> create(@RequestBody Permission permission) throws IdInvalidException {
        if (permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Quyền với module/apiPath/method này đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.create(permission));
    }

    @PutMapping
    @ApiMessage("Cập nhật quyền")
    public ResponseEntity<Permission> update(@RequestBody Permission permission) throws IdInvalidException {
        if (permission.getId() == 0) {
            throw new IdInvalidException("Mã quyền không được để trống");
        }
        return ResponseEntity.ok(permissionService.update(permission));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa quyền")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (permissionService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy quyền: " + id);
        }
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
