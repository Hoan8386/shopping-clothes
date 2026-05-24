package com.vn.shopping.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.request.ReqApDungKhuyenMaiDTO;
import com.vn.shopping.domain.request.ReqCapNhatDonHangDTO;
import com.vn.shopping.domain.request.ReqTaoDonHangDTO;
import com.vn.shopping.domain.request.ReqTaoDonHangTaiQuayDTO;
import com.vn.shopping.domain.response.ResDonHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.*;
import com.vn.shopping.util.error.IdInvalidException;

import jakarta.persistence.EntityManager;

@Service
public class DonHangService {

    private static final int STATUS_CONFIRMED = 1;
    private static final int STATUS_PACKING = 2;
    private static final int STATUS_SHIPPING = 3;

    private final DonHangRepository donHangRepository;
    private final EntityManager entityManager;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final GioHangRepository gioHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;
    private final CuaHangRepository cuaHangRepository;
    private final VanChuyenRepository vanChuyenRepository;
    private final KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository;
    private final KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository;
    private final GioHangService gioHangService;
    private final VNPayService vnPayService;

    public DonHangService(DonHangRepository donHangRepository, EntityManager entityManager,
            KhachHangRepository khachHangRepository, NhanVienRepository nhanVienRepository,
            GioHangRepository gioHangRepository, ChiTietDonHangRepository chiTietDonHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository, SanPhamRepository sanPhamRepository,
            CuaHangRepository cuaHangRepository, VanChuyenRepository vanChuyenRepository,
            KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository,
            KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository,
            GioHangService gioHangService,
            VNPayService vnPayService) {
        this.donHangRepository = donHangRepository;
        this.entityManager = entityManager;
        this.khachHangRepository = khachHangRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.gioHangRepository = gioHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.cuaHangRepository = cuaHangRepository;
        this.vanChuyenRepository = vanChuyenRepository;
        this.khuyenMaiTheoDiemRepository = khuyenMaiTheoDiemRepository;
        this.khuyenMaiTheoHoaDonRepository = khuyenMaiTheoHoaDonRepository;
        this.gioHangService = gioHangService;
        this.vnPayService = vnPayService;
    }

    @Transactional
    public DonHang save(DonHang donHang) {
        DonHang saved = donHangRepository.save(donHang);
        entityManager.flush();
        entityManager.clear();
        return donHangRepository.findById(saved.getId()).orElse(saved);
    }

