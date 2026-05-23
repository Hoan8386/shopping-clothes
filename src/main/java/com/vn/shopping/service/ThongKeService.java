package com.vn.shopping.service;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.response.ResThongKeDTO;
import com.vn.shopping.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ThongKeService {

    private static final int TITLE_ROW = 3;
    private static final int PERIOD_ROW = 4;
    private static final int EXPORT_ROW = 5;
    private static final int TABLE_HEADER_ROW = 7;
    private static final int TABLE_DATA_START_ROW = 10;
    private static final int TITLE_START_COLUMN = 2;
    private static final int TITLE_END_COLUMN = 10;

    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final TraHangRepository traHangRepository;
    private final DoiHangRepository doiHangRepository;
    private final KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository;
    private final KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository;
    private final NhanVienRepository nhanVienRepository;
    private final CuaHangRepository cuaHangRepository;

    public ThongKeService(DonHangRepository donHangRepository,
            ChiTietDonHangRepository chiTietDonHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            PhieuNhapRepository phieuNhapRepository,
            ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
            TraHangRepository traHangRepository,
            DoiHangRepository doiHangRepository,
            KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository,
            KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository,
            NhanVienRepository nhanVienRepository,
            CuaHangRepository cuaHangRepository) {
        this.donHangRepository = donHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.phieuNhapRepository = phieuNhapRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.traHangRepository = traHangRepository;
        this.doiHangRepository = doiHangRepository;
        this.khuyenMaiTheoHoaDonRepository = khuyenMaiTheoHoaDonRepository;
        this.khuyenMaiTheoDiemRepository = khuyenMaiTheoDiemRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.cuaHangRepository = cuaHangRepository;
    }

    public ResThongKeDTO.RevenueReport getRevenueReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId) {
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);

        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);
        long totalOrders = orders.size();
        long completedOrders = orders.stream().filter(o -> Objects.equals(o.getTrangThai(), 5)).count();
        long cancelledOrders = orders.stream().filter(o -> Objects.equals(o.getTrangThai(), 4)).count();
        double totalRevenue = orders.stream().mapToDouble(o -> safeInt(o.getTongTienTra())).sum();
        double averageOrderValue = totalOrders == 0 ? 0 : totalRevenue / totalOrders;

        Map<LocalDate, List<DonHang>> byDay = orders.stream()
                .filter(o -> o.getNgayTao() != null)
                .collect(Collectors.groupingBy(o -> o.getNgayTao().toLocalDate(), TreeMap::new, Collectors.toList()));

        List<ResThongKeDTO.DailyRevenue> dailyRevenues = byDay.entrySet().stream()
                .map(e -> new ResThongKeDTO.DailyRevenue(
                        e.getKey().toString(),
                        e.getValue().stream().mapToDouble(o -> safeInt(o.getTongTienTra())).sum(),
                        (long) e.getValue().size()))
                .toList();

        return new ResThongKeDTO.RevenueReport(
                start.toString(),
                end.toString(),
                totalOrders,
                completedOrders,
                cancelledOrders,
                totalRevenue,
                averageOrderValue,
                dailyRevenues);
    }

    public ResThongKeDTO.OrderPerformanceReport getOrderPerformanceReport(LocalDate fromDate, LocalDate toDate,
            Long cuaHangId) {
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);

        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);
        long totalOrders = orders.size();
        long paidOrders = orders.stream().filter(o -> Objects.equals(o.getTrangThaiThanhToan(), 1)).count();
        long onlineOrders = orders.stream().filter(o -> Objects.equals(o.getHinhThucDonHang(), 1)).count();
        long storeOrders = orders.stream().filter(o -> Objects.equals(o.getHinhThucDonHang(), 0)).count();
        long successOrders = orders.stream().filter(o -> Objects.equals(o.getTrangThai(), 5)).count();
        long cancelOrders = orders.stream().filter(o -> Objects.equals(o.getTrangThai(), 4)).count();

        Map<Integer, Long> statusMap = orders.stream()
                .collect(
                        Collectors.groupingBy(o -> optionalInt(o.getTrangThai()), TreeMap::new, Collectors.counting()));

        List<ResThongKeDTO.StatusCount> statusCounts = statusMap.entrySet().stream()
                .map(e -> new ResThongKeDTO.StatusCount(e.getKey(), e.getValue()))
                .toList();

        double successRate = totalOrders == 0 ? 0 : (successOrders * 100.0 / totalOrders);
        double cancelRate = totalOrders == 0 ? 0 : (cancelOrders * 100.0 / totalOrders);

        return new ResThongKeDTO.OrderPerformanceReport(
                start.toString(),
                end.toString(),
                totalOrders,
                paidOrders,
                onlineOrders,
                storeOrders,
                successRate,
                cancelRate,
                statusCounts);
    }

    public ResThongKeDTO.InventoryAlertReport getInventoryAlertReport(int lowStockThreshold, Long cuaHangId) {
        int threshold = Math.max(0, lowStockThreshold);
        List<ChiTietSanPham> variants = chiTietSanPhamRepository.findAll().stream()
                .filter(v -> cuaHangId == null || Objects.equals(v.getMaCuaHang(), cuaHangId))
                .toList();

        long totalVariants = variants.size();
        long totalStock = variants.stream().mapToLong(v -> safeInt(v.getSoLuong())).sum();
        long outOfStockCount = variants.stream().filter(v -> safeInt(v.getSoLuong()) <= 0).count();
        long lowStockCount = variants.stream()
                .filter(v -> safeInt(v.getSoLuong()) > 0 && safeInt(v.getSoLuong()) <= threshold)
                .count();

        List<ResThongKeDTO.InventoryAlertItem> alerts = variants.stream()
                .filter(v -> safeInt(v.getSoLuong()) <= threshold)
                .sorted(Comparator.comparingInt(v -> safeInt(v.getSoLuong())))
                .map(v -> {
                    String mau = v.getMauSac() == null ? "" : v.getMauSac().getTenMauSac();
                    String size = v.getKichThuoc() == null ? "" : v.getKichThuoc().getTenKichThuoc();
                    String productName = v.getSanPham() == null ? "" : v.getSanPham().getTenSanPham();
                    int soLuong = safeInt(v.getSoLuong());
                    int warningStatus = soLuong <= 0 ? 2 : 1;
                    Long productId = v.getSanPham() == null ? null : v.getSanPham().getId();
                    return new ResThongKeDTO.InventoryAlertItem(
                            v.getId(),
                            productId,
                            productName,
                            v.getMaCuaHang(),
                            mau,
                            size,
                            soLuong,
                            warningStatus);
                })
                .limit(500)
                .toList();

        return new ResThongKeDTO.InventoryAlertReport(
                totalVariants,
                totalStock,
                outOfStockCount,
                lowStockCount,
                threshold,
                alerts);
    }

    public ResThongKeDTO.TopProductReport getTopProductReport(LocalDate fromDate, LocalDate toDate, int limit,
            Long cuaHangId) {
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);
        int topN = Math.max(1, Math.min(limit, 100));

        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);

        class ProductAccumulator {
            long qty;
            double revenue;
            double cost;
            String name;
        }

        Map<Long, ProductAccumulator> acc = new HashMap<>();

        for (DonHang order : orders) {
            List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
            for (ChiTietDonHang detail : details) {
                if (detail.getChiTietSanPham() == null || detail.getChiTietSanPham().getSanPham() == null) {
                    continue;
                }
                SanPham sp = detail.getChiTietSanPham().getSanPham();
                Long productId = sp.getId();
                ProductAccumulator a = acc.computeIfAbsent(productId, k -> new ProductAccumulator());
                int qty = safeInt(detail.getSoLuong());
                a.qty += qty;
                a.revenue += safeDouble(detail.getThanhTien());
                a.cost += qty * safeDouble(sp.getGiaVon());
                a.name = sp.getTenSanPham();
            }
        }

        List<ResThongKeDTO.TopProductItem> items = acc.entrySet().stream()
                .map(e -> {
                    ProductAccumulator a = e.getValue();
                    double margin = a.revenue <= 0 ? 0 : ((a.revenue - a.cost) / a.revenue) * 100;
                    return new ResThongKeDTO.TopProductItem(
                            e.getKey(),
                            a.name,
                            a.qty,
                            a.revenue,
                            margin);
                })
                .sorted((a, b) -> Long.compare(b.getSoLuongBan(), a.getSoLuongBan()))
                .limit(topN)
                .toList();

        return new ResThongKeDTO.TopProductReport(start.toString(), end.toString(), topN, items);
    }

    public ResThongKeDTO.ImportSupplierReport getImportSupplierReport(LocalDate fromDate, LocalDate toDate,
            Long cuaHangId) {
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);

        List<PhieuNhap> receipts = filterByStoreAndDate(phieuNhapRepository.findAll(), this::getReceiptDate, start, end,
                cuaHangId, PhieuNhap::getCuaHang);

        class SupplierAccumulator {
            String name;
            long receiptCount;
            long totalQuantity;
            long missingQuantity;
            double totalValue;
        }

        Map<Long, SupplierAccumulator> map = new HashMap<>();
        double totalImportValue = 0;

        for (PhieuNhap receipt : receipts) {
            Long supplierId = receipt.getNhaCungCap() == null ? -1L : receipt.getNhaCungCap().getId();
            SupplierAccumulator ac = map.computeIfAbsent(supplierId, k -> new SupplierAccumulator());
            ac.name = receipt.getNhaCungCap() == null ? "NCC không xác định"
                    : receipt.getNhaCungCap().getTenNhaCungCap();
            ac.receiptCount++;

            List<ChiTietPhieuNhap> details = chiTietPhieuNhapRepository.findByPhieuNhapId(receipt.getId());
            for (ChiTietPhieuNhap d : details) {
                int qty = safeInt(d.getSoLuong());
                int missing = safeInt(d.getSoLuongThieu());
                ac.totalQuantity += qty;
                ac.missingQuantity += missing;
                double giaVon = 0;
                if (d.getChiTietSanPham() != null && d.getChiTietSanPham().getSanPham() != null) {
                    giaVon = safeDouble(d.getChiTietSanPham().getSanPham().getGiaVon());
                }
                double value = qty * giaVon;
                ac.totalValue += value;
                totalImportValue += value;
            }
        }

        List<ResThongKeDTO.SupplierImportItem> suppliers = map.entrySet().stream()
                .map(e -> new ResThongKeDTO.SupplierImportItem(
                        e.getKey() < 0 ? null : e.getKey(),
                        e.getValue().name,
                        e.getValue().receiptCount,
                        e.getValue().totalQuantity,
                        e.getValue().missingQuantity,
                        e.getValue().totalValue))
                .sorted((a, b) -> Double.compare(safeDouble(b.getTongGiaTriNhap()), safeDouble(a.getTongGiaTriNhap())))
                .toList();

        return new ResThongKeDTO.ImportSupplierReport(
                start.toString(),
                end.toString(),
                (long) receipts.size(),
                totalImportValue,
                suppliers);
    }

    public ResThongKeDTO.ReturnExchangeReport getReturnExchangeReport(LocalDate fromDate, LocalDate toDate,
            Long cuaHangId) {
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);

        List<TraHang> returns = filterByStoreAndDate(traHangRepository.findAll(), TraHang::getNgayTao, start, end,
                cuaHangId, tr -> tr.getDonHang() == null ? null : tr.getDonHang().getCuaHang());
        List<DoiHang> exchanges = filterByStoreAndDate(doiHangRepository.findAll(), DoiHang::getNgayTao, start, end,
                cuaHangId, dh -> dh.getDonHang() == null ? null : dh.getDonHang().getCuaHang());
        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);

        long totalOrders = orders.size();
        long totalReturns = returns.size();
        long totalExchanges = exchanges.size();
        double totalReturnAmount = returns.stream().mapToDouble(r -> safeDouble(r.getTongTien())).sum();
        double totalExchangeAmount = exchanges.stream().mapToDouble(e -> safeDouble(e.getTongTien())).sum();

        double returnRate = totalOrders == 0 ? 0 : totalReturns * 100.0 / totalOrders;
        double exchangeRate = totalOrders == 0 ? 0 : totalExchanges * 100.0 / totalOrders;

        return new ResThongKeDTO.ReturnExchangeReport(
                start.toString(),
                end.toString(),
                totalReturns,
                totalExchanges,
                totalReturnAmount,
                totalExchangeAmount,
                returnRate,
                exchangeRate);
    }

    public ResThongKeDTO.PromotionReport getPromotionReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId) {
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);

        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);
        LocalDateTime now = LocalDateTime.now();

        long activeHoaDon = khuyenMaiTheoHoaDonRepository.findAll().stream()
                .filter(k -> Objects.equals(k.getTrangThai(), 1)
                        && k.getThoiGianBatDau() != null
                        && k.getThoiGianKetThuc() != null
                        && !k.getThoiGianBatDau().isAfter(now)
                        && !k.getThoiGianKetThuc().isBefore(now))
                .count();

        long activeDiem = khuyenMaiTheoDiemRepository.findAll().stream()
                .filter(k -> Objects.equals(k.getTrangThai(), 1)
                        && k.getThoiGianBatDau() != null
                        && k.getThoiGianKetThuc() != null
                        && !k.getThoiGianBatDau().isAfter(now)
                        && !k.getThoiGianKetThuc().isBefore(now))
                .count();

        long usedHoaDon = orders.stream().filter(o -> o.getMaKhuyenMaiHoaDon() != null).count();
        long usedDiem = orders.stream().filter(o -> o.getMaKhuyenMaiDiem() != null).count();
        double totalDiscountAmount = orders.stream().mapToDouble(o -> safeInt(o.getTienGiam())).sum();

        return new ResThongKeDTO.PromotionReport(
                start.toString(),
                end.toString(),
                activeHoaDon,
                activeDiem,
                usedHoaDon,
                usedDiem,
                totalDiscountAmount);
    }

    public ResThongKeDTO.StaffPerformanceReport getStaffPerformanceReport(LocalDate fromDate, LocalDate toDate,
            Long cuaHangId) {
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);

        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);
        List<NhanVien> staffs = nhanVienRepository.findAll().stream()
                .filter(staff -> cuaHangId == null || (staff.getCuaHang() != null
                        && Objects.equals(staff.getCuaHang().getId(), cuaHangId)))
                .toList();

        Map<Long, List<DonHang>> byStaff = orders.stream()
                .filter(o -> o.getNhanVien() != null && o.getNhanVien().getId() != null)
                .collect(Collectors.groupingBy(o -> o.getNhanVien().getId()));

        List<ResThongKeDTO.StaffPerformanceItem> items = new ArrayList<>();
        for (NhanVien staff : staffs) {
            List<DonHang> staffOrders = byStaff.getOrDefault(staff.getId(), Collections.emptyList());
            long orderCount = staffOrders.size();
            double revenue = staffOrders.stream().mapToDouble(o -> safeInt(o.getTongTienTra())).sum();
            long customerCount = staffOrders.stream()
                    .filter(o -> o.getKhachHang() != null)
                    .map(o -> o.getKhachHang().getId())
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();
            double avg = orderCount == 0 ? 0 : revenue / orderCount;

            items.add(new ResThongKeDTO.StaffPerformanceItem(
                    staff.getId(),
                    staff.getTenNhanVien(),
                    staff.getCuaHang() == null ? null : staff.getCuaHang().getId(),
                    orderCount,
                    revenue,
                    customerCount,
                    avg));
        }

        items.sort((a, b) -> Double.compare(safeDouble(b.getDoanhThu()), safeDouble(a.getDoanhThu())));

        return new ResThongKeDTO.StaffPerformanceReport(
                start.toString(),
                end.toString(),
                (long) staffs.size(),
                items);
    }

    public byte[] exportRevenueReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId) throws IOException {
        ResThongKeDTO.RevenueReport report = getRevenueReport(fromDate, toDate, cuaHangId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeKeyValueSheet(
                    workbook.createSheet("TổngQuan"),
                    "Thống kê doanh thu bán hàng",
                    report.getFromDate(),
                    report.getToDate(),
                    cuaHangId,
                    kv("Từ ngày", report.getFromDate()),
                    kv("Đến ngày", report.getToDate()),
                    kv("Tổng đơn", report.getTotalOrders()),
                    kv("Đơn hoàn thành", report.getCompletedOrders()),
                    kv("Đơn hủy", report.getCancelledOrders()),
                    kv("Tổng doanh thu", report.getTotalRevenue()),
                    kv("Giá trị đơn trung bình", report.getAverageOrderValue()));

            Sheet detail = workbook.createSheet("TheoNgày");
            writeTableTitle(detail, "Chi tiết doanh thu theo ngày", report.getFromDate(), report.getToDate(),
                    cuaHangId);
            writeHeader(detail, "Ngày", "Doanh thu", "Số đơn");
            int rowNum = TABLE_DATA_START_ROW;
            for (ResThongKeDTO.DailyRevenue item : report.getDailyRevenues()) {
                Row row = detail.createRow(rowNum++);
                boolean altRow = rowNum % 2 == 0;

                Cell dateCell = row.createCell(0);
                dateCell.setCellValue(item.getDate());
                dateCell.setCellStyle(createDataCellStyle(workbook, altRow, true));

                Cell revenueCell = row.createCell(1);
                revenueCell.setCellValue(safeDouble(item.getRevenue()));
                revenueCell.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell countCell = row.createCell(2);
                countCell.setCellValue(item.getOrderCount() == null ? 0 : item.getOrderCount());
                countCell.setCellStyle(createDataCellStyle(workbook, altRow));
            }

            autosize(detail, 3);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportOrderPerformanceReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.OrderPerformanceReport report = getOrderPerformanceReport(fromDate, toDate, cuaHangId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeKeyValueSheet(
                    workbook.createSheet("TổngQuan"),
                    "Thống kê hiệu suất đơn hàng",
                    report.getFromDate(),
                    report.getToDate(),
                    cuaHangId,
                    kv("Từ ngày", report.getFromDate()),
                    kv("Đến ngày", report.getToDate()),
                    kv("Tổng đơn", report.getTotalOrders()),
                    kv("Đơn đã thanh toán", report.getPaidOrders()),
                    kv("Đơn online", report.getOnlineOrders()),
                    kv("Đơn tại quầy", report.getStoreOrders()),
                    kv("Tỷ lệ thành công", report.getSuccessRate()),
                    kv("Tỷ lệ hủy", report.getCancelRate()));

            Sheet detail = workbook.createSheet("TrạngTháiĐơn");
            writeTableTitle(detail, "Chi tiết đơn hàng theo trạng thái", report.getFromDate(), report.getToDate(),
                    cuaHangId);
            writeHeader(detail, "Trạng thái", "Số lượng");
            int rowNum = TABLE_DATA_START_ROW;
            for (ResThongKeDTO.StatusCount item : report.getStatusCounts()) {
                Row row = detail.createRow(rowNum++);
                boolean altRow = rowNum % 2 == 0;

                Cell statusCell = row.createCell(0);
                statusCell.setCellValue(item.getStatus() == null ? -1 : item.getStatus());
                statusCell.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell countCell = row.createCell(1);
                countCell.setCellValue(item.getCount() == null ? 0 : item.getCount());
                countCell.setCellStyle(createDataCellStyle(workbook, altRow));
            }

            autosize(detail, 2);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportInventoryAlertReport(int lowStockThreshold, Long cuaHangId) throws IOException {
        ResThongKeDTO.InventoryAlertReport report = getInventoryAlertReport(lowStockThreshold, cuaHangId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeKeyValueSheet(
                    workbook.createSheet("TổngQuan"),
                    "Thống kê tồn kho và cảnh báo",
                    null,
                    null,
                    cuaHangId,
                    kv("Tổng biến thể", report.getTotalVariants()),
                    kv("Tổng tồn", report.getTotalStock()),
                    kv("Hết hàng", report.getOutOfStockCount()),
                    kv("Sắp hết", report.getLowStockCount()),
                    kv("Ngưỡng cảnh báo", report.getLowStockThreshold()));

            Sheet detail = workbook.createSheet("CảnhBáo");
            writeTableTitle(detail, "Danh sách cảnh báo tồn kho", null, null, cuaHangId);
            writeHeader(detail, "Chi tiết SP", "Sản phẩm", "Tên SP", "Cửa hàng", "Màu", "Size", "Số lượng", "Cảnh báo");
            int rowNum = TABLE_DATA_START_ROW;
            for (ResThongKeDTO.InventoryAlertItem item : report.getAlerts()) {
                Row row = detail.createRow(rowNum++);
                boolean altRow = rowNum % 2 == 0;

                Cell c0 = row.createCell(0);
                c0.setCellValue(item.getChiTietSanPhamId() == null ? 0 : item.getChiTietSanPhamId());
                c0.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c1 = row.createCell(1);
                c1.setCellValue(item.getSanPhamId() == null ? 0 : item.getSanPhamId());
                c1.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c2 = row.createCell(2);
                c2.setCellValue(Objects.toString(item.getTenSanPham(), ""));
                c2.setCellStyle(createDataCellStyle(workbook, altRow, true));

                Cell c3 = row.createCell(3);
                c3.setCellValue(item.getCuaHangId() == null ? 0 : item.getCuaHangId());
                c3.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c4 = row.createCell(4);
                c4.setCellValue(Objects.toString(item.getMauSac(), ""));
                c4.setCellStyle(createDataCellStyle(workbook, altRow, true));

                Cell c5 = row.createCell(5);
                c5.setCellValue(Objects.toString(item.getKichThuoc(), ""));
                c5.setCellStyle(createDataCellStyle(workbook, altRow, true));

                Cell c6 = row.createCell(6);
                c6.setCellValue(item.getSoLuong() == null ? 0 : item.getSoLuong());
                c6.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c7 = row.createCell(7);
                c7.setCellValue(item.getTrangThaiCanhBao() == null ? 0 : item.getTrangThaiCanhBao());
                c7.setCellStyle(createDataCellStyle(workbook, altRow));
            }

            autosize(detail, 8);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportTopProductReport(LocalDate fromDate, LocalDate toDate, int limit, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.TopProductReport report = getTopProductReport(fromDate, toDate, limit, cuaHangId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeKeyValueSheet(
                    workbook.createSheet("TổngQuan"),
                    "Thống kê top sản phẩm",
                    report.getFromDate(),
                    report.getToDate(),
                    cuaHangId,
                    kv("Từ ngày", report.getFromDate()),
                    kv("Đến ngày", report.getToDate()),
                    kv("Top", report.getLimit()));

            Sheet detail = workbook.createSheet("TopSảnPhẩm");
            writeTableTitle(detail, "Danh sách top sản phẩm", report.getFromDate(), report.getToDate(), cuaHangId);
            writeHeader(detail, "Mã SP", "Tên sản phẩm", "Số lượng bán", "Doanh thu", "Biên lợi nhuận");
            int rowNum = TABLE_DATA_START_ROW;
            for (ResThongKeDTO.TopProductItem item : report.getTopProducts()) {
                Row row = detail.createRow(rowNum++);
                boolean altRow = rowNum % 2 == 0;

                Cell c0 = row.createCell(0);
                c0.setCellValue(item.getSanPhamId() == null ? 0 : item.getSanPhamId());
                c0.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c1 = row.createCell(1);
                c1.setCellValue(Objects.toString(item.getTenSanPham(), ""));
                c1.setCellStyle(createDataCellStyle(workbook, altRow, true));

                Cell c2 = row.createCell(2);
                c2.setCellValue(item.getSoLuongBan() == null ? 0 : item.getSoLuongBan());
                c2.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c3 = row.createCell(3);
                c3.setCellValue(safeDouble(item.getDoanhThu()));
                c3.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c4 = row.createCell(4);
                c4.setCellValue(safeDouble(item.getBienLoiNhuan()));
                c4.setCellStyle(createDataCellStyle(workbook, altRow));
            }

            autosize(detail, 5);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportImportSupplierReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.ImportSupplierReport report = getImportSupplierReport(fromDate, toDate, cuaHangId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeKeyValueSheet(
                    workbook.createSheet("TổngQuan"),
                    "Thống kê nhập hàng và nhà cung cấp",
                    report.getFromDate(),
                    report.getToDate(),
                    cuaHangId,
                    kv("Từ ngày", report.getFromDate()),
                    kv("Đến ngày", report.getToDate()),
                    kv("Tổng phiếu nhập", report.getTotalReceipts()),
                    kv("Tổng giá trị nhập", report.getTotalImportValue()));

            Sheet detail = workbook.createSheet("NhàCungCấp");
            writeTableTitle(detail, "Chi tiết nhà cung cấp", report.getFromDate(), report.getToDate(), cuaHangId);
            writeHeader(detail, "Mã NCC", "Tên NCC", "Số phiếu", "Tổng SL", "Tổng SL thiếu", "Giá trị nhập");
            int rowNum = TABLE_DATA_START_ROW;
            for (ResThongKeDTO.SupplierImportItem item : report.getSuppliers()) {
                Row row = detail.createRow(rowNum++);
                boolean altRow = rowNum % 2 == 0;

                Cell c0 = row.createCell(0);
                c0.setCellValue(item.getNhaCungCapId() == null ? 0 : item.getNhaCungCapId());
                c0.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c1 = row.createCell(1);
                c1.setCellValue(Objects.toString(item.getTenNhaCungCap(), ""));
                c1.setCellStyle(createDataCellStyle(workbook, altRow, true));

                Cell c2 = row.createCell(2);
                c2.setCellValue(item.getSoPhieuNhap() == null ? 0 : item.getSoPhieuNhap());
                c2.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c3 = row.createCell(3);
                c3.setCellValue(item.getTongSoLuongNhap() == null ? 0 : item.getTongSoLuongNhap());
                c3.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c4 = row.createCell(4);
                c4.setCellValue(item.getTongSoLuongThieu() == null ? 0 : item.getTongSoLuongThieu());
                c4.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c5 = row.createCell(5);
                c5.setCellValue(safeDouble(item.getTongGiaTriNhap()));
                c5.setCellStyle(createDataCellStyle(workbook, altRow));
            }

            autosize(detail, 6);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportReturnExchangeReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.ReturnExchangeReport report = getReturnExchangeReport(fromDate, toDate, cuaHangId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeKeyValueSheet(
                    workbook.createSheet("TổngQuan"),
                    "Thống kê trả đổi",
                    report.getFromDate(),
                    report.getToDate(),
                    cuaHangId,
                    kv("Từ ngày", report.getFromDate()),
                    kv("Đến ngày", report.getToDate()),
                    kv("Số trả hàng", report.getTotalReturns()),
                    kv("Số đổi hàng", report.getTotalExchanges()),
                    kv("Tiền trả hàng", report.getTotalReturnAmount()),
                    kv("Tiền đổi hàng", report.getTotalExchangeAmount()),
                    kv("Tỷ lệ trả", report.getReturnRate()),
                    kv("Tỷ lệ đổi", report.getExchangeRate()));

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportPromotionReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId) throws IOException {
        ResThongKeDTO.PromotionReport report = getPromotionReport(fromDate, toDate, cuaHangId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeKeyValueSheet(
                    workbook.createSheet("TổngQuan"),
                    "Thống kê khuyến mãi",
                    report.getFromDate(),
                    report.getToDate(),
                    cuaHangId,
                    kv("Từ ngày", report.getFromDate()),
                    kv("Đến ngày", report.getToDate()),
                    kv("KM hóa đơn đang hoạt động", report.getActiveHoaDonPromotions()),
                    kv("KM điểm đang hoạt động", report.getActiveDiemPromotions()),
                    kv("Đơn dùng KM hóa đơn", report.getUsedHoaDonPromotions()),
                    kv("Đơn dùng KM điểm", report.getUsedDiemPromotions()),
                    kv("Tổng tiền giảm", report.getTotalDiscountAmount()));

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportStaffPerformanceReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.StaffPerformanceReport report = getStaffPerformanceReport(fromDate, toDate, cuaHangId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            writeKeyValueSheet(
                    workbook.createSheet("TổngQuan"),
                    "Thống kê năng suất nhân viên",
                    report.getFromDate(),
                    report.getToDate(),
                    cuaHangId,
                    kv("Từ ngày", report.getFromDate()),
                    kv("Đến ngày", report.getToDate()),
                    kv("Tổng nhân viên", report.getTotalStaff()));

            Sheet detail = workbook.createSheet("NhânViên");
            writeTableTitle(detail, "Chi tiết năng suất nhân viên", report.getFromDate(), report.getToDate(),
                    cuaHangId);
            writeHeader(detail, "Mã NV", "Tên nhân viên", "Mã cửa hàng", "Số đơn", "Doanh thu", "Số khách", "Đơn TB");
            int rowNum = TABLE_DATA_START_ROW;
            for (ResThongKeDTO.StaffPerformanceItem item : report.getStaffs()) {
                Row row = detail.createRow(rowNum++);
                boolean altRow = rowNum % 2 == 0;

                Cell c0 = row.createCell(0);
                c0.setCellValue(item.getNhanVienId() == null ? 0 : item.getNhanVienId());
                c0.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c1 = row.createCell(1);
                c1.setCellValue(Objects.toString(item.getTenNhanVien(), ""));
                c1.setCellStyle(createDataCellStyle(workbook, altRow, true));

                Cell c2 = row.createCell(2);
                c2.setCellValue(item.getCuaHangId() == null ? 0 : item.getCuaHangId());
                c2.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c3 = row.createCell(3);
                c3.setCellValue(item.getSoDonXuLy() == null ? 0 : item.getSoDonXuLy());
                c3.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c4 = row.createCell(4);
                c4.setCellValue(safeDouble(item.getDoanhThu()));
                c4.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c5 = row.createCell(5);
                c5.setCellValue(item.getSoKhachPhucVu() == null ? 0 : item.getSoKhachPhucVu());
                c5.setCellStyle(createDataCellStyle(workbook, altRow));

                Cell c6 = row.createCell(6);
                c6.setCellValue(safeDouble(item.getGiaTriDonTrungBinh()));
                c6.setCellStyle(createDataCellStyle(workbook, altRow));
            }

            autosize(detail, 7);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private LocalDateTime getReceiptDate(PhieuNhap phieuNhap) {
        if (phieuNhap.getNgayDatHang() != null) {
            return phieuNhap.getNgayDatHang();
        }
        return phieuNhap.getNgayTao();
    }

    private <T> List<T> filterByStoreAndDate(List<T> source,
            Function<T, LocalDateTime> dateExtractor,
            LocalDate from,
            LocalDate to,
            Long cuaHangId,
            Function<T, CuaHang> storeExtractor) {
        LocalDateTime fromAt = from.atStartOfDay();
        LocalDateTime toAt = to.atTime(23, 59, 59);
        return source.stream()
                .filter(item -> {
                    if (cuaHangId != null) {
                        CuaHang cuaHang = storeExtractor.apply(item);
                        if (cuaHang == null || cuaHang.getId() == null || !Objects.equals(cuaHang.getId(), cuaHangId)) {
                            return false;
                        }
                    }

                    LocalDateTime time = dateExtractor.apply(item);
                    if (time == null) {
                        return false;
                    }
                    return !time.isBefore(fromAt) && !time.isAfter(toAt);
                })
                .toList();
    }

    private LocalDate normalizeFromDate(LocalDate fromDate) {
        if (fromDate != null) {
            return fromDate;
        }
        return LocalDate.now().withDayOfMonth(1);
    }

    private LocalDate normalizeToDate(LocalDate toDate) {
        if (toDate != null) {
            return toDate;
        }
        return LocalDate.now();
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private int optionalInt(Integer value) {
        return value == null ? -1 : value;
    }

    private double safeDouble(Double value) {
        return value == null ? 0 : value;
    }

    private String[] kv(String key, Object value) {
        return new String[] { key, Objects.toString(value, "") };
    }

    private void writeKeyValueSheet(Sheet sheet, String reportTitle, String fromDate, String toDate,
            Long cuaHangId, String[]... kvPairs) {
        writeTableTitle(sheet, reportTitle, fromDate, toDate, cuaHangId);

        Workbook workbook = sheet.getWorkbook();
        CellStyle keyStyle = createCellStyle(workbook, false, false, true);
        XSSFFont keyFont = createVietnameseFont(workbook);
        keyFont.setBold(true);
        keyFont.setColor(IndexedColors.DARK_BLUE.getIndex());
        keyStyle.setFont(keyFont);
        CellStyle valueStyle = createCellStyle(workbook, false, false, false);

        int rowNum = TABLE_HEADER_ROW + 1;
        Row storeRow = sheet.createRow(rowNum++);
        storeRow.createCell(0);

        Cell storeKeyCell = storeRow.createCell(1);
        storeKeyCell.setCellValue("Tên cửa hàng");
        storeKeyCell.setCellStyle(keyStyle);

        Cell storeValueCell = storeRow.createCell(2);
        storeValueCell.setCellValue(resolveStoreName(cuaHangId));
        storeValueCell.setCellStyle(valueStyle);

        for (String[] kv : kvPairs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0);

            Cell keyCell = row.createCell(1);
            keyCell.setCellValue(kv[0]);
            keyCell.setCellStyle(keyStyle);

            Cell valueCell = row.createCell(2);
            valueCell.setCellValue(kv[1]);
            valueCell.setCellStyle(valueStyle);
        }

        sheet.setColumnWidth(0, 4 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 30 * 256);
    }

    private void writeTableTitle(Sheet sheet, String reportTitle, String fromDate, String toDate, Long cuaHangId) {
        Workbook workbook = sheet.getWorkbook();
        int mergeEndColumn = TITLE_END_COLUMN;

        // Title row
        Row titleRow = sheet.createRow(TITLE_ROW);
        Cell titleCell = titleRow.createCell(TITLE_START_COLUMN);
        titleCell.setCellValue(reportTitle);
        titleCell.setCellStyle(createTitleStyle(workbook));
        titleRow.setHeightInPoints(25);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(TITLE_ROW, TITLE_ROW, TITLE_START_COLUMN,
                mergeEndColumn));

        // Period row
        Row periodRow = sheet.createRow(PERIOD_ROW);
        Cell periodCell = periodRow.createCell(TITLE_START_COLUMN);
        periodCell.setCellValue(formatPeriod(fromDate, toDate));
        periodCell.setCellStyle(createSubtitleStyle(workbook));
        periodRow.setHeightInPoints(18);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(PERIOD_ROW, PERIOD_ROW, TITLE_START_COLUMN,
                mergeEndColumn));

        // Export timestamp row
        Row exportRow = sheet.createRow(EXPORT_ROW);
        Cell exportCell = exportRow.createCell(TITLE_START_COLUMN);
        exportCell.setCellValue("Ngày xuất: " + LocalDateTime.now());
        exportCell.setCellStyle(createSubtitleStyle(workbook));
        exportRow.setHeightInPoints(16);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(EXPORT_ROW, EXPORT_ROW, TITLE_START_COLUMN,
                mergeEndColumn));

        // Store row
        Row storeRow = sheet.createRow(EXPORT_ROW + 1);
        Cell storeCell = storeRow.createCell(TITLE_START_COLUMN);
        storeCell.setCellValue("Cửa hàng: " + resolveStoreName(cuaHangId));
        storeCell.setCellStyle(createSubtitleStyle(workbook));
        storeRow.setHeightInPoints(16);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(EXPORT_ROW + 1, EXPORT_ROW + 1,
                TITLE_START_COLUMN, mergeEndColumn));
    }

    private void writeHeader(Sheet sheet, String... headers) {
        Row headerRow = sheet.createRow(TABLE_HEADER_ROW);
        headerRow.setHeightInPoints(20);

        Workbook workbook = sheet.getWorkbook();
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void autosize(Sheet sheet, int colCount) {
        for (int i = 0; i < colCount; i++) {
            sheet.autoSizeColumn(i);
        }
        // Freeze the title block and header so scrolling keeps the context visible.
        sheet.createFreezePane(0, TABLE_DATA_START_ROW);
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = createVietnameseFont(workbook);
        font.setBold(true);
        font.setFontHeight(19);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        XSSFColor bgColor = new XSSFColor(new byte[] { (byte) 0x15, (byte) 0x3E, (byte) 0x75 }, null);
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        BorderStyle border = BorderStyle.THIN;
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);

        return style;
    }

    private CellStyle createSubtitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = createVietnameseFont(workbook);
        font.setFontHeight(11);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);

        XSSFColor bgColor = new XSSFColor(new byte[] { (byte) 0xF4, (byte) 0xF7, (byte) 0xFB }, null);
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = createVietnameseFont(workbook);
        font.setBold(true);
        font.setFontHeight(12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        XSSFColor bgColor = new XSSFColor(new byte[] { (byte) 0x1D, (byte) 0x4E, (byte) 0x89 }, null);
        style.setFillForegroundColor(bgColor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);

        BorderStyle border = BorderStyle.THIN;
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);

        return style;
    }

    private CellStyle createCellStyle(Workbook workbook, boolean bold, boolean wrap, boolean leftAlign) {
        CellStyle style = workbook.createCellStyle();

        if (bold) {
            XSSFFont font = createVietnameseFont(workbook);
            font.setBold(true);
            font.setFontHeight(11);
            style.setFont(font);
        } else {
            XSSFFont font = createVietnameseFont(workbook);
            font.setFontHeight(11);
            style.setFont(font);
        }

        style.setAlignment(leftAlign ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(wrap);

        BorderStyle border = BorderStyle.THIN;
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);

        return style;
    }

    private CellStyle createDataCellStyle(Workbook workbook, boolean alternateRow) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = createVietnameseFont(workbook);
        font.setFontHeight(11);
        style.setFont(font);

        if (alternateRow) {
            XSSFColor bgColor = new XSSFColor(new byte[] { (byte) 0xF2, (byte) 0xF2, (byte) 0xF2 }, null);
            style.setFillForegroundColor(bgColor);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        BorderStyle border = BorderStyle.THIN;
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);

        return style;
    }

    private CellStyle createDataCellStyle(Workbook workbook, boolean alternateRow, boolean isText) {
        CellStyle style = createDataCellStyle(workbook, alternateRow);
        if (isText) {
            style.setAlignment(HorizontalAlignment.LEFT);
        }
        return style;
    }

    private XSSFFont createVietnameseFont(Workbook workbook) {
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontName("Arial");
        font.setCharSet(1); // Charset 1 = default/Unicode
        return font;
    }

    private String resolveStoreName(Long cuaHangId) {
        if (cuaHangId == null) {
            return "Toàn bộ cửa hàng";
        }
        return cuaHangRepository.findById(cuaHangId)
                .map(CuaHang::getTenCuaHang)
                .filter(name -> name != null && !name.trim().isEmpty())
                .orElse("Toàn bộ cửa hàng");
    }

    private String formatPeriod(String fromDate, String toDate) {
        if (fromDate == null || toDate == null || "N/A".equalsIgnoreCase(fromDate) || "N/A".equalsIgnoreCase(toDate)) {
            return "Thời kỳ: Không áp dụng";
        }
        return "Kỳ báo cáo: " + fromDate + " -> " + toDate;
    }
}
