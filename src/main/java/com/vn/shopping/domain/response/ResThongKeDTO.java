package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResThongKeDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyRevenue {
        private String date;
        private Double revenue;
        private Long orderCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueReport {
        private String fromDate;
        private String toDate;
        private Long totalOrders;
        private Long completedOrders;
        private Long cancelledOrders;
        private Double totalRevenue;
        private Double averageOrderValue;
        private List<DailyRevenue> dailyRevenues;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderPerformanceReport {
        private String fromDate;
        private String toDate;
        private Long totalOrders;
        private Long paidOrders;
        private Long onlineOrders;
        private Long storeOrders;
        private Double successRate;
        private Double cancelRate;
        private List<StatusCount> statusCounts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusCount {
        private Integer status;
        private Long count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryAlertItem {
        private Long chiTietSanPhamId;
        private Long sanPhamId;
        private String tenSanPham;
        private Long cuaHangId;
        private String mauSac;
        private String kichThuoc;
        private Integer soLuong;
        private Integer trangThaiCanhBao;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryAlertReport {
        private Long totalVariants;
        private Long totalStock;
        private Long outOfStockCount;
        private Long lowStockCount;
        private Integer lowStockThreshold;
        private List<InventoryAlertItem> alerts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopProductItem {
        private Long sanPhamId;
        private String tenSanPham;
        private Long soLuongBan;
        private Double doanhThu;
        private Double bienLoiNhuan;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopProductReport {
        private String fromDate;
        private String toDate;
        private Integer limit;
        private List<TopProductItem> topProducts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SupplierImportItem {
        private Long nhaCungCapId;
        private String tenNhaCungCap;
        private Long soPhieuNhap;
        private Long tongSoLuongNhap;
        private Long tongSoLuongThieu;
        private Double tongGiaTriNhap;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImportSupplierReport {
        private String fromDate;
        private String toDate;
        private Long totalReceipts;
        private Double totalImportValue;
        private List<SupplierImportItem> suppliers;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReturnExchangeReport {
        private String fromDate;
        private String toDate;
        private Long totalReturns;
        private Long totalExchanges;
        private Double totalReturnAmount;
        private Double totalExchangeAmount;
        private Double returnRate;
        private Double exchangeRate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionReport {
        private String fromDate;
        private String toDate;
        private Long activeHoaDonPromotions;
        private Long activeDiemPromotions;
        private Long usedHoaDonPromotions;
        private Long usedDiemPromotions;
        private Double totalDiscountAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffPerformanceItem {
        private Long nhanVienId;
        private String tenNhanVien;
        private Long cuaHangId;
        private Long soDonXuLy;
        private Double doanhThu;
        private Long soKhachPhucVu;
        private Double giaTriDonTrungBinh;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StaffPerformanceReport {
        private String fromDate;
        private String toDate;
        private Long totalStaff;
        private List<StaffPerformanceItem> staffs;
    }
}
