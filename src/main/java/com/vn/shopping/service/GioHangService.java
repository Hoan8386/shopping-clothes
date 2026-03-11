package com.vn.shopping.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vn.shopping.domain.ChiTietGioHang;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.GioHang;
import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.KhuyenMaiTheoDiem;
import com.vn.shopping.domain.KhuyenMaiTheoHoaDon;
import com.vn.shopping.domain.request.ReqApDungKhuyenMaiDTO;
import com.vn.shopping.domain.request.ReqThemGioHangDTO;
import com.vn.shopping.domain.response.ResApDungKhuyenMaiDTO;
import com.vn.shopping.domain.response.ResGioHangDTO;
import com.vn.shopping.domain.response.ResKhuyenMaiHopLeDTO;
import com.vn.shopping.repository.ChiTietGioHangRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.GioHangRepository;
import com.vn.shopping.repository.KhachHangRepository;
import com.vn.shopping.repository.KhuyenMaiTheoDiemRepository;
import com.vn.shopping.repository.KhuyenMaiTheoHoaDonRepository;
import com.vn.shopping.util.error.IdInvalidException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GioHangService {

    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository;
    private final KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository;

    public GioHangService(GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository,
            KhachHangRepository khachHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            KhuyenMaiTheoHoaDonRepository khuyenMaiTheoHoaDonRepository,
            KhuyenMaiTheoDiemRepository khuyenMaiTheoDiemRepository) {
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.khachHangRepository = khachHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.khuyenMaiTheoHoaDonRepository = khuyenMaiTheoHoaDonRepository;
        this.khuyenMaiTheoDiemRepository = khuyenMaiTheoDiemRepository;
    }

    private KhachHang getCurrentKhachHang() throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));
    }

    public ChiTietGioHang themSanPhamVaoGioHang(ReqThemGioHangDTO req) throws IdInvalidException {
        // Kiểm tra số lượng tối thiểu
        if (req.getSoLuong() == null || req.getSoLuong() < 1) {
            throw new IdInvalidException("Số lượng phải lớn hơn hoặc bằng 1");
        }

        // Lấy khách hàng từ token đang đăng nhập
        KhachHang khachHang = getCurrentKhachHang();

        // Tìm chi tiết sản phẩm
        ChiTietSanPham chiTietSanPham = chiTietSanPhamRepository.findById(req.getMaChiTietSanPham())
                .orElseThrow(
                        () -> new IdInvalidException("Không tìm thấy chi tiết sản phẩm: " + req.getMaChiTietSanPham()));

        // Tìm hoặc tạo giỏ hàng cho khách hàng
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId())
                .orElseGet(() -> {
                    GioHang newGioHang = new GioHang();
                    newGioHang.setKhachHang(khachHang);
                    return gioHangRepository.save(newGioHang);
                });

        // Kiểm tra sản phẩm đã có trong giỏ chưa
        Optional<ChiTietGioHang> existingItem = chiTietGioHangRepository
                .findByGioHangMaGioHangAndChiTietSanPhamId(gioHang.getMaGioHang(), chiTietSanPham.getId());

        if (existingItem.isPresent()) {
            // Nếu đã có -> cộng thêm số lượng
            ChiTietGioHang item = existingItem.get();
            item.setSoLuong(item.getSoLuong() + req.getSoLuong());
            return chiTietGioHangRepository.save(item);
        } else {
            // Nếu chưa có -> tạo mới
            ChiTietGioHang newItem = new ChiTietGioHang();
            newItem.setGioHang(gioHang);
            newItem.setChiTietSanPham(chiTietSanPham);
            newItem.setSoLuong(req.getSoLuong());
            return chiTietGioHangRepository.save(newItem);
        }
    }

    public void xoaChiTietGioHang(Long maChiTietGioHang) throws IdInvalidException {
        ChiTietGioHang item = chiTietGioHangRepository.findById(maChiTietGioHang)
                .orElseThrow(() -> new IdInvalidException(
                        "Không tìm thấy chi tiết giỏ hàng: " + maChiTietGioHang));

        chiTietGioHangRepository.delete(item);
    }

    public ResGioHangDTO getGioHangCuaToi() throws IdInvalidException {
        KhachHang khachHang = getCurrentKhachHang();
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId()).orElse(null);
        if (gioHang == null) {
            return null;
        }

        ResGioHangDTO dto = new ResGioHangDTO();
        dto.setMaGioHang(gioHang.getMaGioHang());

        List<ResGioHangDTO.ChiTietGioHangDTO> chiTietList = new ArrayList<>();
        int tongSoLuong = 0;
        Double tongTien = 0.0;

        if (gioHang.getChiTietGioHangs() != null) {
            for (ChiTietGioHang ct : gioHang.getChiTietGioHangs()) {
                ResGioHangDTO.ChiTietGioHangDTO item = new ResGioHangDTO.ChiTietGioHangDTO();
                item.setMaChiTietGioHang(ct.getMaChiTietGioHang());
                item.setSoLuong(ct.getSoLuong());

                ChiTietSanPham ctsp = ct.getChiTietSanPham();
                if (ctsp != null) {
                    item.setMaChiTietSanPham(ctsp.getId());
                    if (ctsp.getKichThuoc() != null) {
                        item.setKichThuoc(ctsp.getKichThuoc().getTenKichThuoc());
                    }
                    if (ctsp.getMauSac() != null) {
                        item.setMauSac(ctsp.getMauSac().getTenMauSac());
                    }
                    if (ctsp.getSanPham() != null) {
                        item.setTenSanPham(ctsp.getSanPham().getTenSanPham());
                        item.setGiaBan(ctsp.getSanPham().getGiaBan());

                        Double thanhTien = ctsp.getSanPham().getGiaBan() * ct.getSoLuong();
                        item.setThanhTien(thanhTien);
                        tongTien += thanhTien;
                    }
                }

                tongSoLuong += ct.getSoLuong();
                chiTietList.add(item);
            }
        }

        dto.setTongSoLuong(tongSoLuong);
        dto.setTongTien(tongTien);
        dto.setChiTietGioHangs(chiTietList);

        return dto;
    }

    /**
     * Tính tổng tiền giỏ hàng hiện tại (sau giảm giá sản phẩm, trước khuyến mãi).
     */
    private int tinhTongTienGioHang(GioHang gioHang) {
        int tong = 0;
        if (gioHang == null || gioHang.getChiTietGioHangs() == null)
            return tong;
        for (ChiTietGioHang ct : gioHang.getChiTietGioHangs()) {
            ChiTietSanPham ctsp = ct.getChiTietSanPham();
            if (ctsp != null && ctsp.getSanPham() != null) {
                Double giaBan = ctsp.getSanPham().getGiaBan() != null ? ctsp.getSanPham().getGiaBan() : 0;
                Integer giaGiamPhanTram = ctsp.getSanPham().getGiaGiam() != null ? ctsp.getSanPham().getGiaGiam() : 0;
                double giaGiam = giaBan * giaGiamPhanTram / 100;
                tong += (int) ((giaBan - giaGiam) * ct.getSoLuong());
            }
        }
        return tong;
    }

    /**
     * Trả về danh sách khuyến mãi hợp lệ cho giỏ hàng hiện tại của khách hàng.
     * - Khuyến mãi hóa đơn: lọc theo tổng tiền giỏ hàng
     * - Khuyến mãi điểm: lọc theo điểm tích lũy của khách hàng
     */
    public ResKhuyenMaiHopLeDTO getKhuyenMaiHopLe() throws IdInvalidException {
        KhachHang khachHang = getCurrentKhachHang();
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId()).orElse(null);
        int tongTien = tinhTongTienGioHang(gioHang);
        int diemKhachHang = khachHang.getDiemTichLuy() != null ? khachHang.getDiemTichLuy() : 0;
        LocalDateTime now = LocalDateTime.now();

        List<KhuyenMaiTheoHoaDon> khuyenMaiHoaDon = khuyenMaiTheoHoaDonRepository.findKhuyenMaiHopLe(tongTien, now);
        List<KhuyenMaiTheoDiem> khuyenMaiDiem = khuyenMaiTheoDiemRepository.findKhuyenMaiHopLe(diemKhachHang, now);

        return new ResKhuyenMaiHopLeDTO(khuyenMaiHoaDon, khuyenMaiDiem);
    }

    /**
     * Xem trước số tiền được giảm khi áp dụng mã khuyến mãi vào giỏ hàng hiện tại.
     * Không thay đổi dữ liệu, chỉ tính toán và trả về kết quả dự kiến.
     */
    public ResApDungKhuyenMaiDTO xemTruocKhuyenMai(ReqApDungKhuyenMaiDTO req) throws IdInvalidException {
        KhachHang khachHang = getCurrentKhachHang();
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId())
                .orElseThrow(() -> new IdInvalidException("Giỏ hàng trống"));

        int tongTienGoc = tinhTongTienGioHang(gioHang);
        LocalDateTime now = LocalDateTime.now();

        ResApDungKhuyenMaiDTO result = new ResApDungKhuyenMaiDTO();
        result.setTongTienGoc(tongTienGoc);
        int tongTienGiam = 0;

        // --- Khuyến mãi theo hóa đơn ---
        if (req.getMaKhuyenMaiHoaDon() != null) {
            KhuyenMaiTheoHoaDon km = khuyenMaiTheoHoaDonRepository.findById(req.getMaKhuyenMaiHoaDon())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy mã khuyến mãi hóa đơn: " + req.getMaKhuyenMaiHoaDon()));

            if (km.getTrangThai() == null || km.getTrangThai() != 1)
                throw new IdInvalidException("Mã khuyến mãi không còn hoạt động");
            if (km.getThoiGianBatDau() != null && now.isBefore(km.getThoiGianBatDau()))
                throw new IdInvalidException("Mã khuyến mãi chưa đến thời gian áp dụng");
            if (km.getThoiGianKetThuc() != null && now.isAfter(km.getThoiGianKetThuc()))
                throw new IdInvalidException("Mã khuyến mãi đã hết hạn");
            if (km.getSoLuong() != null && km.getSoLuong() <= 0)
                throw new IdInvalidException("Mã khuyến mãi đã hết lượt sử dụng");
            if (km.getHoaDonToiDa() != null && km.getHoaDonToiDa() > 0 && tongTienGoc > km.getHoaDonToiDa())
                throw new IdInvalidException(
                        "Đơn hàng vượt giá trị tối đa (" + km.getHoaDonToiDa() + ") của mã khuyến mãi");

            int tienGiam = 0;
            if (km.getPhanTramGiam() != null && km.getPhanTramGiam() > 0) {
                tienGiam = (int) (tongTienGoc * km.getPhanTramGiam() / 100);
                if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0 && tienGiam > km.getGiamToiDa())
                    tienGiam = km.getGiamToiDa();
            }
            result.setMaKhuyenMaiHoaDon(km.getId());
            result.setTenKhuyenMaiHoaDon(km.getTenKhuyenMai());
            result.setTienGiamHoaDon(tienGiam);
            tongTienGiam += tienGiam;
        }

        // --- Khuyến mãi theo điểm ---
        if (req.getMaKhuyenMaiDiem() != null) {
            KhuyenMaiTheoDiem km = khuyenMaiTheoDiemRepository.findById(req.getMaKhuyenMaiDiem())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy mã khuyến mãi điểm: " + req.getMaKhuyenMaiDiem()));

            if (km.getTrangThai() == null || km.getTrangThai() != 1)
                throw new IdInvalidException("Mã khuyến mãi điểm không còn hoạt động");
            if (km.getThoiGianBatDau() != null && now.isBefore(km.getThoiGianBatDau()))
                throw new IdInvalidException("Mã khuyến mãi điểm chưa đến thời gian áp dụng");
            if (km.getThoiGianKetThuc() != null && now.isAfter(km.getThoiGianKetThuc()))
                throw new IdInvalidException("Mã khuyến mãi điểm đã hết hạn");
            if (km.getSoLuong() != null && km.getSoLuong() <= 0)
                throw new IdInvalidException("Mã khuyến mãi điểm đã hết lượt sử dụng");

            int diemKhachHang = khachHang.getDiemTichLuy() != null ? khachHang.getDiemTichLuy() : 0;
            if (km.getDiemToiThieu() != null && diemKhachHang < km.getDiemToiThieu())
                throw new IdInvalidException(
                        "Điểm tích lũy không đủ (cần tối thiểu " + km.getDiemToiThieu() + " điểm)");
            if (km.getHoaDonToiDa() != null && km.getHoaDonToiDa() > 0 && tongTienGoc > km.getHoaDonToiDa())
                throw new IdInvalidException(
                        "Đơn hàng vượt giá trị tối đa (" + km.getHoaDonToiDa() + ") của mã khuyến mãi điểm");

            int tienGiam = 0;
            if (km.getPhanTramGiam() != null && km.getPhanTramGiam() > 0) {
                tienGiam = (int) (tongTienGoc * km.getPhanTramGiam() / 100);
                if (km.getGiamToiDa() != null && km.getGiamToiDa() > 0 && tienGiam > km.getGiamToiDa())
                    tienGiam = km.getGiamToiDa();
            }
            result.setMaKhuyenMaiDiem(km.getId());
            result.setTenKhuyenMaiDiem(km.getTenKhuyenMai());
            result.setTienGiamDiem(tienGiam);
            tongTienGiam += tienGiam;
        }

        result.setTongTienGiam(tongTienGiam);
        result.setTongTienTra(tongTienGoc - tongTienGiam);
        return result;
    }
}
