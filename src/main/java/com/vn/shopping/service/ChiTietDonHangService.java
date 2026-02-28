package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietDonHang;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.DonHang;
import com.vn.shopping.repository.ChiTietDonHangRepository;

@Service
public class ChiTietDonHangService {

    private final ChiTietDonHangRepository chiTietDonHangRepository;

    public ChiTietDonHangService(ChiTietDonHangRepository chiTietDonHangRepository) {
        this.chiTietDonHangRepository = chiTietDonHangRepository;
    }

    @Transactional
    public ChiTietDonHang create(ChiTietDonHang chiTiet) {
        return chiTietDonHangRepository.save(chiTiet);
    }

    @Transactional
    public ChiTietDonHang create(Long donHangId, Long chiTietSanPhamId,
            Double giaSanPham, Double giamGia, Double giaGiam,
            Integer soLuong, Double thanhTien) {

        ChiTietDonHang ct = new ChiTietDonHang();

        if (donHangId != null) {
            DonHang dh = new DonHang();
            dh.setId(donHangId);
            ct.setDonHang(dh);
        }
        if (chiTietSanPhamId != null) {
            ChiTietSanPham ctsp = new ChiTietSanPham();
            ctsp.setId(chiTietSanPhamId);
            ct.setChiTietSanPham(ctsp);
        }

        ct.setGiaSanPham(giaSanPham);
        ct.setGiamGia(giamGia);
        ct.setGiaGiam(giaGiam);
        ct.setSoLuong(soLuong);
        ct.setThanhTien(thanhTien);

        return chiTietDonHangRepository.save(ct);
    }

    @Transactional
    public ChiTietDonHang update(ChiTietDonHang chiTiet) {
        ChiTietDonHang existing = chiTietDonHangRepository.findById(chiTiet.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết đơn hàng: " + chiTiet.getId()));

        existing.setDonHang(chiTiet.getDonHang());
        existing.setChiTietSanPham(chiTiet.getChiTietSanPham());
        existing.setGiaSanPham(chiTiet.getGiaSanPham());
        existing.setGiamGia(chiTiet.getGiamGia());
        existing.setGiaGiam(chiTiet.getGiaGiam());
        existing.setSoLuong(chiTiet.getSoLuong());
        existing.setThanhTien(chiTiet.getThanhTien());

        return chiTietDonHangRepository.save(existing);
    }

    public void delete(long id) {
        chiTietDonHangRepository.deleteById(id);
    }

    public ChiTietDonHang findById(long id) {
        return chiTietDonHangRepository.findById(id).orElse(null);
    }

    public List<ChiTietDonHang> findAll() {
        return chiTietDonHangRepository.findAll();
    }

    public List<ChiTietDonHang> findByDonHangId(Long donHangId) {
        return chiTietDonHangRepository.findByDonHangId(donHangId);
    }
}
