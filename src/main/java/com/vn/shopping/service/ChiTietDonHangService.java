package com.vn.shopping.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietDonHang;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.DonHang;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.request.ReqChiTietDonHangDTO;
import com.vn.shopping.domain.response.ResChiTietDonHangDTO;
import com.vn.shopping.repository.ChiTietDonHangRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.DonHangRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class ChiTietDonHangService {

    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final DonHangRepository donHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    public ChiTietDonHangService(ChiTietDonHangRepository chiTietDonHangRepository,
            DonHangRepository donHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository) {
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.donHangRepository = donHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
    }

    @Transactional
    public ChiTietDonHang create(ReqChiTietDonHangDTO req) throws IdInvalidException {
        ChiTietDonHang ct = new ChiTietDonHang();

        if (req.getDonHangId() != null) {
            DonHang dh = donHangRepository.findById(req.getDonHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng: " + req.getDonHangId()));
            ct.setDonHang(dh);
        }
        if (req.getChiTietSanPhamId() != null) {
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(req.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết sản phẩm: " + req.getChiTietSanPhamId()));
            ct.setChiTietSanPham(ctsp);
        }

        ct.setGiaSanPham(req.getGiaSanPham());
        ct.setGiamGia(req.getGiamGia());
        ct.setGiaGiam(req.getGiaGiam());
        ct.setSoLuong(req.getSoLuong());
        ct.setThanhTien(req.getThanhTien());

        return chiTietDonHangRepository.save(ct);
    }

    @Transactional
    public ChiTietDonHang update(ReqChiTietDonHangDTO req) throws IdInvalidException {
        ChiTietDonHang existing = chiTietDonHangRepository.findById(req.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy chi tiết đơn hàng: " + req.getId()));

        if (req.getDonHangId() != null) {
            DonHang dh = donHangRepository.findById(req.getDonHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng: " + req.getDonHangId()));
            existing.setDonHang(dh);
        }
        if (req.getChiTietSanPhamId() != null) {
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(req.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết sản phẩm: " + req.getChiTietSanPhamId()));
            existing.setChiTietSanPham(ctsp);
        }

        existing.setGiaSanPham(req.getGiaSanPham());
        existing.setGiamGia(req.getGiamGia());
        existing.setGiaGiam(req.getGiaGiam());
        existing.setSoLuong(req.getSoLuong());
        existing.setThanhTien(req.getThanhTien());

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

    // ===== Chuyển đổi Entity -> DTO =====

    public ResChiTietDonHangDTO toDTO(ChiTietDonHang ct) {
        ResChiTietDonHangDTO dto = new ResChiTietDonHangDTO();
        dto.setId(ct.getId());
        dto.setGiaSanPham(ct.getGiaSanPham());
        dto.setGiamGia(ct.getGiamGia());
        dto.setGiaGiam(ct.getGiaGiam());
        dto.setSoLuong(ct.getSoLuong());
        dto.setThanhTien(ct.getThanhTien());
        dto.setNgayTao(ct.getNgayTao());
        dto.setNgayCapNhat(ct.getNgayCapNhat());

        // Đơn hàng
        DonHang dh = ct.getDonHang();
        if (dh != null) {
            ResChiTietDonHangDTO.DonHangDTO dhDTO = new ResChiTietDonHangDTO.DonHangDTO();
            dhDTO.setId(dh.getId());
            dhDTO.setDiaChi(dh.getDiaChi());
            dhDTO.setSdt(dh.getSdt());
            dhDTO.setTrangThai(dh.getTrangThai());
            dhDTO.setTongTienTra(dh.getTongTienTra());
            dto.setDonHang(dhDTO);
        }

        // Chi tiết sản phẩm
        ChiTietSanPham ctsp = ct.getChiTietSanPham();
        if (ctsp != null) {
            ResChiTietDonHangDTO.ChiTietSanPhamDTO ctspDTO = new ResChiTietDonHangDTO.ChiTietSanPhamDTO();
            ctspDTO.setId(ctsp.getId());
            ctspDTO.setSoLuong(ctsp.getSoLuong());

            SanPham sp = ctsp.getSanPham();
            if (sp != null) {
                ctspDTO.setTenSanPham(sp.getTenSanPham());
                ctspDTO.setHinhAnhChinh(sp.getHinhAnhChinh());
            }
            if (ctsp.getMauSac() != null) {
                ctspDTO.setTenMauSac(ctsp.getMauSac().getTenMauSac());
            }
            if (ctsp.getKichThuoc() != null) {
                ctspDTO.setTenKichThuoc(ctsp.getKichThuoc().getTenKichThuoc());
            }

            dto.setChiTietSanPham(ctspDTO);
        }

        return dto;
    }

    public List<ResChiTietDonHangDTO> toDTOList(List<ChiTietDonHang> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
