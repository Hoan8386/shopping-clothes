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
import java.time.format.DateTimeFormatter;
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
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final TraHangRepository traHangRepository;
    private final ChiTietTraHangRepository chiTietTraHangRepository;
    private final DoiHangRepository doiHangRepository;
    private final ChiTietDoiHangRepository chiTietDoiHangRepository;
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
            ChiTietTraHangRepository chiTietTraHangRepository,
            DoiHangRepository doiHangRepository,
            ChiTietDoiHangRepository chiTietDoiHangRepository,
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
        this.chiTietTraHangRepository = chiTietTraHangRepository;
        this.doiHangRepository = doiHangRepository;
        this.chiTietDoiHangRepository = chiTietDoiHangRepository;
        this.khuyenMaiTheoHoaDonRepository = khuyenMaiTheoHoaDonRepository;
        this.khuyenMaiTheoDiemRepository = khuyenMaiTheoDiemRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.cuaHangRepository = cuaHangRepository;
    }

    private record OrderLineDetail(
            LocalDateTime ngayTao,
            Long donHangId,
            Long cuaHangId,
            String tenCuaHang,
            Long khachHangId,
            String tenKhachHang,
            String sdtKhachHang,
            Long nhanVienId,
            String tenNhanVien,
            Integer trangThai,
            Integer trangThaiThanhToan,
            Integer hinhThucDonHang,
            Integer phuongThucThanhToan,
            Long maKhuyenMaiHoaDon,
            Long maKhuyenMaiDiem,
            Integer tongTien,
            Integer tienGiam,
            Integer tongTienGiam,
            Integer tongTienTra,
            Long chiTietDonHangId,
            Long chiTietSanPhamId,
            Long sanPhamId,
            String tenSanPham,
            String thuongHieu,
            String kieuSanPham,
            String boSuuTap,
            String mauSac,
            String kichThuoc,
            String maVach,
            Integer soLuong,
            Double giaSanPham,
            Double giamGia,
            Double giaGiam,
            Double thanhTien,
            String diaChi,
            String tenNguoiMua) {
    }

    private record ReceiptLineDetail(
            LocalDateTime ngayTao,
            Long phieuNhapId,
            String tenPhieuNhap,
            Long cuaHangId,
            String tenCuaHang,
            Long nhaCungCapId,
            String tenNhaCungCap,
            Integer trangThaiPhieuNhap,
            LocalDateTime ngayDatHang,
            LocalDateTime ngayNhanHang,
            Long chiTietPhieuNhapId,
            Long chiTietSanPhamId,
            Long sanPhamId,
            String tenSanPham,
            String mauSac,
            String kichThuoc,
            String maVach,
            Integer soLuong,
            Integer soLuongThieu,
            Integer soLuongDaNhap,
            String ghiTru,
            String ghiTruKiemHang,
            Integer trangThaiChiTiet,
            Double giaVon,
            Double giaTriDong) {
    }

    private record ReturnLineDetail(
            LocalDateTime ngayTao,
            Long traHangId,
            Long donHangId,
            Long cuaHangId,
            String tenCuaHang,
            Long khachHangId,
            String tenKhachHang,
            Long nhanVienId,
            String tenNhanVien,
            Integer trangThaiTraHang,
            Double tongTienHoan,
            String lyDoTraHang,
            String paymentRef,
            Long chiTietTraHangId,
            Long chiTietDonHangId,
            Long chiTietSanPhamId,
            Long sanPhamId,
            String tenSanPham,
            String mauSac,
            String kichThuoc,
            String hinhAnhChinh,
            Double giaSanPham,
            Integer soLuongTra,
            String ghiTru,
            Integer trangThaiChiTiet,
            Double thanhTien) {
    }

    private record ExchangeLineDetail(
            LocalDateTime ngayTao,
            Long doiHangId,
            Long donHangId,
            Long cuaHangId,
            String tenCuaHang,
            Long khachHangId,
            String tenKhachHang,
            Long nhanVienId,
            String tenNhanVien,
            Integer trangThaiDoiHang,
            Double tongTien,
            String ghiTruDoi,
            Long chiTietDoiHangId,
            Long chiTietDonHangIdTra,
            Long chiTietSanPhamTraId,
            Long sanPhamTraId,
            String tenSanPhamTra,
            String mauSacTra,
            String kichThuocTra,
            Double giaSanPhamTra,
            Integer soLuongTra,
            Long chiTietSanPhamDoiId,
            Long sanPhamDoiId,
            String tenSanPhamDoi,
            String mauSacDoi,
            String kichThuocDoi,
            Double giaSanPhamDoi,
            String ghiTruChiTiet,
            Integer trangThaiChiTiet,
            Double chenhLechGia) {
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

    private List<OrderLineDetail> collectOrderLineDetails(List<DonHang> orders) {
        List<OrderLineDetail> details = new ArrayList<>();
        for (DonHang order : orders) {
            List<ChiTietDonHang> orderDetails = chiTietDonHangRepository.findByDonHang(order);
            for (ChiTietDonHang detail : orderDetails) {
                ChiTietSanPham variant = detail.getChiTietSanPham();
                SanPham product = variant == null ? null : variant.getSanPham();
                details.add(new OrderLineDetail(
                        order.getNgayTao(),
                        order.getId(),
                        order.getCuaHang() == null ? null : order.getCuaHang().getId(),
                        order.getCuaHang() == null ? "" : Objects.toString(order.getCuaHang().getTenCuaHang(), ""),
                        order.getKhachHang() == null ? null : order.getKhachHang().getId(),
                        order.getKhachHang() == null ? ""
                                : Objects.toString(order.getKhachHang().getTenKhachHang(), ""),
                        order.getKhachHang() == null ? "" : Objects.toString(order.getKhachHang().getSdt(), ""),
                        order.getNhanVien() == null ? null : order.getNhanVien().getId(),
                        order.getNhanVien() == null ? "" : Objects.toString(order.getNhanVien().getTenNhanVien(), ""),
                        order.getTrangThai(),
                        order.getTrangThaiThanhToan(),
                        order.getHinhThucDonHang(),
                        order.getPhuongThucThanhToan(),
                        order.getMaKhuyenMaiHoaDon(),
                        order.getMaKhuyenMaiDiem(),
                        order.getTongTien(),
                        order.getTienGiam(),
                        order.getTongTienGiam(),
                        order.getTongTienTra(),
                        detail.getId(),
                        variant == null ? null : variant.getId(),
                        product == null ? null : product.getId(),
                        product == null ? "" : Objects.toString(product.getTenSanPham(), ""),
                        product == null ? ""
                                : Objects.toString(product.getThuongHieu() == null ? null
                                        : product.getThuongHieu().getTenThuongHieu(), ""),
                        product == null ? ""
                                : Objects.toString(product.getKieuSanPham() == null ? null
                                        : product.getKieuSanPham().getTenKieuSanPham(), ""),
                        product == null ? ""
                                : Objects.toString(
                                        product.getBoSuuTap() == null ? null : product.getBoSuuTap().getTenSuuTap(),
                                        ""),
                        variant == null ? ""
                                : Objects.toString(
                                        variant.getMauSac() == null ? null : variant.getMauSac().getTenMauSac(), ""),
                        variant == null ? ""
                                : Objects.toString(variant.getKichThuoc() == null ? null
                                        : variant.getKichThuoc().getTenKichThuoc(), ""),
                        variant == null ? "" : Objects.toString(variant.getMaVach(), ""),
                        detail.getSoLuong(),
                        detail.getGiaSanPham(),
                        detail.getGiamGia(),
                        detail.getGiaGiam(),
                        detail.getThanhTien(),
                        order.getDiaChi(),
                        order.getTenNguoiMua()));
            }
        }
        return details;
    }

    private List<ReceiptLineDetail> collectReceiptLineDetails(List<PhieuNhap> receipts) {
        List<ReceiptLineDetail> details = new ArrayList<>();
        for (PhieuNhap receipt : receipts) {
            List<ChiTietPhieuNhap> receiptDetails = chiTietPhieuNhapRepository.findByPhieuNhapId(receipt.getId());
            for (ChiTietPhieuNhap detail : receiptDetails) {
                ChiTietSanPham variant = detail.getChiTietSanPham();
                SanPham product = variant == null ? null : variant.getSanPham();
                double giaVon = product == null ? 0 : safeDouble(product.getGiaVon());
                details.add(new ReceiptLineDetail(
                        receipt.getNgayTao(),
                        receipt.getId(),
                        receipt.getTenPhieuNhap(),
                        receipt.getCuaHang() == null ? null : receipt.getCuaHang().getId(),
                        receipt.getCuaHang() == null ? "" : Objects.toString(receipt.getCuaHang().getTenCuaHang(), ""),
                        receipt.getNhaCungCap() == null ? null : receipt.getNhaCungCap().getId(),
                        receipt.getNhaCungCap() == null ? ""
                                : Objects.toString(receipt.getNhaCungCap().getTenNhaCungCap(), ""),
                        receipt.getTrangThai(),
                        receipt.getNgayDatHang(),
                        receipt.getNgayNhanHang(),
                        detail.getId(),
                        variant == null ? null : variant.getId(),
                        product == null ? null : product.getId(),
                        product == null ? "" : Objects.toString(product.getTenSanPham(), ""),
                        variant == null ? ""
                                : Objects.toString(
                                        variant.getMauSac() == null ? null : variant.getMauSac().getTenMauSac(), ""),
                        variant == null ? ""
                                : Objects.toString(variant.getKichThuoc() == null ? null
                                        : variant.getKichThuoc().getTenKichThuoc(), ""),
                        variant == null ? "" : Objects.toString(variant.getMaVach(), ""),
                        detail.getSoLuong(),
                        detail.getSoLuongThieu(),
                        detail.getSoLuongDaNhap(),
                        detail.getGhiTru(),
                        detail.getGhiTruKiemHang(),
                        detail.getTrangThai(),
                        giaVon,
                        safeInt(detail.getSoLuong()) * giaVon));
            }
        }
        return details;
    }

    private List<ReturnLineDetail> collectReturnLineDetails(List<TraHang> returns) {
        List<ReturnLineDetail> details = new ArrayList<>();
        for (TraHang traHang : returns) {
            List<ChiTietTraHang> returnDetails = chiTietTraHangRepository.findByTraHangId(traHang.getId());
            for (ChiTietTraHang detail : returnDetails) {
                ChiTietDonHang returnOrderDetail = detail.getSanPhamTra();
                ChiTietSanPham variant = returnOrderDetail == null ? null : returnOrderDetail.getChiTietSanPham();
                SanPham product = variant == null ? null : variant.getSanPham();
                DonHang order = traHang.getDonHang();
                details.add(new ReturnLineDetail(
                        traHang.getNgayTao(),
                        traHang.getId(),
                        order == null ? null : order.getId(),
                        order == null || order.getCuaHang() == null ? null : order.getCuaHang().getId(),
                        order == null || order.getCuaHang() == null ? ""
                                : Objects.toString(order.getCuaHang().getTenCuaHang(), ""),
                        order == null || order.getKhachHang() == null ? null : order.getKhachHang().getId(),
                        order == null || order.getKhachHang() == null ? ""
                                : Objects.toString(order.getKhachHang().getTenKhachHang(), ""),
                        order == null || order.getNhanVien() == null ? null : order.getNhanVien().getId(),
                        order == null || order.getNhanVien() == null ? ""
                                : Objects.toString(order.getNhanVien().getTenNhanVien(), ""),
                        traHang.getTrangThai(),
                        traHang.getTongTien(),
                        traHang.getLyDoTraHang(),
                        traHang.getPaymentRef(),
                        detail.getId(),
                        returnOrderDetail == null ? null : returnOrderDetail.getId(),
                        variant == null ? null : variant.getId(),
                        product == null ? null : product.getId(),
                        product == null ? "" : Objects.toString(product.getTenSanPham(), ""),
                        variant == null ? ""
                                : Objects.toString(
                                        variant.getMauSac() == null ? null : variant.getMauSac().getTenMauSac(), ""),
                        variant == null ? ""
                                : Objects.toString(variant.getKichThuoc() == null ? null
                                        : variant.getKichThuoc().getTenKichThuoc(), ""),
                        product == null ? "" : Objects.toString(product.getHinhAnhChinh(), ""),
                        returnOrderDetail == null ? null : returnOrderDetail.getGiaSanPham(),
                        detail.getSoLuongTra(),
                        detail.getGhiTru(),
                        detail.getTrangThai(),
                        returnOrderDetail == null || returnOrderDetail.getThanhTien() == null ? 0
                                : returnOrderDetail.getThanhTien()));
            }
        }
        return details;
    }

    private List<ExchangeLineDetail> collectExchangeLineDetails(List<DoiHang> exchanges) {
        List<ExchangeLineDetail> details = new ArrayList<>();
        for (DoiHang doiHang : exchanges) {
            List<ChiTietDoiHang> exchangeDetails = chiTietDoiHangRepository.findByDoiHangId(doiHang.getId());
            for (ChiTietDoiHang detail : exchangeDetails) {
                ChiTietDonHang returnOrderDetail = detail.getSanPhamTra();
                ChiTietSanPham returnVariant = returnOrderDetail == null ? null : returnOrderDetail.getChiTietSanPham();
                SanPham returnProduct = returnVariant == null ? null : returnVariant.getSanPham();
                ChiTietSanPham exchangeVariant = detail.getSanPhamDoi();
                SanPham exchangeProduct = exchangeVariant == null ? null : exchangeVariant.getSanPham();
                DonHang order = doiHang.getDonHang();
                details.add(new ExchangeLineDetail(
                        doiHang.getNgayTao(),
                        doiHang.getId(),
                        order == null ? null : order.getId(),
                        order == null || order.getCuaHang() == null ? null : order.getCuaHang().getId(),
                        order == null || order.getCuaHang() == null ? ""
                                : Objects.toString(order.getCuaHang().getTenCuaHang(), ""),
                        order == null || order.getKhachHang() == null ? null : order.getKhachHang().getId(),
                        order == null || order.getKhachHang() == null ? ""
                                : Objects.toString(order.getKhachHang().getTenKhachHang(), ""),
                        order == null || order.getNhanVien() == null ? null : order.getNhanVien().getId(),
                        order == null || order.getNhanVien() == null ? ""
                                : Objects.toString(order.getNhanVien().getTenNhanVien(), ""),
                        doiHang.getTrangThai(),
                        doiHang.getTongTien(),
                        doiHang.getGhiTru(),
                        detail.getId(),
                        returnOrderDetail == null ? null : returnOrderDetail.getId(),
                        returnVariant == null ? null : returnVariant.getId(),
                        returnProduct == null ? null : returnProduct.getId(),
                        returnProduct == null ? "" : Objects.toString(returnProduct.getTenSanPham(), ""),
                        returnVariant == null ? ""
                                : Objects.toString(returnVariant.getMauSac() == null ? null
                                        : returnVariant.getMauSac().getTenMauSac(), ""),
                        returnVariant == null ? ""
                                : Objects.toString(returnVariant.getKichThuoc() == null ? null
                                        : returnVariant.getKichThuoc().getTenKichThuoc(), ""),
                        returnOrderDetail == null ? null : returnOrderDetail.getGiaSanPham(),
                        returnOrderDetail == null ? null : returnOrderDetail.getSoLuong(),
                        exchangeVariant == null ? null : exchangeVariant.getId(),
                        exchangeProduct == null ? null : exchangeProduct.getId(),
                        exchangeProduct == null ? "" : Objects.toString(exchangeProduct.getTenSanPham(), ""),
                        exchangeVariant == null ? ""
                                : Objects.toString(exchangeVariant.getMauSac() == null ? null
                                        : exchangeVariant.getMauSac().getTenMauSac(), ""),
                        exchangeVariant == null ? ""
                                : Objects.toString(exchangeVariant.getKichThuoc() == null ? null
                                        : exchangeVariant.getKichThuoc().getTenKichThuoc(), ""),
                        exchangeProduct == null ? null : safeDouble(exchangeProduct.getGiaBan()),
                        detail.getGhiTru(),
                        detail.getTrangThai(),
                        safeDouble(exchangeProduct == null ? null : exchangeProduct.getGiaBan())
                                - safeDouble(returnOrderDetail == null ? null : returnOrderDetail.getGiaSanPham())));
            }
        }
        return details;
    }

    private void writeValues(Row row, Workbook workbook, boolean alternateRow, Object... values) {
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            Cell cell = row.createCell(i);
            if (value instanceof Number number) {
                cell.setCellValue(number.doubleValue());
                cell.setCellStyle(createDataCellStyle(workbook, alternateRow));
            } else if (value instanceof LocalDateTime dateTime) {
                cell.setCellValue(formatDateTime(dateTime));
                cell.setCellStyle(createDataCellStyle(workbook, alternateRow, true));
            } else if (value instanceof LocalDate date) {
                cell.setCellValue(date.toString());
                cell.setCellStyle(createDataCellStyle(workbook, alternateRow, true));
            } else {
                cell.setCellValue(Objects.toString(value, ""));
                cell.setCellStyle(createDataCellStyle(workbook, alternateRow, true));
            }
        }
    }

    private void writeOrderDetailSheet(Sheet sheet, List<OrderLineDetail> lines, String reportTitle, String fromDate,
            String toDate, Long cuaHangId) {
        writeTableTitle(sheet, reportTitle, fromDate, toDate, cuaHangId);
        writeHeader(sheet, "Ngày tạo", "Mã đơn", "Cửa hàng", "Khách hàng", "SĐT", "Nhân viên", "Trạng thái",
                "Thanh toán", "Hình thức", "KM hóa đơn", "KM điểm", "Mã CTSP", "Sản phẩm", "Thương hiệu",
                "Kiểu SP", "Bộ sưu tập", "Màu", "Size", "Mã vạch", "SL", "Giá SP", "Giảm giá",
                "Giá sau giảm", "Thành tiền", "Tổng đơn", "Tiền giảm", "Tổng tiền trả", "PT thanh toán",
                "Địa chỉ", "Người mua");

        Workbook workbook = sheet.getWorkbook();
        int rowNum = TABLE_DATA_START_ROW;
        for (OrderLineDetail item : lines) {
            Row row = sheet.createRow(rowNum++);
            boolean altRow = rowNum % 2 == 0;
            writeValues(row, workbook, altRow,
                    item.ngayTao(),
                    item.donHangId(),
                    item.tenCuaHang(),
                    item.tenKhachHang(),
                    item.sdtKhachHang(),
                    item.tenNhanVien(),
                    Objects.toString(item.trangThai(), "") + " - " + orderStatusText(item.trangThai()),
                    Objects.toString(item.trangThaiThanhToan(), "") + " - "
                            + paymentStatusText(item.trangThaiThanhToan()),
                    Objects.toString(item.hinhThucDonHang(), "") + " - " + orderTypeText(item.hinhThucDonHang()),
                    item.maKhuyenMaiHoaDon(),
                    item.maKhuyenMaiDiem(),
                    item.chiTietSanPhamId(),
                    item.tenSanPham(),
                    item.thuongHieu(),
                    item.kieuSanPham(),
                    item.boSuuTap(),
                    item.mauSac(),
                    item.kichThuoc(),
                    item.maVach(),
                    item.soLuong(),
                    item.giaSanPham(),
                    item.giamGia(),
                    item.giaGiam(),
                    item.thanhTien(),
                    item.tongTien(),
                    item.tienGiam(),
                    item.tongTienTra(),
                    Objects.toString(item.phuongThucThanhToan(), "") + " - "
                            + paymentMethodText(item.phuongThucThanhToan()),
                    item.diaChi(),
                    item.tenNguoiMua());
        }

        autosize(sheet, 30);
    }

    private void writeReceiptDetailSheet(Sheet sheet, List<ReceiptLineDetail> lines, String reportTitle,
            String fromDate,
            String toDate, Long cuaHangId) {
        writeTableTitle(sheet, reportTitle, fromDate, toDate, cuaHangId);
        writeHeader(sheet, "Ngày tạo", "Mã phiếu", "Tên phiếu", "Cửa hàng", "Nhà cung cấp", "Trạng thái phiếu",
                "Ngày đặt", "Ngày nhận", "Mã CTPN", "Mã CTSP", "Sản phẩm", "Màu", "Size", "Mã vạch",
                "SL đặt", "SL thiếu", "SL đã nhập", "Ghi chú", "Ghi chú kiểm", "Trạng thái CT", "Giá vốn",
                "Giá trị dòng");

        Workbook workbook = sheet.getWorkbook();
        int rowNum = TABLE_DATA_START_ROW;
        for (ReceiptLineDetail item : lines) {
            Row row = sheet.createRow(rowNum++);
            boolean altRow = rowNum % 2 == 0;
            writeValues(row, workbook, altRow,
                    item.ngayTao(),
                    item.phieuNhapId(),
                    item.tenPhieuNhap(),
                    item.tenCuaHang(),
                    item.tenNhaCungCap(),
                    item.trangThaiPhieuNhap(),
                    item.ngayDatHang(),
                    item.ngayNhanHang(),
                    item.chiTietPhieuNhapId(),
                    item.chiTietSanPhamId(),
                    item.tenSanPham(),
                    item.mauSac(),
                    item.kichThuoc(),
                    item.maVach(),
                    item.soLuong(),
                    item.soLuongThieu(),
                    item.soLuongDaNhap(),
                    item.ghiTru(),
                    item.ghiTruKiemHang(),
                    item.trangThaiChiTiet(),
                    item.giaVon(),
                    item.giaTriDong());
        }

        autosize(sheet, 23);
    }

    private void writeReturnDetailSheet(Sheet sheet, List<ReturnLineDetail> lines, String reportTitle, String fromDate,
            String toDate, Long cuaHangId) {
        writeTableTitle(sheet, reportTitle, fromDate, toDate, cuaHangId);
        writeHeader(sheet, "Ngày tạo", "Mã trả", "Mã đơn", "Cửa hàng", "Khách hàng", "Nhân viên", "Trạng thái",
                "Tổng tiền hoàn", "Lý do", "Payment ref", "Mã CT trả", "Mã CTĐH", "Mã CTSP", "Sản phẩm",
                "Màu", "Size", "Ảnh", "Giá SP", "SL trả", "Ghi chú", "Trạng thái CT", "Thành tiền");

        Workbook workbook = sheet.getWorkbook();
        int rowNum = TABLE_DATA_START_ROW;
        for (ReturnLineDetail item : lines) {
            Row row = sheet.createRow(rowNum++);
            boolean altRow = rowNum % 2 == 0;
            writeValues(row, workbook, altRow,
                    item.ngayTao(),
                    item.traHangId(),
                    item.donHangId(),
                    item.tenCuaHang(),
                    item.tenKhachHang(),
                    item.tenNhanVien(),
                    Objects.toString(item.trangThaiTraHang(), "") + " - " + returnStatusText(item.trangThaiTraHang()),
                    item.tongTienHoan(),
                    item.lyDoTraHang(),
                    item.paymentRef(),
                    item.chiTietTraHangId(),
                    item.chiTietDonHangId(),
                    item.chiTietSanPhamId(),
                    item.tenSanPham(),
                    item.mauSac(),
                    item.kichThuoc(),
                    item.hinhAnhChinh(),
                    item.giaSanPham(),
                    item.soLuongTra(),
                    item.ghiTru(),
                    item.trangThaiChiTiet(),
                    item.thanhTien());
        }

        autosize(sheet, 23);
    }

    private void writeExchangeDetailSheet(Sheet sheet, List<ExchangeLineDetail> lines, String reportTitle,
            String fromDate, String toDate, Long cuaHangId) {
        writeTableTitle(sheet, reportTitle, fromDate, toDate, cuaHangId);
        writeHeader(sheet, "Ngày tạo", "Mã đổi", "Mã đơn", "Cửa hàng", "Khách hàng", "Nhân viên", "Trạng thái",
                "Tổng tiền", "Ghi chú", "Mã CT đổi", "Mã CT trả", "Mã CTSP trả", "SP trả", "Màu trả",
                "Size trả", "Giá trả", "SL trả", "Mã CTSP đổi", "SP đổi", "Màu đổi", "Size đổi",
                "Giá đổi", "Ghi chú CT", "Trạng thái CT", "Chênh lệch");

        Workbook workbook = sheet.getWorkbook();
        int rowNum = TABLE_DATA_START_ROW;
        for (ExchangeLineDetail item : lines) {
            Row row = sheet.createRow(rowNum++);
            boolean altRow = rowNum % 2 == 0;
            writeValues(row, workbook, altRow,
                    item.ngayTao(),
                    item.doiHangId(),
                    item.donHangId(),
                    item.tenCuaHang(),
                    item.tenKhachHang(),
                    item.tenNhanVien(),
                    Objects.toString(item.trangThaiDoiHang(), "") + " - " + exchangeStatusText(item.trangThaiDoiHang()),
                    item.tongTien(),
                    item.ghiTruDoi(),
                    item.chiTietDoiHangId(),
                    item.chiTietDonHangIdTra(),
                    item.chiTietSanPhamTraId(),
                    item.tenSanPhamTra(),
                    item.mauSacTra(),
                    item.kichThuocTra(),
                    item.giaSanPhamTra(),
                    item.soLuongTra(),
                    item.chiTietSanPhamDoiId(),
                    item.tenSanPhamDoi(),
                    item.mauSacDoi(),
                    item.kichThuocDoi(),
                    item.giaSanPhamDoi(),
                    item.ghiTruChiTiet(),
                    item.trangThaiChiTiet(),
                    item.chenhLechGia());
        }

        autosize(sheet, 26);
    }

    private String formatDateTime(LocalDateTime value) {
        return value == null ? "" : value.format(DATE_TIME_FORMATTER);
    }

    private String orderStatusText(Integer status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case 0 -> "Chờ xác nhận";
            case 1 -> "Đã xác nhận";
            case 2 -> "Đang xử lý";
            case 3 -> "Đang giao";
            case 4 -> "Hủy";
            case 5 -> "Hoàn thành";
            default -> "Trạng thái " + status;
        };
    }

    private String paymentStatusText(Integer status) {
        if (status == null) {
            return "";
        }
        return status == 1 ? "Đã thanh toán" : "Chưa thanh toán";
    }

    private String orderTypeText(Integer type) {
        if (type == null) {
            return "";
        }
        return type == 1 ? "Online" : "Tại quầy";
    }

    private String paymentMethodText(Integer method) {
        if (method == null) {
            return "";
        }
        return switch (method) {
            case 0 -> "Tiền mặt";
            case 1 -> "Chuyển khoản";
            case 2 -> "VNPay";
            default -> "Phương thức " + method;
        };
    }

    private String returnStatusText(Integer status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case 0 -> "Chờ xử lý";
            case 1 -> "Đã duyệt";
            case 2 -> "Đã từ chối";
            default -> "Trạng thái " + status;
        };
    }

    private String exchangeStatusText(Integer status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case 0 -> "Chờ xử lý";
            case 1 -> "Đã duyệt";
            case 2 -> "Đã hoàn tất";
            default -> "Trạng thái " + status;
        };
    }

    public byte[] exportRevenueReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId) throws IOException {
        ResThongKeDTO.RevenueReport report = getRevenueReport(fromDate, toDate, cuaHangId);
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);
        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);
        List<OrderLineDetail> orderLines = collectOrderLineDetails(orders);
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

            Sheet orderDetail = workbook.createSheet("ChiTietDonHang");
            writeOrderDetailSheet(orderDetail, orderLines, "Chi tiết đơn hàng và sản phẩm", report.getFromDate(),
                    report.getToDate(), cuaHangId);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportOrderPerformanceReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.OrderPerformanceReport report = getOrderPerformanceReport(fromDate, toDate, cuaHangId);
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);
        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);
        List<OrderLineDetail> orderLines = collectOrderLineDetails(orders);
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

            Sheet orderDetail = workbook.createSheet("ChiTietDonHang");
            writeOrderDetailSheet(orderDetail, orderLines, "Chi tiết đơn hàng theo hiệu suất", report.getFromDate(),
                    report.getToDate(), cuaHangId);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportInventoryAlertReport(int lowStockThreshold, Long cuaHangId) throws IOException {
        ResThongKeDTO.InventoryAlertReport report = getInventoryAlertReport(lowStockThreshold, cuaHangId);
        List<ChiTietSanPham> variants = chiTietSanPhamRepository.findAll().stream()
                .filter(v -> cuaHangId == null || Objects.equals(v.getMaCuaHang(), cuaHangId))
                .toList();
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
            writeHeader(detail, "Chi tiết SP", "Sản phẩm", "Tên SP", "Thương hiệu", "Kiểu SP", "Bộ sưu tập",
                    "Cửa hàng", "Mã vạch", "Màu", "Size", "Giá bán", "Giá vốn", "Tồn kho", "Trạng thái",
                    "Mô tả");
            int rowNum = TABLE_DATA_START_ROW;
            for (ChiTietSanPham variant : variants) {
                if (safeInt(variant.getSoLuong()) > Math.max(0, lowStockThreshold)) {
                    continue;
                }
                SanPham product = variant.getSanPham();
                Row row = detail.createRow(rowNum++);
                boolean altRow = rowNum % 2 == 0;

                writeValues(row, workbook, altRow,
                        variant.getId(),
                        product == null ? null : product.getId(),
                        product == null ? "" : product.getTenSanPham(),
                        product == null || product.getThuongHieu() == null ? ""
                                : product.getThuongHieu().getTenThuongHieu(),
                        product == null || product.getKieuSanPham() == null ? ""
                                : product.getKieuSanPham().getTenKieuSanPham(),
                        product == null || product.getBoSuuTap() == null ? "" : product.getBoSuuTap().getTenSuuTap(),
                        variant.getMaCuaHang(),
                        variant.getMaVach(),
                        variant.getMauSac() == null ? "" : variant.getMauSac().getTenMauSac(),
                        variant.getKichThuoc() == null ? "" : variant.getKichThuoc().getTenKichThuoc(),
                        product == null ? null : product.getGiaBan(),
                        product == null ? null : product.getGiaVon(),
                        variant.getSoLuong(),
                        variant.getTrangThai(),
                        product == null ? "" : product.getMoTa());
            }

            autosize(detail, 15);
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportTopProductReport(LocalDate fromDate, LocalDate toDate, int limit, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.TopProductReport report = getTopProductReport(fromDate, toDate, limit, cuaHangId);
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);
        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);
        Set<Long> topProductIds = report.getTopProducts().stream()
                .map(ResThongKeDTO.TopProductItem::getSanPhamId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<OrderLineDetail> orderLines = collectOrderLineDetails(orders).stream()
                .filter(line -> line.sanPhamId() != null && topProductIds.contains(line.sanPhamId()))
                .toList();
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

            Sheet orderDetail = workbook.createSheet("ChiTietBanHang");
            writeOrderDetailSheet(orderDetail, orderLines, "Chi tiết bán hàng của top sản phẩm", report.getFromDate(),
                    report.getToDate(), cuaHangId);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportImportSupplierReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.ImportSupplierReport report = getImportSupplierReport(fromDate, toDate, cuaHangId);
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);
        List<PhieuNhap> receipts = filterByStoreAndDate(phieuNhapRepository.findAll(), this::getReceiptDate, start, end,
                cuaHangId, PhieuNhap::getCuaHang);
        List<ReceiptLineDetail> receiptLines = collectReceiptLineDetails(receipts);
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

            Sheet receiptDetail = workbook.createSheet("ChiTietPhieuNhap");
            writeReceiptDetailSheet(receiptDetail, receiptLines, "Chi tiết phiếu nhập và sản phẩm",
                    report.getFromDate(),
                    report.getToDate(), cuaHangId);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportReturnExchangeReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.ReturnExchangeReport report = getReturnExchangeReport(fromDate, toDate, cuaHangId);
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);
        List<TraHang> returns = filterByStoreAndDate(traHangRepository.findAll(), TraHang::getNgayTao, start, end,
                cuaHangId, tr -> tr.getDonHang() == null ? null : tr.getDonHang().getCuaHang());
        List<DoiHang> exchanges = filterByStoreAndDate(doiHangRepository.findAll(), DoiHang::getNgayTao, start, end,
                cuaHangId, dh -> dh.getDonHang() == null ? null : dh.getDonHang().getCuaHang());
        List<ReturnLineDetail> returnLines = collectReturnLineDetails(returns);
        List<ExchangeLineDetail> exchangeLines = collectExchangeLineDetails(exchanges);
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

            Sheet returnDetail = workbook.createSheet("ChiTietTraHang");
            writeReturnDetailSheet(returnDetail, returnLines, "Chi tiết trả hàng", report.getFromDate(),
                    report.getToDate(),
                    cuaHangId);

            Sheet exchangeDetail = workbook.createSheet("ChiTietDoiHang");
            writeExchangeDetailSheet(exchangeDetail, exchangeLines, "Chi tiết đổi hàng", report.getFromDate(),
                    report.getToDate(), cuaHangId);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportPromotionReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId) throws IOException {
        ResThongKeDTO.PromotionReport report = getPromotionReport(fromDate, toDate, cuaHangId);
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);
        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang);
        List<OrderLineDetail> promoLines = collectOrderLineDetails(orders).stream()
                .filter(line -> line.maKhuyenMaiHoaDon() != null || line.maKhuyenMaiDiem() != null)
                .toList();
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

            Sheet detail = workbook.createSheet("DonApDungKM");
            writeOrderDetailSheet(detail, promoLines, "Chi tiết đơn hàng áp dụng khuyến mãi", report.getFromDate(),
                    report.getToDate(), cuaHangId);

            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportStaffPerformanceReport(LocalDate fromDate, LocalDate toDate, Long cuaHangId)
            throws IOException {
        ResThongKeDTO.StaffPerformanceReport report = getStaffPerformanceReport(fromDate, toDate, cuaHangId);
        LocalDate start = normalizeFromDate(fromDate);
        LocalDate end = normalizeToDate(toDate);
        List<DonHang> orders = filterByStoreAndDate(donHangRepository.findAll(), DonHang::getNgayTao, start, end,
                cuaHangId, DonHang::getCuaHang).stream()
                .filter(order -> order.getNhanVien() != null && order.getNhanVien().getId() != null)
                .toList();
        List<OrderLineDetail> staffLines = collectOrderLineDetails(orders);
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

            Sheet orderDetail = workbook.createSheet("ChiTietDonHang");
            writeOrderDetailSheet(orderDetail, staffLines, "Chi tiết đơn hàng theo nhân viên", report.getFromDate(),
                    report.getToDate(), cuaHangId);

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
