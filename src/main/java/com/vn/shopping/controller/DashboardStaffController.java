package com.vn.shopping.controller;

import com.vn.shopping.domain.response.ResDashboardStaffDTO;
import com.vn.shopping.service.DashboardStaffService;
import com.vn.shopping.util.anotation.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardStaffController {

    @Autowired
    private DashboardStaffService dashboardStaffService;

    @ApiMessage("Lấy tổng quan dashboard nhân viên thành công")
    @GetMapping("/summary")
    public ResponseEntity<ResDashboardStaffDTO.DashboardSummary> getDashboardSummary(
            @RequestParam(value = "employeeId") Long employeeId) {
        ResDashboardStaffDTO.DashboardSummary summary = dashboardStaffService.getDashboardSummary(employeeId);
        if (summary == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(summary);
    }
}
