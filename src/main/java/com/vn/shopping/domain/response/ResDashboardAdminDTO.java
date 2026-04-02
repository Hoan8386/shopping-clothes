package com.vn.shopping.domain.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class ResDashboardAdminDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStat {
        private String date;
        private Double revenue;
        private Long orderCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopProductStat {
        private Long productId;
        private String productName;
        private Long quantitySold;
        private Double totalRevenue;
        private Double profitMargin;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardSummary {
        private Double todayRevenue;
        private Double monthRevenue;
        private Double previousMonthRevenue;
        private Double revenueGrowthPercent;
        private Long monthlyOrders;
        private Long newCustomers;
        private Long productsSold;
        private Long totalCustomers;
        private List<DailyStat> revenueByDay;
        private List<DailyStat> ordersByDay;
        private List<TopProductStat> topProducts;
    }

    // Overview Dashboard
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardOverview {
        private Double totalRevenue;
        private Long totalOrders;
        private Long totalCustomers;
        private Long totalEmployees;
        private Long totalInventory;
        private Double averageOrderValue;
        private Double revenueGrowth; // % change from previous period
    }

    // Revenue Statistics
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueStat {
        private String period;
        private Double revenue;
        private Long orderCount;
        private Double averageOrderValue;
    }

    // Top Selling Products
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopSellingProduct {
        private Long productId;
        private String productName;
        private Long quantitySold;
        private Double totalRevenue;
        private Double profitMargin;
    }

    // Customer Statistics
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerStat {
        private Long totalCustomers;
        private Long newCustomersThisMonth;
        private Long activeCustomers;
        private Double averageShoppingValue;
        private Double customerRetentionRate;
    }

    // Employee Statistics
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeStat {
        private Long totalEmployees;
        private Long activeEmployees;
        private Long employeesOnLeave;
        private Double averageSalesPerEmployee;
        private Double topEmployeesSalesValue;
    }

    // Store Performance
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorePerformance {
        private Long storeId;
        private String storeName;
        private Double totalSales;
        private Long orderCount;
        private Long employeeCount;
    }

    // Return & Exchange Stats
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnExchangeStat {
        private Long totalReturns;
        private Long totalExchanges;
        private Double returnRate;
        private Double exchangeRate;
        private Double totalReturnAmount;
    }
}
