package com.vn.shopping.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vn.shopping.domain.ChiTietGioHang;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.GioHang;
import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.domain.request.ReqThemGioHangDTO;
import com.vn.shopping.domain.response.ResGioHangDTO;
import com.vn.shopping.repository.ChiTietGioHangRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.GioHangRepository;
import com.vn.shopping.repository.KhachHangRepository;
import com.vn.shopping.util.error.IdInvalidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GioHangService {

    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    public GioHangService(GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository,
            KhachHangRepository khachHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.khachHangRepository = khachHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
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
}