    /**
     * KHÁCH HÀNG đặt hàng online:
     * - Lấy thông tin khách hàng từ token (SecurityContext)
     * - Lấy giỏ hàng của khách → tạo chi tiết đơn hàng
     * - Xóa giỏ hàng sau khi tạo đơn
     * - HinhThucDonHang = 1 (online)
     */
    @Transactional
    public DonHang taoDonHangOnline(ReqTaoDonHangDTO req) throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));
        return taoDonHangOnlineTheoKhachHang(req, khachHang);
    }

    @Transactional
    public DonHang taoDonHangOnlineByEmail(ReqTaoDonHangDTO req, String customerEmail) throws IdInvalidException {
        if (customerEmail == null || customerEmail.isBlank()) {
            throw new IdInvalidException("Không xác định được khách hàng để tạo đơn hàng");
        }

        KhachHang khachHang = khachHangRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + customerEmail));
        return taoDonHangOnlineTheoKhachHang(req, khachHang);
    }

    @Transactional(readOnly = true)
    public String taoLinkThanhToanOnlineVNPay(ReqTaoDonHangDTO req, String ipAddr) throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId())
                .orElseThrow(() -> new IdInvalidException("Giỏ hàng trống, không thể thanh toán"));

        if (gioHang.getChiTietGioHangs() == null || gioHang.getChiTietGioHangs().isEmpty()) {
            throw new IdInvalidException("Giỏ hàng trống, không thể thanh toán");
        }

        if (req == null) {
            throw new IdInvalidException("Thông tin thanh toán không hợp lệ");
        }

        if (req.getSdt() == null || req.getSdt().isBlank()) {
            throw new IdInvalidException("Vui lòng nhập số điện thoại nhận hàng");
        }

        if (req.getVanChuyenId() == null) {
            throw new IdInvalidException("Vui lòng chọn bên vận chuyển");
        }

        if (req.getDiaChi() == null || req.getDiaChi().isBlank()) {
            throw new IdInvalidException("Vui lòng nhập địa chỉ giao hàng");
        }

        if (req.getHinhThucDonHang() == null || req.getHinhThucDonHang() != 1) {
            throw new IdInvalidException("Checkout VNPAY online yêu cầu hinhThucDonHang = 1");
        }

        ReqApDungKhuyenMaiDTO previewReq = new ReqApDungKhuyenMaiDTO();
        previewReq.setMaKhuyenMaiHoaDon(req.getMaKhuyenMaiHoaDon());
        previewReq.setMaKhuyenMaiDiem(req.getMaKhuyenMaiDiem());

        int tongThanhToan = gioHangService.xemTruocKhuyenMai(previewReq).getTongTienTra();
        if (tongThanhToan <= 0) {
            throw new IdInvalidException("Tổng thanh toán không hợp lệ để tạo link VNPAY");
        }

        return vnPayService.createPaymentUrlForOnlineCartWithPending(
                gioHang.getMaGioHang(),
                tongThanhToan,
                ipAddr,
                email,
                req);
    }

    @Transactional
    public DonHang hoanTatDonHangOnlineTuCallback(String txnRef, String transactionNo) throws IdInvalidException {
        if (transactionNo != null && !transactionNo.isBlank()) {
            Optional<DonHang> existingByPaymentRef = donHangRepository.findByPaymentRef(transactionNo);
            if (existingByPaymentRef.isPresent()) {
                return existingByPaymentRef.get();
            }
        }

        VNPayService.PendingOnlineCheckout pending = vnPayService.consumePendingOnlineCheckout(txnRef);
        if (pending == null) {
            throw new IdInvalidException("Không tìm thấy phiên thanh toán online đang chờ xác nhận");
        }

        DonHang donHang = taoDonHangOnlineByEmail(pending.getRequest(), pending.getCustomerEmail());
        donHang.setTrangThaiThanhToan(1);
        donHang.setPaymentRef((transactionNo != null && !transactionNo.isBlank()) ? transactionNo : txnRef);
        return save(donHang);
    }

    private DonHang taoDonHangOnlineTheoKhachHang(ReqTaoDonHangDTO req, KhachHang khachHang) throws IdInvalidException {
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId())
                .orElseThrow(() -> new IdInvalidException("Giỏ hàng trống, không thể tạo đơn hàng"));

        List<ChiTietGioHang> chiTietGioHangs = gioHang.getChiTietGioHangs();
        if (chiTietGioHangs == null || chiTietGioHangs.isEmpty()) {
            throw new IdInvalidException("Giỏ hàng trống, không thể tạo đơn hàng");
        }

        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setDiaChi(req.getDiaChi());
        donHang.setSdt(req.getSdt());
        donHang.setMaKhuyenMaiHoaDon(req.getMaKhuyenMaiHoaDon());
        donHang.setMaKhuyenMaiDiem(req.getMaKhuyenMaiDiem());
        // Đặt rõ ràng: đơn online
        donHang.setHinhThucDonHang(0);
        // Sử dụng trường req.hinhThucDonHang làm phương thức thanh toán (0=COD,1=VNPAY)
        donHang.setPhuongThucThanhToan(req.getHinhThucDonHang() != null ? req.getHinhThucDonHang() : 0);
        donHang.setTrangThai(0);
        donHang.setTrangThaiThanhToan(0);

        if (req.getCuaHangId() != null) {
            CuaHang ch = cuaHangRepository.findById(req.getCuaHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng: " + req.getCuaHangId()));
            donHang.setCuaHang(ch);
        }

        if (req.getVanChuyenId() == null) {
            throw new IdInvalidException("Vui lòng chọn bên vận chuyển");
        }
        VanChuyen vanChuyen = vanChuyenRepository.findById(req.getVanChuyenId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy bên vận chuyển: " + req.getVanChuyenId()));
        donHang.setVanChuyen(vanChuyen);

        DonHang savedDonHang = donHangRepository.save(donHang);

        if (savedDonHang.getHinhThucDonHang() != null && savedDonHang.getHinhThucDonHang() == 1) {
            savedDonHang.setPaymentRef(String.valueOf(savedDonHang.getId()));
        }

        int tongTien = 0;
        List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();

        for (ChiTietGioHang ctgh : chiTietGioHangs) {
            ChiTietSanPham ctsp = ctgh.getChiTietSanPham();
            SanPham sp = ctsp.getSanPham();

            Double giaBan = sp.getGiaBan() != null ? sp.getGiaBan() : 0;
            Integer phanTramGiamSanPham = sp.getGiaGiam() != null ? sp.getGiaGiam() : 0;
            Double giaSauGiamSanPham = giaBan - (giaBan * phanTramGiamSanPham / 100);
            Double thanhTien = giaSauGiamSanPham * ctgh.getSoLuong();

            ChiTietDonHang ctdh = new ChiTietDonHang();
            ctdh.setDonHang(savedDonHang);
            ctdh.setChiTietSanPham(ctsp);
            ctdh.setGiaSanPham(giaSauGiamSanPham);
            ctdh.setGiamGia(0d);
            ctdh.setGiaGiam(0d);
            ctdh.setSoLuong(ctgh.getSoLuong());
            ctdh.setThanhTien(thanhTien);

            chiTietDonHangs.add(ctdh);
            tongTien += thanhTien.intValue();
        }

        chiTietDonHangRepository.saveAll(chiTietDonHangs);

        savedDonHang.setTongTien(tongTien);
        savedDonHang.setTienGiam(0);
        savedDonHang.setTongTienGiam(0);
        savedDonHang.setTongTienTra(tongTien);

        apDungKhuyenMaiTheoHoaDon(savedDonHang, req.getMaKhuyenMaiHoaDon());
        apDungKhuyenMaiTheoDiem(savedDonHang, khachHang, req.getMaKhuyenMaiDiem());
        phanBoGiamGiaToanDonChoChiTiet(savedDonHang, chiTietDonHangs);

        donHangRepository.save(savedDonHang);
        chiTietDonHangRepository.saveAll(chiTietDonHangs);

        truSoLuongSanPham(chiTietDonHangs);

        gioHang.getChiTietGioHangs().clear();
        gioHangRepository.save(gioHang);

        entityManager.flush();
        entityManager.clear();
        return donHangRepository.findById(savedDonHang.getId()).orElse(savedDonHang);
    }

    /**
     * NHÂN VIÊN tạo đơn hàng tại quầy:
     * - Lấy thông tin nhân viên từ token (SecurityContext)
     * - Nhân viên truyền thông tin khách hàng + danh sách sản phẩm
     * - HinhThucDonHang = 0 (tại quầy)
     */
    @Transactional
    public DonHang taoDonHangTaiQuay(ReqTaoDonHangTaiQuayDTO req) throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên với email: " + email));
        return taoDonHangTaiQuayVoiNhanVien(req, nhanVien);
    }

    @Transactional
    public DonHang taoDonHangTaiQuayByNhanVienId(ReqTaoDonHangTaiQuayDTO req, Long nhanVienId)
            throws IdInvalidException {
        if (nhanVienId == null) {
            throw new IdInvalidException("Không xác định được nhân viên xử lý đơn hàng");
        }

        NhanVien nhanVien = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên: " + nhanVienId));
        return taoDonHangTaiQuayVoiNhanVien(req, nhanVien);
    }

    private DonHang taoDonHangTaiQuayVoiNhanVien(ReqTaoDonHangTaiQuayDTO req, NhanVien nhanVien)
            throws IdInvalidException {

        if (nhanVien.getCuaHang() == null || nhanVien.getCuaHang().getId() == null) {
            throw new IdInvalidException("Nhân viên chưa được gán cửa hàng");
        }

        if (req.getChiTietDonHangs() == null || req.getChiTietDonHangs().isEmpty()) {
            throw new IdInvalidException("Đơn hàng tại quầy phải có ít nhất 1 sản phẩm");
        }

        DonHang donHang = new DonHang();
        donHang.setNhanVien(nhanVien);
        donHang.setCuaHang(nhanVien.getCuaHang());
        // Đặt rõ ràng: đơn tại quầy
        donHang.setHinhThucDonHang(1);
        // Phương thức thanh toán có thể do nhân viên truyền (mặc định COD=0)
        donHang.setPhuongThucThanhToan(req.getHinhThucDonHang() != null ? req.getHinhThucDonHang() : 0);
        donHang.setTrangThai(5); // Đã nhận hàng tại quầy
        donHang.setTrangThaiThanhToan(1); // Đã thanh toán
        donHang.setDiaChi("Mua tại cửa hàng");

        KhachHang khachHang = null;
        if (req.getKhachHangId() != null) {
            khachHang = khachHangRepository.findById(req.getKhachHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng: " + req.getKhachHangId()));
        } else if (req.getSdt() != null && !req.getSdt().isBlank()) {
            khachHang = khachHangRepository.findBySdt(req.getSdt().trim()).orElse(null);
        }

        donHang.setKhachHang(khachHang);
        donHang.setSdt(req.getSdt());
        donHang.setTenNguoiMua(khachHang != null ? khachHang.getTenKhachHang() : req.getTenNguoiMua());
        donHang.setMaKhuyenMaiHoaDon(req.getMaKhuyenMaiHoaDon());
        donHang.setMaKhuyenMaiDiem(req.getMaKhuyenMaiDiem());

        List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();
        int tongTien = 0;

        for (ReqTaoDonHangTaiQuayDTO.ChiTietDonTaiQuayDTO item : req.getChiTietDonHangs()) {
            if (item.getChiTietSanPhamId() == null || item.getSoLuong() == null || item.getSoLuong() <= 0) {
                throw new IdInvalidException("Thông tin chi tiết đơn hàng không hợp lệ");
            }

            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(item.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết sản phẩm: " + item.getChiTietSanPhamId()));

            if (!nhanVien.getCuaHang().getId().equals(ctsp.getMaCuaHang())) {
                throw new IdInvalidException("Sản phẩm " + item.getChiTietSanPhamId()
                        + " không thuộc cửa hàng hiện tại của nhân viên");
            }

            int tonKho = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;
            if (tonKho < item.getSoLuong()) {
                throw new IdInvalidException("Sản phẩm " + item.getChiTietSanPhamId()
                        + " không đủ tồn kho. Còn: " + tonKho);
            }

            SanPham sp = ctsp.getSanPham();
            double giaBan = sp != null && sp.getGiaBan() != null ? sp.getGiaBan() : 0;
            int giamSanPham = sp != null && sp.getGiaGiam() != null ? sp.getGiaGiam() : 0;
            double giaSauGiamSanPham = giaBan - (giaBan * giamSanPham / 100.0);
            double thanhTien = giaSauGiamSanPham * item.getSoLuong();

            ChiTietDonHang ctdh = new ChiTietDonHang();
            ctdh.setChiTietSanPham(ctsp);
            ctdh.setGiaSanPham(giaSauGiamSanPham);
            ctdh.setGiamGia(0d);
            ctdh.setGiaGiam(0d);
            ctdh.setSoLuong(item.getSoLuong());
            ctdh.setThanhTien(thanhTien);

            chiTietDonHangs.add(ctdh);
            tongTien += (int) thanhTien;
        }

        donHang.setTongTien(tongTien);
        donHang.setTienGiam(0);
        donHang.setTongTienGiam(0);
        donHang.setTongTienTra(tongTien);

        DonHang saved = donHangRepository.save(donHang);

        for (ChiTietDonHang ct : chiTietDonHangs) {
            ct.setDonHang(saved);
        }
        chiTietDonHangRepository.saveAll(chiTietDonHangs);

        apDungKhuyenMaiTheoHoaDon(saved, req.getMaKhuyenMaiHoaDon());
        apDungKhuyenMaiTheoDiem(saved, khachHang, req.getMaKhuyenMaiDiem());
        phanBoGiamGiaToanDonChoChiTiet(saved, chiTietDonHangs);

        donHangRepository.save(saved);
        chiTietDonHangRepository.saveAll(chiTietDonHangs);

        // Trừ số lượng sản phẩm từ chi tiết đơn hàng
        truSoLuongSanPham(chiTietDonHangs);

        entityManager.flush();
        entityManager.clear();
        return donHangRepository.findById(saved.getId()).orElse(saved);
    }

    /**
     * Cập nhật trạng thái đơn hàng + địa chỉ (nếu chưa đóng gói).
     * Trạng thái: 0=đã đặt hàng, 1=đã nhận đơn, 2=đang đóng gói, 3=đã gửi, 4=hủy,
     * 5=đã nhận hàng
     *
     * Luồng chuyển trạng thái:
     * - Nhân viên: 0→1, 1→2, 2→3
     * - Khách hàng: 3→5 (xác nhận đã nhận hàng)
     * - Hủy đơn: 0→4, 1→4 (chỉ khi chưa đóng gói)
     * - Địa chỉ/SĐT chỉ được cập nhật khi trạng thái hiện tại < 2
     */
    @Transactional
    public DonHang capNhatDonHang(ReqCapNhatDonHangDTO req) throws IdInvalidException {
        if (req.getId() == null || req.getId() == 0) {
            throw new IdInvalidException("Mã đơn hàng không được để trống");
        }

        DonHang existing = donHangRepository.findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng: " + req.getId()));

        Integer trangThaiCu = existing.getTrangThai() != null ? existing.getTrangThai() : 0;

        // Cập nhật địa chỉ + SĐT: chỉ cho phép khi chưa đóng gói (trangThai < 2)
        if (req.getDiaChi() != null) {
            if (trangThaiCu >= 2) {
                throw new IdInvalidException("Không thể cập nhật địa chỉ khi đơn hàng đã đóng gói hoặc đã gửi");
            }
            existing.setDiaChi(req.getDiaChi());
        }

        if (req.getSdt() != null) {
            if (trangThaiCu >= 2) {
                throw new IdInvalidException("Không thể cập nhật số điện thoại khi đơn hàng đã đóng gói hoặc đã gửi");
            }
            existing.setSdt(req.getSdt());
        }

        if (req.getTrangThaiThanhToan() != null) {
            existing.setTrangThaiThanhToan(req.getTrangThaiThanhToan());
        }

        if (req.getHinhThucDonHang() != null) {
            existing.setHinhThucDonHang(req.getHinhThucDonHang());
        }

        if (req.getPaymentRef() != null) {
            existing.setPaymentRef(req.getPaymentRef());
        }

        // Cập nhật trạng thái
        if (req.getTrangThai() != null) {

            Integer trangThaiMoi = req.getTrangThai();

            validateChuyenTrangThai(trangThaiCu, trangThaiMoi);

            existing.setTrangThai(trangThaiMoi);

            // Khi khách xác nhận đã nhận hàng, đơn được xem là đã thanh toán
            if (trangThaiMoi == 5) {
                existing.setTrangThaiThanhToan(1);
            }

            // Nếu trạng thái được cập nhật sang các trạng thái nội bộ
            if (trangThaiMoi == STATUS_CONFIRMED
                    || trangThaiMoi == STATUS_PACKING
                    || trangThaiMoi == STATUS_SHIPPING) {

                Authentication auth = SecurityContextHolder
                        .getContext()
                        .getAuthentication();

                if (auth != null && auth.isAuthenticated()) {

                    String email = auth.getName();

                    boolean isAdmin = auth.getAuthorities().stream()
                            .anyMatch(a -> "ADMIN".equals(a.getAuthority()));

                    // Nếu đơn đã có nhân viên xử lý trước đó
                    if (existing.getNhanVien() != null
                            && existing.getNhanVien().getEmail() != null) {

                        String emailNhanVienCu = existing.getNhanVien().getEmail();

                        // Không phải admin và không phải chính người đang xử lý
                        if (!isAdmin && !emailNhanVienCu.equals(email)) {

                            throw new IdInvalidException(
                                    "Đơn hàng đã được nhân viên khác xử lý");
                        }
                    }

                    // Gán nhân viên hiện tại vào đơn hàng
                    NhanVien nhanVien = nhanVienRepository.findByEmail(email)
                            .orElseThrow(() -> new IdInvalidException(
                                    "Không tìm thấy nhân viên với email: " + email));

                    existing.setNhanVien(nhanVien);
                }
            }
            // Cộng điểm tích lũy khi khách xác nhận nhận hàng
            if (trangThaiMoi == 5 && trangThaiCu != 5) {
                congDiemTichLuy(existing);
            }
        }

        DonHang saved = donHangRepository.save(existing);

        entityManager.flush();
        entityManager.clear();

        return donHangRepository.findById(saved.getId()).orElse(saved);
    }

    /**
     * Kiểm tra chuyển trạng thái hợp lệ.
     * 0→1, 1→2, 2→3, 3→5: luồng chính
     * 0→4, 1→4: hủy đơn (chỉ khi chưa đóng gói)
     */
    private void validateChuyenTrangThai(Integer trangThaiCu, Integer trangThaiMoi) throws IdInvalidException {
        boolean hopLe = false;

        if (trangThaiMoi == 4) {
            // Hủy đơn: chỉ cho phép khi chưa đóng gói (0 hoặc 1)
            hopLe = (trangThaiCu == 0 || trangThaiCu == 1);
        } else {
            // Luồng chính: 0→1, 1→2, 2→3, 3→5
            hopLe = (trangThaiCu == 0 && trangThaiMoi == 1)
                    || (trangThaiCu == 1 && trangThaiMoi == 2)
                    || (trangThaiCu == 2 && trangThaiMoi == 3)
                    || (trangThaiCu == 3 && trangThaiMoi == 5);
        }

        if (!hopLe) {
            throw new IdInvalidException(
                    "Không thể chuyển trạng thái từ " + trangThaiCu + " sang " + trangThaiMoi);
        }
    }

    public void delete(long id) {
        donHangRepository.deleteById(id);
    }

    /**
     * Cộng điểm tích lũy cho khách hàng khi xác nhận đã nhận hàng (trạng thái 5).
     * Mỗi 100.000 VND = 10 điểm
     */
    private void congDiemTichLuy(DonHang donHang) {
        if (donHang.getKhachHang() == null || donHang.getKhachHang().getId() == null) {
            return;
        }

        KhachHang khachHang = khachHangRepository.findById(donHang.getKhachHang().getId()).orElse(null);
        if (khachHang == null) {
            return;
        }

        // Lấy tổng tiền trả (sau giảm giá)
        Integer tongTienTra = donHang.getTongTienTra() != null ? donHang.getTongTienTra() : 0;

        // Tính điểm: mỗi 100.000 VND = 10 điểm
        int diemCong = (tongTienTra / 100000) * 10;

        if (diemCong > 0) {
            int diemHienTai = khachHang.getDiemTichLuy() != null ? khachHang.getDiemTichLuy() : 0;
            khachHang.setDiemTichLuy(diemHienTai + diemCong);
            khachHangRepository.save(khachHang);
        }
    }

    public DonHang findById(long id) {
        return donHangRepository.findById(id).orElse(null);
    }

    public List<DonHang> findAll() {
        return donHangRepository.findAll();
    }

    /**
     * Áp dụng khuyến mãi theo hóa đơn khi người dùng nhập mã khuyến mãi.
     * Kiểm tra: tồn tại, đang hoạt động, còn hạn, còn số lượng, đơn hàng đạt
     * hoaDonToiThieu.
     * Tính tiền giảm theo phần trăm, giới hạn giảm tối đa.
     */
    private void apDungKhuyenMaiTheoHoaDon(DonHang donHang, Long maKhuyenMaiHoaDon) throws IdInvalidException {
        if (maKhuyenMaiHoaDon == null) {
            return;
        }

        KhuyenMaiTheoHoaDon km = khuyenMaiTheoHoaDonRepository.findById(maKhuyenMaiHoaDon)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy mã khuyến mãi: " + maKhuyenMaiHoaDon));

        // Kiểm tra trạng thái hoạt động
        if (km.getTrangThai() == null || km.getTrangThai() != 1) {
            throw new IdInvalidException("Mã khuyến mãi không còn hoạt động");
        }

        // Kiểm tra thời gian
        LocalDateTime now = LocalDateTime.now();
        if (km.getThoiGianBatDau() != null && now.isBefore(km.getThoiGianBatDau())) {
            throw new IdInvalidException("Mã khuyến mãi chưa đến thời gian áp dụng");
        }
        if (km.getThoiGianKetThuc() != null && now.isAfter(km.getThoiGianKetThuc())) {
            throw new IdInvalidException("Mã khuyến mãi đã hết hạn");
        }

        // Kiểm tra số lượng
        if (km.getSoLuong() != null && km.getSoLuong() <= 0) {
            throw new IdInvalidException("Mã khuyến mãi đã hết lượt sử dụng");
        }

        int tongTien = donHang.getTongTien() != null ? donHang.getTongTien() : 0;

        // Kiểm tra hóa đơn tối thiểu (đơn hàng phải đạt giá trị tối thiểu)
        if (km.getHoaDonToiThieu() != null && km.getHoaDonToiThieu() > 0 && tongTien < km.getHoaDonToiThieu()) {
            throw new IdInvalidException(
                    "Đơn hàng chưa đạt giá trị tối thiểu (" + km.getHoaDonToiThieu() + ") của mã khuyến mãi");
        }

        int tienGiam = 0;

        if (km.getPhanTramGiam() != null && km.getPhanTramGiam() > 0) {
            tienGiam = (int) (tongTien * km.getPhanTramGiam() / 100);

            // Áp dụng giới hạn giảm tối đa
            if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0 && tienGiam > km.getGiamToiDa()) {
                tienGiam = km.getGiamToiDa();
            }
        }

        if (tienGiam > 0) {
            donHang.setMaKhuyenMaiHoaDon(km.getId());
            donHang.setTienGiam(tienGiam);
            donHang.setTongTienGiam(tienGiam);
            donHang.setTongTienTra(tongTien - tienGiam);

            // Trừ số lượng khuyến mãi
            if (km.getSoLuong() != null && km.getSoLuong() > 0) {
                km.setSoLuong(km.getSoLuong() - 1);
                khuyenMaiTheoHoaDonRepository.save(km);
            }
        }
    }

    /**
     * Áp dụng khuyến mãi theo điểm tích lũy theo mã người dùng chọn.
     * - Người dùng phải truyền maKhuyenMaiDiem
     * - Kiểm tra mã hợp lệ, còn hạn, còn số lượng, đủ điểm
     * - Trừ điểm tích lũy của khách hàng theo mức điểm tối thiểu của mã
     */
    private void apDungKhuyenMaiTheoDiem(DonHang donHang, KhachHang khachHang, Long maKhuyenMaiDiem)
            throws IdInvalidException {
        if (maKhuyenMaiDiem == null) {
            return;
        }

        if (khachHang == null || khachHang.getDiemTichLuy() == null || khachHang.getDiemTichLuy() <= 0) {
            throw new IdInvalidException("Bạn không đủ điểm tích lũy để áp dụng khuyến mãi");
        }

        KhuyenMaiTheoDiem km = khuyenMaiTheoDiemRepository.findById(maKhuyenMaiDiem)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy mã khuyến mãi điểm: " + maKhuyenMaiDiem));

        // Kiểm tra trạng thái hoạt động
        if (km.getTrangThai() == null || km.getTrangThai() != 1) {
            throw new IdInvalidException("Mã khuyến mãi điểm không còn hoạt động");
        }

        // Kiểm tra thời gian hiệu lực
        LocalDateTime now = LocalDateTime.now();
        if (km.getThoiGianBatDau() != null && now.isBefore(km.getThoiGianBatDau())) {
            throw new IdInvalidException("Mã khuyến mãi điểm chưa đến thời gian áp dụng");
        }
        if (km.getThoiGianKetThuc() != null && now.isAfter(km.getThoiGianKetThuc())) {
            throw new IdInvalidException("Mã khuyến mãi điểm đã hết hạn");
        }

        // Kiểm tra số lượng
        if (km.getSoLuong() != null && km.getSoLuong() <= 0) {
            throw new IdInvalidException("Mã khuyến mãi điểm đã hết lượt sử dụng");
        }

        int tongTien = donHang.getTongTien() != null ? donHang.getTongTien() : 0;
        int diemKhachHang = khachHang.getDiemTichLuy();
        int diemCan = km.getDiemToiThieu() != null ? km.getDiemToiThieu() : 0;

        if (diemCan <= 0) {
            throw new IdInvalidException("Mã khuyến mãi điểm không hợp lệ");
        }

        if (diemKhachHang < diemCan) {
            throw new IdInvalidException("Bạn không đủ điểm để áp dụng mã khuyến mãi này");
        }

        // Kiểm tra hóa đơn tối thiểu
        if (km.getHoaDonToiThieu() != null && km.getHoaDonToiThieu() > 0 && tongTien < km.getHoaDonToiThieu()) {
            throw new IdInvalidException(
                    "Đơn hàng chưa đạt giá trị tối thiểu (" + km.getHoaDonToiThieu() + ") của mã khuyến mãi điểm");
        }

        int tienGiam = 0;
        if (km.getPhanTramGiam() != null && km.getPhanTramGiam() > 0) {
            tienGiam = (int) (tongTien * km.getPhanTramGiam() / 100);

            if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0 && tienGiam > km.getGiamToiDa()) {
                tienGiam = km.getGiamToiDa();
            }
        }

        if (tienGiam <= 0) {
            throw new IdInvalidException("Mã khuyến mãi điểm không tạo ra giá trị giảm hợp lệ");
        }

        int tienGiamHienTai = donHang.getTienGiam() != null ? donHang.getTienGiam() : 0;
        int tongTienGiamHienTai = donHang.getTongTienGiam() != null ? donHang.getTongTienGiam() : 0;
        int tongTienGiamMoi = tongTienGiamHienTai + tienGiam;

        donHang.setMaKhuyenMaiDiem(km.getId());
        donHang.setTienGiam(tienGiamHienTai + tienGiam);
        donHang.setTongTienGiam(tongTienGiamMoi);
        donHang.setTongTienTra(Math.max(tongTien - tongTienGiamMoi, 0));

        // Trừ số lượng khuyến mãi
        if (km.getSoLuong() != null && km.getSoLuong() > 0) {
            km.setSoLuong(km.getSoLuong() - 1);
            khuyenMaiTheoDiemRepository.save(km);
        }

        // Trừ điểm tích lũy theo mức điểm yêu cầu của mã
        khachHang.setDiemTichLuy(diemKhachHang - diemCan);
        khachHangRepository.save(khachHang);
    }

    /**
     * Phân bổ tổng giảm giá của hóa đơn xuống từng dòng sản phẩm theo tỷ trọng
     * thành tiền.
     * - tongTienChuaGiam = tổng tiền trước mã giảm (đã gồm giảm theo sản phẩm)
     * - tongTienSauGiam = tổng tiền phải trả sau tất cả mã giảm
     * - tỷ lệ giảm = (tongTienChuaGiam - tongTienSauGiam) / tongTienChuaGiam
     */
    private void phanBoGiamGiaToanDonChoChiTiet(DonHang donHang, List<ChiTietDonHang> chiTietDonHangs) {
        if (chiTietDonHangs == null || chiTietDonHangs.isEmpty()) {
            return;
        }

        double tongTienChuaGiam = donHang.getTongTien() != null ? donHang.getTongTien() : 0;
        double tongTienSauGiam = donHang.getTongTienTra() != null ? donHang.getTongTienTra() : tongTienChuaGiam;
        double tongTienGiam = Math.max(tongTienChuaGiam - tongTienSauGiam, 0);

        if (tongTienChuaGiam <= 0 || tongTienGiam <= 0) {
            for (ChiTietDonHang ct : chiTietDonHangs) {
                ct.setGiamGia(0d);
                ct.setGiaGiam(0d);
                double gia = ct.getGiaSanPham() != null ? ct.getGiaSanPham() : 0;
                int soLuong = ct.getSoLuong() != null ? ct.getSoLuong() : 0;
                ct.setThanhTien(gia * soLuong);
            }
            return;
        }

        double tongDaPhanBo = 0;
        for (int i = 0; i < chiTietDonHangs.size(); i++) {
            ChiTietDonHang ct = chiTietDonHangs.get(i);
            double giaDonVi = ct.getGiaSanPham() != null ? ct.getGiaSanPham() : 0;
            int soLuong = ct.getSoLuong() != null ? ct.getSoLuong() : 0;
            double thanhTienDong = giaDonVi * soLuong;

            double giamDong;
            if (i == chiTietDonHangs.size() - 1) {
                giamDong = Math.max(tongTienGiam - tongDaPhanBo, 0);
            } else {
                giamDong = (thanhTienDong / tongTienChuaGiam) * tongTienGiam;
                tongDaPhanBo += giamDong;
            }

            // Không để giảm vượt quá thành tiền dòng.
            giamDong = Math.min(giamDong, thanhTienDong);

            double giaGiamDonVi = soLuong > 0 ? giamDong / soLuong : 0;
            double phanTramGiam = giaDonVi > 0 ? (giaGiamDonVi / giaDonVi) * 100 : 0;
            double thanhTienSauGiam = Math.max(thanhTienDong - giamDong, 0);

            ct.setGiaGiam(giaGiamDonVi);
            ct.setGiamGia(phanTramGiam);
            ct.setThanhTien(thanhTienSauGiam);
        }
    }

    /**
     * Trừ số lượng ChiTietSanPham và tính lại tổng SanPham khi đặt hàng.
     * - Trừ soLuong từ ChiTietDonHang khỏi ChiTietSanPham tương ứng
     * - Tính lại tổng soLuong trên SanPham = tổng soLuong của tất cả ChiTietSanPham
     */
    private void truSoLuongSanPham(List<ChiTietDonHang> chiTietDonHangs) {
        for (ChiTietDonHang ctdh : chiTietDonHangs) {
            if (ctdh.getChiTietSanPham() == null || ctdh.getSoLuong() == null || ctdh.getSoLuong() <= 0) {
                continue;
            }

            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(ctdh.getChiTietSanPham().getId())
                    .orElse(null);
            if (ctsp == null) {
                continue;
            }

            // Trừ số lượng chi tiết sản phẩm
            int soLuongHienTai = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;
            int soLuongMoi = soLuongHienTai - ctdh.getSoLuong();
            ctsp.setSoLuong(Math.max(soLuongMoi, 0));
            chiTietSanPhamRepository.save(ctsp);

            // Tính lại tổng số lượng sản phẩm
            if (ctsp.getSanPham() != null) {
                capNhatTongSoLuongSanPham(ctsp.getSanPham().getId());
            }
        }
    }

    /**
     * Tính lại tổng số lượng sản phẩm = tổng soLuong của tất cả ChiTietSanPham
     */
    private void capNhatTongSoLuongSanPham(Long sanPhamId) {
        SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElse(null);
        if (sanPham == null) {
            return;
        }

        List<ChiTietSanPham> danhSachChiTiet = chiTietSanPhamRepository.findBySanPhamId(sanPhamId);
        int tongSoLuong = danhSachChiTiet.stream()
                .mapToInt(ct -> ct.getSoLuong() != null ? ct.getSoLuong() : 0)
                .sum();

        sanPham.setSoLuong(tongSoLuong);
        sanPhamRepository.save(sanPham);
    }

    /**
     * Lấy danh sách đơn hàng có lọc + phân trang.
     * - Nếu user là KHACH_HANG → bắt buộc lọc theo khachHangId (chỉ xem đơn của
     * mình)
     * - Nếu user là NHAN_VIEN / ADMIN → xem tất cả, có thể lọc tùy ý
     */
    @Transactional(readOnly = true)
    public ResultPaginationDTO findAllWithFilter(
            Long cuaHangId,
            Long nhanVienId,
            Integer trangThai,
            Integer trangThaiThanhToan,
            Integer hinhThucDonHang,
            Pageable pageable) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Xác định user hiện tại là Khách hàng hay Nhân viên
        Long khachHangId = null;
        Optional<KhachHang> khOpt = khachHangRepository.findByEmail(email);
        if (khOpt.isPresent()) {
            // Là khách hàng → chỉ xem đơn hàng của mình
            khachHangId = khOpt.get().getId();
        }
        // Nếu không phải khách hàng thì là nhân viên/admin → khachHangId = null → xem
        // tất cả

        Specification<DonHang> spec = DonHangSpecification.filter(
                khachHangId, cuaHangId, nhanVienId, trangThai, trangThaiThanhToan, hinhThucDonHang);

        Page<DonHang> page = donHangRepository.findAll(spec, pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        List<ResDonHangDTO> dtoList = page.getContent().stream()
                .map(this::convertToDTO)
                .toList();

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(dtoList);
        return result;
    }

    /**
     * Lấy đơn hàng theo id.
     * - KHACH_HANG chỉ được xem đơn hàng của chính mình
     * - NHAN_VIEN / ADMIN xem được tất cả
     */
    @Transactional(readOnly = true)
    public ResDonHangDTO findByIdForCurrentUser(long id) throws IdInvalidException {
        DonHang donHang = donHangRepository.findById(id).orElse(null);
        if (donHang == null) {
            throw new IdInvalidException("Không tìm thấy đơn hàng: " + id);
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<KhachHang> khOpt = khachHangRepository.findByEmail(email);
        if (khOpt.isPresent()) {
            // Là khách hàng → kiểm tra đơn hàng có phải của mình không
            KhachHang kh = khOpt.get();
            if (donHang.getKhachHang() == null || !donHang.getKhachHang().getId().equals(kh.getId())) {
                throw new IdInvalidException("Bạn không có quyền xem đơn hàng này");
            }
        }

        return convertToDTO(donHang);
    }

    public ResDonHangDTO convertToDTO(DonHang donHang) {
        ResDonHangDTO dto = new ResDonHangDTO();
        dto.setId(donHang.getId());
        dto.setDiaChi(donHang.getDiaChi());
        dto.setSdt(donHang.getSdt());
        dto.setTenNguoiMua(donHang.getTenNguoiMua());
        dto.setTongTien(donHang.getTongTien());
        dto.setTienGiam(donHang.getTienGiam());
        dto.setTongTienGiam(donHang.getTongTienGiam());
        dto.setTongTienTra(donHang.getTongTienTra());
        dto.setPaymentRef(donHang.getPaymentRef());
        dto.setTrangThai(mapTrangThai(donHang.getTrangThai()));
        dto.setTrangThaiThanhToan(mapTrangThaiThanhToan(donHang.getTrangThaiThanhToan()));
        dto.setHinhThucDonHang(mapHinhThucDonHang(donHang.getHinhThucDonHang()));
        dto.setPhuongThucThanhToan(mapPhuongThucThanhToan(donHang.getPhuongThucThanhToan()));
        dto.setNgayTao(donHang.getNgayTao());
        dto.setNgayCapNhat(donHang.getNgayCapNhat());

        if (donHang.getVanChuyen() != null) {
            VanChuyen vc = donHang.getVanChuyen();
            dto.setVanChuyen(new ResDonHangDTO.VanChuyenDTO(
                    vc.getId(), vc.getTenVanChuyen(), vc.getSoDienThoai(), vc.getWebsite(), vc.getGhiTru()));
        }

        // Map khuyến mãi theo hóa đơn
        if (donHang.getMaKhuyenMaiHoaDon() != null) {
            int tongTien = donHang.getTongTien() != null ? donHang.getTongTien() : 0;
            khuyenMaiTheoHoaDonRepository.findById(donHang.getMaKhuyenMaiHoaDon()).ifPresent(km -> {
                int tienDaGiam = 0;
                if (km.getPhanTramGiam() != null && km.getPhanTramGiam() > 0) {
                    tienDaGiam = (int) (tongTien * km.getPhanTramGiam() / 100);
                    if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0 && tienDaGiam > km.getGiamToiDa()) {
                        tienDaGiam = km.getGiamToiDa();
                    }
                }
                dto.setKhuyenMaiHoaDon(new ResDonHangDTO.KhuyenMaiHoaDonDTO(
                        km.getId(), km.getTenKhuyenMai(), km.getPhanTramGiam(),
                        km.getGiamToiDa(), km.getHoaDonToiThieu(), tienDaGiam));
            });
        }

        // Map khuyến mãi theo điểm
        if (donHang.getMaKhuyenMaiDiem() != null) {
            int tongTien = donHang.getTongTien() != null ? donHang.getTongTien() : 0;
            khuyenMaiTheoDiemRepository.findById(donHang.getMaKhuyenMaiDiem()).ifPresent(km -> {
                int tienDaGiam = 0;
                if (km.getPhanTramGiam() != null && km.getPhanTramGiam() > 0) {
                    tienDaGiam = (int) (tongTien * km.getPhanTramGiam() / 100);
                    if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0 && tienDaGiam > km.getGiamToiDa()) {
                        tienDaGiam = km.getGiamToiDa();
                    }
                }
                dto.setKhuyenMaiDiem(new ResDonHangDTO.KhuyenMaiDiemDTO(
                        km.getId(), km.getTenKhuyenMai(), km.getPhanTramGiam(),
                        km.getGiamToiDa(), km.getHoaDonToiThieu(), km.getDiemToiThieu(), tienDaGiam));
            });
        }

        if (donHang.getCuaHang() != null) {
            CuaHang ch = donHang.getCuaHang();
            dto.setCuaHang(new ResDonHangDTO.CuaHangDTO(
                    ch.getId(), ch.getTenCuaHang(), ch.getDiaChi(), ch.getSoDienThoai()));
        }

        if (donHang.getKhachHang() != null) {
            KhachHang kh = donHang.getKhachHang();
            dto.setKhachHang(new ResDonHangDTO.KhachHangDTO(
                    kh.getId(), kh.getTenKhachHang(), kh.getSdt(), kh.getEmail(), kh.getDiemTichLuy()));
        }

        if (donHang.getNhanVien() != null) {
            NhanVien nv = donHang.getNhanVien();
            dto.setNhanVien(new ResDonHangDTO.NhanVienDTO(
                    nv.getId(), nv.getTenNhanVien(), nv.getEmail(), nv.getSoDienThoai()));
        }

        if (donHang.getChiTietDonHangs() != null) {
            List<ResDonHangDTO.ChiTietDonHangDTO> chiTietList = donHang.getChiTietDonHangs().stream()
                    .map(ct -> {
                        ResDonHangDTO.ChiTietDonHangDTO ctDto = new ResDonHangDTO.ChiTietDonHangDTO();
                        ctDto.setId(ct.getId());
                        ctDto.setGiaSanPham(ct.getGiaSanPham());
                        ctDto.setGiamGia(ct.getGiamGia());
                        ctDto.setGiaGiam(ct.getGiaGiam());
                        ctDto.setSoLuong(ct.getSoLuong());
                        ctDto.setThanhTien(ct.getThanhTien());

                        if (ct.getChiTietSanPham() != null) {
                            ctDto.setChiTietSanPhamId(ct.getChiTietSanPham().getId());
                            if (ct.getChiTietSanPham().getSanPham() != null) {
                                ctDto.setSanPhamId(ct.getChiTietSanPham().getSanPham().getId());
                                ctDto.setTenSanPham(ct.getChiTietSanPham().getSanPham().getTenSanPham());
                                ctDto.setHinhAnhChinh(ct.getChiTietSanPham().getSanPham().getHinhAnhChinh());
                            }
                            if (ct.getChiTietSanPham().getMauSac() != null) {
                                ctDto.setTenMauSac(ct.getChiTietSanPham().getMauSac().getTenMauSac());
                            }
                            if (ct.getChiTietSanPham().getKichThuoc() != null) {
                                ctDto.setTenKichThuoc(ct.getChiTietSanPham().getKichThuoc().getTenKichThuoc());
                            }
                        }
                        return ctDto;
                    }).toList();
            dto.setChiTietDonHangs(chiTietList);
        }

        return dto;
    }

    private String mapTrangThai(Integer trangThai) {
        if (trangThai == null)
            return null;
        return switch (trangThai) {
            case 0 -> "Chờ xác nhận";
            case 1 -> "Đã xác nhận";
            case 2 -> "Đang đóng gói";
            case 3 -> "Đang giao hàng";
            case 4 -> "Đã hủy";
            case 5 -> "Đã nhận hàng";
            default -> "Không xác định";
        };
    }

    private String mapTrangThaiThanhToan(Integer trangThai) {
        if (trangThai == null)
            return null;
        return switch (trangThai) {
            case 0 -> "Chưa thanh toán";
            case 1 -> "Đã thanh toán";
            case 2 -> "Thanh toán thất bại";
            default -> "Không xác định";
        };
    }

    private String mapHinhThucDonHang(Integer hinhThuc) {
        if (hinhThuc == null)
            return null;
        return switch (hinhThuc) {
            case 0 -> "Online";
            case 1 -> "Tại quầy";
            default -> "Không xác định";
        };
    }

    private String mapPhuongThucThanhToan(Integer phuongThuc) {
        if (phuongThuc == null)
            return null;
        return switch (phuongThuc) {
            case 0 -> "COD/Tiền mặt";
            case 1 -> "VNPAY";
            default -> "Không xác định";
        };
    }
}
