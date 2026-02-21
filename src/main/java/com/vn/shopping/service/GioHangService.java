package com.vn.shopping.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietGioHang;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.GioHang;
import com.vn.shopping.domain.KhachHang;
import com.vn.shopping.repository.ChiTietGioHangRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.GioHangRepository;

@Service
public class GioHangService {

    private final GioHangRepository gioHangRepository;
    private final ChiTietGioHangRepository chiTietGioHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    public GioHangService(GioHangRepository gioHangRepository,
            ChiTietGioHangRepository chiTietGioHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.gioHangRepository = gioHangRepository;
        this.chiTietGioHangRepository = chiTietGioHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    /**
     * Lấy hoặc tạo giỏ hàng cho khách hàng
     */
    public GioHang getOrCreateGioHang(KhachHang khachHang) {
        return gioHangRepository.findByKhachHang(khachHang)
                .orElseGet(() -> {
                    GioHang gioHang = new GioHang();
                    gioHang.setKhachHang(khachHang);
                    return gioHangRepository.save(gioHang);
                });
    }

    /**
     * Xem giỏ hàng
     */
    public List<ChiTietGioHang> getChiTietGioHang(GioHang gioHang) {
        return chiTietGioHangRepository.findByGioHang(gioHang);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng (nếu đã có thì cộng dồn số lượng)
     */
    @Transactional
    public ChiTietGioHang addToCart(KhachHang khachHang, String maChiTietSanPham, int soLuong) {
        GioHang gioHang = getOrCreateGioHang(khachHang);

        ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(maChiTietSanPham)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm: " + maChiTietSanPham));

        Optional<ChiTietGioHang> existing = chiTietGioHangRepository
                .findByGioHangAndChiTietSanPham(gioHang, ctsp);

        if (existing.isPresent()) {
            ChiTietGioHang item = existing.get();
            item.setSoLuong(item.getSoLuong() + soLuong);
            return chiTietGioHangRepository.save(item);
        } else {
            ChiTietGioHang item = new ChiTietGioHang();
            item.setGioHang(gioHang);
            item.setChiTietSanPham(ctsp);
            item.setSoLuong(soLuong);
            return chiTietGioHangRepository.save(item);
        }
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    @Transactional
    public ChiTietGioHang updateCartItem(KhachHang khachHang, String maChiTietSanPham, int soLuong) {
        GioHang gioHang = getOrCreateGioHang(khachHang);

        ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(maChiTietSanPham)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm: " + maChiTietSanPham));

        ChiTietGioHang item = chiTietGioHangRepository
                .findByGioHangAndChiTietSanPham(gioHang, ctsp)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ hàng"));

        if (soLuong <= 0) {
            chiTietGioHangRepository.delete(item);
            return null;
        }

        item.setSoLuong(soLuong);
        return chiTietGioHangRepository.save(item);
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    @Transactional
    public void removeFromCart(KhachHang khachHang, String maChiTietSanPham) {
        GioHang gioHang = getOrCreateGioHang(khachHang);

        ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(maChiTietSanPham)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết sản phẩm: " + maChiTietSanPham));

        chiTietGioHangRepository.findByGioHangAndChiTietSanPham(gioHang, ctsp)
                .ifPresent(chiTietGioHangRepository::delete);
    }
}
