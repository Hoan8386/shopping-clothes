package com.vn.shopping.controller;

import com.vn.shopping.domain.response.ResDashboardAdminDTO;
import com.vn.shopping.service.DashboardAdminService;
import com.vn.shopping.util.anotation.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardAdminController {

    @Autowired
    private DashboardAdminService dashboardAdminService;

    @ApiMessage("Lấy tổng quan dashboard quản trị thành công")
    @GetMapping("/summary")
    public ResponseEntity<ResDashboardAdminDTO.DashboardSummary> getDashboardSummary() {
        return ResponseEntity.ok(dashboardAdminService.getDashboardSummary());
    }
}
