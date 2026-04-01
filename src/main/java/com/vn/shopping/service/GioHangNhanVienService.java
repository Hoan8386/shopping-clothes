package com.vn.shopping.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietGioHangNhanVien;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.GioHangNhanVien;
import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.KhuyenMaiTheoDiem;
import com.vn.shopping.domain.KhuyenMaiTheoHoaDon;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.request.ReqCapNhatKhachGioHangNhanVienDTO;
import com.vn.shopping.domain.request.ReqCapNhatKhuyenMaiGioHangNhanVienDTO;
import com.vn.shopping.domain.request.ReqCapNhatSoLuongGioHangNhanVienDTO;
import com.vn.shopping.domain.request.ReqTaoDonHangTaiQuayDTO;
import com.vn.shopping.domain.request.ReqThanhToanGioHangNhanVienDTO;
import com.vn.shopping.domain.request.ReqThemSanPhamGioHangNhanVienDTO;
import com.vn.shopping.domain.response.ResDonHangDTO;
import com.vn.shopping.domain.response.ResGioHangNhanVienDTO;
import com.vn.shopping.repository.ChiTietGioHangNhanVienRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.DonHangRepository;
import com.vn.shopping.repository.GioHangNhanVienRepository;
import com.vn.shopping.repository.KhachHangRepository;
import com.vn.shopping.repository.KhuyenMaiTheoDiemRepository;
import com.vn.shopping.repository.KhuyenMaiTheoHoaDonRepository;
import com.vn.shopping.repository.NhanVienRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class GioHangNhanVienService {

    private final GioHangNhanVienRepository gioHangNhanVienRepository;
    private final ChiTietGioHangNhanVienRepository chiTietGioHangNhanVienRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository;
    private final KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository;
    private final DonHangService donHangService;
    private final VNPayService vnPayService;
    private final DonHangRepository donHangRepository;

    public GioHangNhanVienService(
            GioHangNhanVienRepository gioHangNhanVienRepository,
            ChiTietGioHangNhanVienRepository chiTietGioHangNhanVienRepository,
            NhanVienRepository nhanVienRepository,
            KhachHangRepository khachHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository,
            KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository,
            DonHangService donHangService,
            VNPayService vnPayService,
            DonHangRepository donHangRepository) {
        this.gioHangNhanVienRepository = gioHangNhanVienRepository;
        this.chiTietGioHangNhanVienRepository = chiTietGioHangNhanVienRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.khachHangRepository = khachHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.khuyenMaiTheoHoaDonRepository = khuyenMaiTheoHoaDonRepository;
        this.khuyenMaiTheoDiemRepository = khuyenMaiTheoDiemRepository;
        this.donHangService = donHangService;
        this.vnPayService = vnPayService;
        this.donHangRepository = donHangRepository;
    }

    private NhanVien getCurrentNhanVien() throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên với email: " + email));
    }

    private GioHangNhanVien getOrCreateCurrentCart() throws IdInvalidException {
        NhanVien nhanVien = getCurrentNhanVien();

        return gioHangNhanVienRepository
                .findFirstByNhanVienIdAndTrangThaiOrderByNgayCapNhatDesc(nhanVien.getId(), 0)
                .orElseGet(() -> {
                    GioHangNhanVien cart = new GioHangNhanVien();
                    cart.setNhanVien(nhanVien);
                    cart.setTrangThai(0);
                    return gioHangNhanVienRepository.save(cart);
                });
    }

    private double getGiaSauGiamSanPham(ChiTietSanPham ctsp) {
        SanPham sp = ctsp.getSanPham();
        if (sp == null) {
            return 0;
        }
        double giaBan = sp.getGiaBan() != null ? sp.getGiaBan() : 0;
        int giamSanPham = sp.getGiaGiam() != null ? sp.getGiaGiam() : 0;
        return giaBan - (giaBan * giamSanPham / 100.0);
    }

    private int tinhTongTienGoc(GioHangNhanVien cart) {
        int tong = 0;
        if (cart.getChiTietGioHangs() == null) {
            return tong;
        }
        for (ChiTietGioHangNhanVien ct : cart.getChiTietGioHangs()) {
            double gia = getGiaSauGiamSanPham(ct.getChiTietSanPham());
            int soLuong = ct.getSoLuong() != null ? ct.getSoLuong() : 0;
            tong += (int) (gia * soLuong);
        }
        return tong;
    }

    private int tinhTienGiamHoaDon(Long maKhuyenMaiHoaDon, int tongTienGoc) throws IdInvalidException {
        if (maKhuyenMaiHoaDon == null) {
            return 0;
        }

        KhuyenMaiTheoHoaDon km = khuyenMaiTheoHoaDonRepository.findById(maKhuyenMaiHoaDon)
                .orElseThrow(
                        () -> new IdInvalidException("Không tìm thấy mã khuyến mãi hóa đơn: " + maKhuyenMaiHoaDon));

        LocalDateTime now = LocalDateTime.now();
        if (km.getTrangThai() == null || km.getTrangThai() != 1) {
            throw new IdInvalidException("Mã khuyến mãi theo hóa đơn không còn hoạt động");
        }
        if (km.getThoiGianBatDau() != null && now.isBefore(km.getThoiGianBatDau())) {
            throw new IdInvalidException("Mã khuyến mãi theo hóa đơn chưa đến thời gian áp dụng");
        }
        if (km.getThoiGianKetThuc() != null && now.isAfter(km.getThoiGianKetThuc())) {
            throw new IdInvalidException("Mã khuyến mãi theo hóa đơn đã hết hạn");
        }
        if (km.getSoLuong() != null && km.getSoLuong() <= 0) {
            throw new IdInvalidException("Mã khuyến mãi theo hóa đơn đã hết lượt sử dụng");
        }
        if (km.getHoaDonToiThieu() != null && km.getHoaDonToiThieu() > 0 && tongTienGoc < km.getHoaDonToiThieu()) {
            throw new IdInvalidException("Giỏ hàng chưa đạt điều kiện áp dụng mã khuyến mãi hóa đơn");
        }

        int tienGiam = 0;
        if (km.getPhanTramGiam() != null && km.getPhanTramGiam() > 0) {
            tienGiam = (int) (tongTienGoc * km.getPhanTramGiam() / 100);
            if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0 && tienGiam > km.getGiamToiDa()) {
                tienGiam = km.getGiamToiDa();
            }
        }
        return Math.max(tienGiam, 0);
    }

    private int tinhTienGiamDiem(Long maKhuyenMaiDiem, KhachHang khachHang, int tongTienGoc) throws IdInvalidException {
        if (maKhuyenMaiDiem == null) {
            return 0;
        }
        if (khachHang == null) {
            throw new IdInvalidException("Cần khách hàng đã đăng ký để áp dụng giảm giá theo điểm");
        }

        KhuyenMaiTheoDiem km = khuyenMaiTheoDiemRepository.findById(maKhuyenMaiDiem)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy mã khuyến mãi điểm: " + maKhuyenMaiDiem));

        LocalDateTime now = LocalDateTime.now();
        if (km.getTrangThai() == null || km.getTrangThai() != 1) {
            throw new IdInvalidException("Mã khuyến mãi theo điểm không còn hoạt động");
        }
        if (km.getThoiGianBatDau() != null && now.isBefore(km.getThoiGianBatDau())) {
            throw new IdInvalidException("Mã khuyến mãi theo điểm chưa đến thời gian áp dụng");
        }
        if (km.getThoiGianKetThuc() != null && now.isAfter(km.getThoiGianKetThuc())) {
            throw new IdInvalidException("Mã khuyến mãi theo điểm đã hết hạn");
        }
        if (km.getSoLuong() != null && km.getSoLuong() <= 0) {
            throw new IdInvalidException("Mã khuyến mãi theo điểm đã hết lượt sử dụng");
        }
        int diemKhachHang = khachHang.getDiemTichLuy() != null ? khachHang.getDiemTichLuy() : 0;
        int diemCan = km.getDiemToiThieu() != null ? km.getDiemToiThieu() : 0;
        if (diemKhachHang < diemCan) {
            throw new IdInvalidException("Khách hàng không đủ điểm tích lũy để áp dụng mã này");
        }
        if (km.getHoaDonToiThieu() != null && km.getHoaDonToiThieu() > 0 && tongTienGoc < km.getHoaDonToiThieu()) {
            throw new IdInvalidException("Giỏ hàng chưa đạt điều kiện áp dụng mã khuyến mãi theo điểm");
        }

        int tienGiam = 0;
        if (km.getPhanTramGiam() != null && km.getPhanTramGiam() > 0) {
            tienGiam = (int) (tongTienGoc * km.getPhanTramGiam() / 100);
            if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0 && tienGiam > km.getGiamToiDa()) {
                tienGiam = km.getGiamToiDa();
            }
        }

        return Math.max(tienGiam, 0);
    }

    @Transactional(readOnly = true)
    public ResGioHangNhanVienDTO getCurrentCart() throws IdInvalidException {
        GioHangNhanVien cart = getOrCreateCurrentCart();
        return toResponse(cart);
    }

    @Transactional
    public ResGioHangNhanVienDTO capNhatThongTinKhach(ReqCapNhatKhachGioHangNhanVienDTO req) throws IdInvalidException {
        return capNhatThongTinKhach(req, null);
    }

    @Transactional
    public ResGioHangNhanVienDTO capNhatThongTinKhach(ReqCapNhatKhachGioHangNhanVienDTO req, Long cartId)
            throws IdInvalidException {
        GioHangNhanVien cart = getCartByIdOrCurrentCart(cartId);

        String sdt = req.getSdt() != null ? req.getSdt().trim() : null;
        cart.setSdt(sdt);

        KhachHang khachHang = null;
        if (sdt != null && !sdt.isBlank()) {
            khachHang = khachHangRepository.findBySdt(sdt).orElse(null);
        }

        if (khachHang != null) {
            cart.setKhachHang(khachHang);
            cart.setTenNguoiMua(khachHang.getTenKhachHang());
        } else {
            cart.setKhachHang(null);
            cart.setTenNguoiMua(req.getTenNguoiMua());
        }

        gioHangNhanVienRepository.save(cart);
        return toResponse(cart);
    }

    @Transactional
    public ResGioHangNhanVienDTO themSanPham(ReqThemSanPhamGioHangNhanVienDTO req) throws IdInvalidException {
        return themSanPham(req, null);
    }

    @Transactional
    public ResGioHangNhanVienDTO themSanPham(ReqThemSanPhamGioHangNhanVienDTO req, Long cartId)
            throws IdInvalidException {
        GioHangNhanVien cart = getCartByIdOrCurrentCart(cartId);
        NhanVien nhanVien = getCurrentNhanVien();

        if (nhanVien.getCuaHang() == null || nhanVien.getCuaHang().getId() == null) {
            throw new IdInvalidException("Nhân viên chưa được gán cửa hàng");
        }

        int soLuongThem = req.getSoLuong() != null && req.getSoLuong() > 0 ? req.getSoLuong() : 1;

        ChiTietSanPham ctsp;
        if (req.getChiTietSanPhamId() != null) {
            ctsp = chiTietSanPhamRepository.findById(req.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết sản phẩm: " + req.getChiTietSanPhamId()));
        } else if (req.getMaVach() != null && !req.getMaVach().isBlank()) {
            ctsp = chiTietSanPhamRepository
                    .findFirstByMaVachAndMaCuaHang(req.getMaVach().trim(), nhanVien.getCuaHang().getId())
                    .orElseThrow(
                            () -> new IdInvalidException("Không tìm thấy sản phẩm theo mã vạch tại cửa hàng hiện tại"));
        } else {
            throw new IdInvalidException("Cần chiTietSanPhamId hoặc maVach để thêm sản phẩm");
        }

        if (!nhanVien.getCuaHang().getId().equals(ctsp.getMaCuaHang())) {
            throw new IdInvalidException("Sản phẩm không thuộc cửa hàng hiện tại");
        }

        int tonKho = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;

        ChiTietGioHangNhanVien item = chiTietGioHangNhanVienRepository
                .findByGioHangNhanVienIdAndChiTietSanPhamId(cart.getId(), ctsp.getId())
                .orElseGet(() -> {
                    ChiTietGioHangNhanVien created = new ChiTietGioHangNhanVien();
                    created.setGioHangNhanVien(cart);
                    created.setChiTietSanPham(ctsp);
                    created.setSoLuong(0);
                    return created;
                });

        int soLuongMoi = (item.getSoLuong() != null ? item.getSoLuong() : 0) + soLuongThem;
        if (soLuongMoi > tonKho) {
            throw new IdInvalidException("Số lượng vượt quá tồn kho hiện tại");
        }

        item.setSoLuong(soLuongMoi);
        chiTietGioHangNhanVienRepository.save(item);

        return toResponse(cart);
    }

    @Transactional
    public ResGioHangNhanVienDTO capNhatSoLuong(Long maChiTietGioHangNhanVien, ReqCapNhatSoLuongGioHangNhanVienDTO req)
            throws IdInvalidException {
        return capNhatSoLuong(maChiTietGioHangNhanVien, req, null);
    }

    @Transactional
    public ResGioHangNhanVienDTO capNhatSoLuong(Long maChiTietGioHangNhanVien, ReqCapNhatSoLuongGioHangNhanVienDTO req,
            Long cartId)
            throws IdInvalidException {
        GioHangNhanVien cart = getCartByIdOrCurrentCart(cartId);
        ChiTietGioHangNhanVien item = chiTietGioHangNhanVienRepository.findById(maChiTietGioHangNhanVien)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy chi tiết giỏ hàng nhân viên"));

        if (!item.getGioHangNhanVien().getId().equals(cart.getId())) {
            throw new IdInvalidException("Không có quyền cập nhật chi tiết giỏ hàng này");
        }

        if (req.getSoLuong() == null || req.getSoLuong() < 1) {
            throw new IdInvalidException("Số lượng phải lớn hơn hoặc bằng 1");
        }

        int tonKho = item.getChiTietSanPham().getSoLuong() != null ? item.getChiTietSanPham().getSoLuong() : 0;
        if (req.getSoLuong() > tonKho) {
            throw new IdInvalidException("Số lượng vượt quá tồn kho hiện tại");
        }

        item.setSoLuong(req.getSoLuong());
        chiTietGioHangNhanVienRepository.save(item);
        return toResponse(cart);
    }

    @Transactional
    public ResGioHangNhanVienDTO xoaSanPham(Long maChiTietGioHangNhanVien) throws IdInvalidException {
        return xoaSanPham(maChiTietGioHangNhanVien, null);
    }

    @Transactional
    public ResGioHangNhanVienDTO xoaSanPham(Long maChiTietGioHangNhanVien, Long cartId) throws IdInvalidException {
        GioHangNhanVien cart = getCartByIdOrCurrentCart(cartId);
        ChiTietGioHangNhanVien item = chiTietGioHangNhanVienRepository.findById(maChiTietGioHangNhanVien)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy chi tiết giỏ hàng nhân viên"));

        if (!item.getGioHangNhanVien().getId().equals(cart.getId())) {
            throw new IdInvalidException("Không có quyền xóa chi tiết giỏ hàng này");
        }

        chiTietGioHangNhanVienRepository.delete(item);
        return toResponse(cart);
    }

    @Transactional
    public ResGioHangNhanVienDTO capNhatKhuyenMai(ReqCapNhatKhuyenMaiGioHangNhanVienDTO req) throws IdInvalidException {
        return capNhatKhuyenMai(req, null);
    }

    @Transactional
    public ResGioHangNhanVienDTO capNhatKhuyenMai(ReqCapNhatKhuyenMaiGioHangNhanVienDTO req, Long cartId)
            throws IdInvalidException {
        GioHangNhanVien cart = getCartByIdOrCurrentCart(cartId);
        cart.setMaKhuyenMaiHoaDon(req.getMaKhuyenMaiHoaDon());
        cart.setMaKhuyenMaiDiem(req.getMaKhuyenMaiDiem());
        gioHangNhanVienRepository.save(cart);
        return toResponse(cart);
    }

    @Transactional
    public ResDonHangDTO thanhToan(ReqThanhToanGioHangNhanVienDTO req) throws IdInvalidException {
        return thanhToan(req, null);
    }

    @Transactional
    public ResDonHangDTO thanhToan(ReqThanhToanGioHangNhanVienDTO req, Long cartId) throws IdInvalidException {
        int hinhThucDonHang = req.getHinhThucDonHang() != null ? req.getHinhThucDonHang() : 0;
        if (hinhThucDonHang == 1) {
            throw new IdInvalidException("Thanh toán VNPAY cần tạo link trước, chỉ tạo đơn sau callback thành công");
        }

        GioHangNhanVien cart = validateCartForCheckout(cartId);
        ReqTaoDonHangTaiQuayDTO taoDonReq = buildCheckoutRequestFromCart(cart, hinhThucDonHang);

        var donHang = donHangService.taoDonHangTaiQuayByNhanVienId(
                taoDonReq,
                cart.getNhanVien() != null ? cart.getNhanVien().getId() : null);

        // Dong gio hien tai va tao gio moi khi can
        cart.setTrangThai(1);
        gioHangNhanVienRepository.save(cart);

        return donHangService.convertToDTO(donHang);
    }

    @Transactional(readOnly = true)
    public String taoLinkThanhToanVNPay(Long cartId, String ipAddr) throws IdInvalidException {
        GioHangNhanVien cart = validateCartForCheckout(cartId);

        int tongTienGoc = tinhTongTienGoc(cart);
        int tienGiamHoaDon = 0;
        int tienGiamDiem = 0;

        try {
            tienGiamHoaDon = tinhTienGiamHoaDon(cart.getMaKhuyenMaiHoaDon(), tongTienGoc);
        } catch (IdInvalidException ex) {
            // Ignore invalid saved promotion when generating VNPay URL and continue with
            // remaining valid discounts.
            tienGiamHoaDon = 0;
        }

        try {
            tienGiamDiem = tinhTienGiamDiem(cart.getMaKhuyenMaiDiem(), cart.getKhachHang(), tongTienGoc);
        } catch (IdInvalidException ex) {
            // Ignore invalid saved point promotion when generating VNPay URL.
            tienGiamDiem = 0;
        }
        int tongThanhToan = Math.max(tongTienGoc - tienGiamHoaDon - tienGiamDiem, 0);

        if (tongThanhToan <= 0) {
            throw new IdInvalidException("Tổng thanh toán không hợp lệ để tạo link VNPAY");
        }

        return vnPayService.createPaymentUrlForStaffCart(cart.getId(), tongThanhToan, ipAddr);
    }

    @Transactional
    public ResDonHangDTO hoanTatThanhToanVNPayTuCallback(String txnRef, String transactionNo)
            throws IdInvalidException {
        Long cartId = vnPayService.extractStaffCartId(txnRef);

        if (transactionNo != null && !transactionNo.isBlank()) {
            var existingOrder = donHangRepository.findByPaymentRef(transactionNo).orElse(null);
            if (existingOrder != null) {
                return donHangService.convertToDTO(existingOrder);
            }
        }

        GioHangNhanVien cart = gioHangNhanVienRepository.findById(cartId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy giỏ hàng: " + cartId));

        if (cart.getTrangThai() != null && cart.getTrangThai() != 0) {
            throw new IdInvalidException("Giỏ hàng đã được thanh toán trước đó");
        }

        ReqTaoDonHangTaiQuayDTO taoDonReq = buildCheckoutRequestFromCart(cart, 1);
        var donHang = donHangService.taoDonHangTaiQuayByNhanVienId(
                taoDonReq,
                cart.getNhanVien() != null ? cart.getNhanVien().getId() : null);

        donHang.setTrangThaiThanhToan(1);
        donHang.setPaymentRef((transactionNo != null && !transactionNo.isBlank()) ? transactionNo : txnRef);
        donHang = donHangService.save(donHang);

        cart.setTrangThai(1);
        gioHangNhanVienRepository.save(cart);

        return donHangService.convertToDTO(donHang);
    }

    private GioHangNhanVien validateCartForCheckout(Long cartId) throws IdInvalidException {
        GioHangNhanVien cart = getCartByIdOrCurrentCart(cartId);

        if (cart.getChiTietGioHangs() == null || cart.getChiTietGioHangs().isEmpty()) {
            throw new IdInvalidException("Giỏ hàng nhân viên đang trống");
        }

        if (cart.getSdt() == null || cart.getSdt().isBlank()) {
            throw new IdInvalidException("Vui lòng nhập số điện thoại người mua trước khi thanh toán");
        }

        if ((cart.getKhachHang() == null) && (cart.getTenNguoiMua() == null || cart.getTenNguoiMua().isBlank())) {
            throw new IdInvalidException("Vui lòng nhập tên người mua trước khi thanh toán");
        }

        return cart;
    }

    private ReqTaoDonHangTaiQuayDTO buildCheckoutRequestFromCart(GioHangNhanVien cart, Integer hinhThucDonHang) {
        ReqTaoDonHangTaiQuayDTO taoDonReq = new ReqTaoDonHangTaiQuayDTO();
        taoDonReq.setTenNguoiMua(cart.getTenNguoiMua());
        taoDonReq.setSdt(cart.getSdt());
        taoDonReq.setKhachHangId(cart.getKhachHang() != null ? cart.getKhachHang().getId() : null);
        taoDonReq.setMaKhuyenMaiHoaDon(cart.getMaKhuyenMaiHoaDon());
        taoDonReq.setMaKhuyenMaiDiem(cart.getMaKhuyenMaiDiem());
        taoDonReq.setHinhThucDonHang(hinhThucDonHang);

        List<ReqTaoDonHangTaiQuayDTO.ChiTietDonTaiQuayDTO> chiTiet = new ArrayList<>();
        for (ChiTietGioHangNhanVien ct : cart.getChiTietGioHangs()) {
            ReqTaoDonHangTaiQuayDTO.ChiTietDonTaiQuayDTO item = new ReqTaoDonHangTaiQuayDTO.ChiTietDonTaiQuayDTO();
            item.setChiTietSanPhamId(ct.getChiTietSanPham().getId());
            item.setSoLuong(ct.getSoLuong());
            chiTiet.add(item);
        }
        taoDonReq.setChiTietDonHangs(chiTiet);
        return taoDonReq;
    }

    private ResGioHangNhanVienDTO toResponse(GioHangNhanVien cart) throws IdInvalidException {
        ResGioHangNhanVienDTO dto = new ResGioHangNhanVienDTO();
        dto.setId(cart.getId());
        dto.setTrangThai(cart.getTrangThai());
        dto.setTenNguoiMua(cart.getTenNguoiMua());
        dto.setSdt(cart.getSdt());
        dto.setMaKhuyenMaiHoaDon(cart.getMaKhuyenMaiHoaDon());
        dto.setMaKhuyenMaiDiem(cart.getMaKhuyenMaiDiem());

        if (cart.getKhachHang() != null) {
            dto.setKhachHangId(cart.getKhachHang().getId());
            dto.setTenKhachHang(cart.getKhachHang().getTenKhachHang());
            dto.setEmailKhachHang(cart.getKhachHang().getEmail());
            dto.setDiemTichLuy(cart.getKhachHang().getDiemTichLuy());
        }

        List<ResGioHangNhanVienDTO.ChiTietDTO> chiTietDTOs = new ArrayList<>();
        int tongSoLuong = 0;
        int tongTienGoc = 0;

        if (cart.getChiTietGioHangs() != null) {
            for (ChiTietGioHangNhanVien ct : cart.getChiTietGioHangs()) {
                ResGioHangNhanVienDTO.ChiTietDTO item = new ResGioHangNhanVienDTO.ChiTietDTO();
                item.setId(ct.getId());
                item.setChiTietSanPhamId(ct.getChiTietSanPham().getId());
                item.setMaVach(ct.getChiTietSanPham().getMaVach());
                item.setSoLuong(ct.getSoLuong());
                item.setTonKho(ct.getChiTietSanPham().getSoLuong());

                if (ct.getChiTietSanPham().getSanPham() != null) {
                    item.setSanPhamId(ct.getChiTietSanPham().getSanPham().getId());
                    item.setTenSanPham(ct.getChiTietSanPham().getSanPham().getTenSanPham());
                }
                if (ct.getChiTietSanPham().getMauSac() != null) {
                    item.setTenMauSac(ct.getChiTietSanPham().getMauSac().getTenMauSac());
                }
                if (ct.getChiTietSanPham().getKichThuoc() != null) {
                    item.setTenKichThuoc(ct.getChiTietSanPham().getKichThuoc().getTenKichThuoc());
                }

                double gia = getGiaSauGiamSanPham(ct.getChiTietSanPham());
                item.setGiaBan(gia);
                item.setThanhTien(gia * ct.getSoLuong());

                chiTietDTOs.add(item);
                tongSoLuong += ct.getSoLuong() != null ? ct.getSoLuong() : 0;
                tongTienGoc += (int) (gia * (ct.getSoLuong() != null ? ct.getSoLuong() : 0));
            }
        }

        dto.setChiTietGioHangs(chiTietDTOs);
        dto.setTongSoLuong(tongSoLuong);
        dto.setTongTienGoc(tongTienGoc);

        LocalDateTime now = LocalDateTime.now();
        List<KhuyenMaiTheoHoaDon> hoaDonHopLe = khuyenMaiTheoHoaDonRepository.findKhuyenMaiHopLe(tongTienGoc, now);
        List<KhuyenMaiTheoDiem> diemHopLe = new ArrayList<>();

        int diemKhachHang = dto.getDiemTichLuy() != null ? dto.getDiemTichLuy() : 0;
        if (dto.getKhachHangId() != null) {
            diemHopLe = khuyenMaiTheoDiemRepository.findKhuyenMaiHopLe(diemKhachHang, tongTienGoc, now);
        }

        List<ResGioHangNhanVienDTO.KhuyenMaiHoaDonDTO> hoaDonDtos = new ArrayList<>();
        for (KhuyenMaiTheoHoaDon km : hoaDonHopLe) {
            ResGioHangNhanVienDTO.KhuyenMaiHoaDonDTO kmDto = new ResGioHangNhanVienDTO.KhuyenMaiHoaDonDTO();
            kmDto.setId(km.getId());
            kmDto.setTenKhuyenMai(km.getTenKhuyenMai());
            kmDto.setPhanTramGiam(km.getPhanTramGiam());
            kmDto.setGiamToiDa(km.getGiamToiDa());
            kmDto.setHoaDonToiThieu(km.getHoaDonToiThieu());
            hoaDonDtos.add(kmDto);
        }

        List<ResGioHangNhanVienDTO.KhuyenMaiDiemDTO> diemDtos = new ArrayList<>();
        for (KhuyenMaiTheoDiem km : diemHopLe) {
            ResGioHangNhanVienDTO.KhuyenMaiDiemDTO kmDto = new ResGioHangNhanVienDTO.KhuyenMaiDiemDTO();
            kmDto.setId(km.getId());
            kmDto.setTenKhuyenMai(km.getTenKhuyenMai());
            kmDto.setPhanTramGiam(km.getPhanTramGiam());
            kmDto.setGiamToiDa(km.getGiamToiDa());
            kmDto.setHoaDonToiThieu(km.getHoaDonToiThieu());
            kmDto.setDiemToiThieu(km.getDiemToiThieu());
            diemDtos.add(kmDto);
        }

        dto.setKhuyenMaiHoaDonHopLe(hoaDonDtos);
        dto.setKhuyenMaiDiemHopLe(diemDtos);

        int tienGiamHoaDon = 0;
        int tienGiamDiem = 0;

        try {
            tienGiamHoaDon = tinhTienGiamHoaDon(cart.getMaKhuyenMaiHoaDon(), tongTienGoc);
        } catch (IdInvalidException ex) {
            dto.setMaKhuyenMaiHoaDon(null);
        }

        try {
            tienGiamDiem = tinhTienGiamDiem(cart.getMaKhuyenMaiDiem(), cart.getKhachHang(), tongTienGoc);
        } catch (IdInvalidException ex) {
            dto.setMaKhuyenMaiDiem(null);
        }

        dto.setTienGiamHoaDon(tienGiamHoaDon);
        dto.setTienGiamDiem(tienGiamDiem);
        dto.setTongTienGiam(tienGiamHoaDon + tienGiamDiem);
        dto.setTongTienThanhToan(Math.max(tongTienGoc - dto.getTongTienGiam(), 0));

        return dto;
    }

    // === Methods for managing multiple draft carts ===

    public List<ResGioHangNhanVienDTO> getAllDraftCarts() throws IdInvalidException {
        NhanVien nhanVien = getCurrentNhanVien();
        List<GioHangNhanVien> carts = gioHangNhanVienRepository
                .findByNhanVienIdAndTrangThaiOrderByNgayCapNhatDesc(nhanVien.getId(), 0);
        List<ResGioHangNhanVienDTO> dtos = new ArrayList<>();
        for (GioHangNhanVien cart : carts) {
            dtos.add(toResponse(cart));
        }
        return dtos;
    }

    public ResGioHangNhanVienDTO getDraftCartById(Long cartId) throws IdInvalidException {
        NhanVien nhanVien = getCurrentNhanVien();
        GioHangNhanVien cart = gioHangNhanVienRepository
                .findByIdAndNhanVienId(cartId, nhanVien.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy giỏ hàng: " + cartId));

        if (cart.getTrangThai() != null && cart.getTrangThai() != 0) {
            throw new IdInvalidException("Giỏ hàng này đã thanh toán");
        }
        return toResponse(cart);
    }

    @Transactional
    public void xoaDraftCart(Long cartId) throws IdInvalidException {
        NhanVien nhanVien = getCurrentNhanVien();
        GioHangNhanVien cart = gioHangNhanVienRepository
                .findByIdAndNhanVienId(cartId, nhanVien.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy giỏ hàng: " + cartId));

        if (cart.getTrangThai() != null && cart.getTrangThai() != 0) {
            throw new IdInvalidException("Chỉ có thể xóa giỏ hàng chưa thanh toán");
        }

        gioHangNhanVienRepository.delete(cart);
    }

    @Transactional
    public ResGioHangNhanVienDTO createNewDraftCart() throws IdInvalidException {
        NhanVien nhanVien = getCurrentNhanVien();
        GioHangNhanVien cart = new GioHangNhanVien();
        cart.setNhanVien(nhanVien);
        cart.setTrangThai(0);
        cart = gioHangNhanVienRepository.save(cart);
        return toResponse(cart);
    }

    private GioHangNhanVien getCartByIdOrCurrentCart(Long cartId) throws IdInvalidException {
        NhanVien nhanVien = getCurrentNhanVien();

        if (cartId != null) {
            return gioHangNhanVienRepository
                    .findByIdAndNhanVienId(cartId, nhanVien.getId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy giỏ hàng: " + cartId));
        }

        return gioHangNhanVienRepository
                .findFirstByNhanVienIdAndTrangThaiOrderByNgayCapNhatDesc(nhanVien.getId(), 0)
                .orElseGet(() -> {
                    GioHangNhanVien cart = new GioHangNhanVien();
                    cart.setNhanVien(nhanVien);
                    cart.setTrangThai(0);
                    return gioHangNhanVienRepository.save(cart);
                });
    }
}
