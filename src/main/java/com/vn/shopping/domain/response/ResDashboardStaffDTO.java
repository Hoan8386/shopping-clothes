package com.vn.shopping.domain.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ResDashboardStaffDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardSummary {
        private Long employeeId;
        private String employeeName;
        private String storeName;
        private Long ordersNeedProcess;
        private Long ordersPacking;
        private Long ordersShipping;
        private Long ordersReturnExchange;
    }

    // Staff Overview Dashboard
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffOverview {
        private Long employeeId;
        private String employeeName;
        private String storeName;
        private Long ordersHandledToday;
        private Long ordersHandledThisMonth;
        private Double salesValueToday;
        private Double salesValueThisMonth;
        private Double averageOrderValue;
        private Long customersServedToday;
    }

    // Staff Schedule Info
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleInfo {
        private Long scheduleId;
        private LocalDate workDate;
        private String shiftName;
        private String shiftTime;
        private Integer status; // 0: Scheduled, 1: Confirmed, 2: Completed, 3: Absent
        private String note;
    }

    // Staff Sales Performance
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesPerformance {
        private LocalDate date;
        private Long ordersCount;
        private Double salesRevenue;
        private Double commission;
        private Double averageOrderValue;
    }

    // Staff Salary Info
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalaryInfo {
        private Double baseSalary;
        private Double bonus;
        private Double commission;
        private Double totalSalary;
        private String month;
        private String status; // Pending, Approved, Paid
    }

    // Tasks/Orders for Staff
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderTask {
        private Long orderId;
        private String customerName;
        private String customerPhone;
        private Double orderTotal;
        private Integer orderStatus;
        private String createdTime;
    }

    // Weekly Performance Summary
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeekPerformance {
        private String weekRange;
        private Long totalOrders;
        private Double totalRevenue;
        private Double averageDailyRevenue;
        private Long customersServed;
        private Double averageOrderValue;
    }

    // Staff Availability
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpcomingSchedule {
        private LocalDate date;
        private String shiftName;
        private String shiftTime;
        private Integer status;
    }
}
