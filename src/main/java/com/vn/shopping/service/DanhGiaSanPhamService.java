package com.vn.shopping.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.request.ReqDanhGiaSanPhamDTO;
import com.vn.shopping.domain.response.ResDanhGiaSanPhamDTO;
import com.vn.shopping.repository.*;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class DanhGiaSanPhamService {

    private final DanhGiaSanPhamRepository danhGiaSanPhamRepository;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final MinioStorageService minioStorageService;

    public DanhGiaSanPhamService(DanhGiaSanPhamRepository danhGiaSanPhamRepository,
            KhachHangRepository khachHangRepository,
            ChiTietDonHangRepository chiTietDonHangRepository,
            MinioStorageService minioStorageService) {
        this.danhGiaSanPhamRepository = danhGiaSanPhamRepository;
        this.khachHangRepository = khachHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.minioStorageService = minioStorageService;
    }

    /**
     * Khách hàng tạo đánh giá sản phẩm.
     * Điều kiện:
     * - Phải đăng nhập
     * - Đơn hàng phải ở trạng thái 5 (Đã nhận hàng)
     * - Đơn hàng phải thuộc về khách hàng
     * - Mỗi chi tiết đơn hàng chỉ được đánh giá 1 lần duy nhất
     */
    @Transactional
    public DanhGiaSanPham create(ReqDanhGiaSanPhamDTO req, MultipartFile file) throws IdInvalidException {
        // 1. Lấy khách hàng từ token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        // 2. Kiểm tra chi tiết đơn hàng tồn tại
        ChiTietDonHang chiTietDonHang = chiTietDonHangRepository.findById(req.getChiTietDonHangId())
                .orElseThrow(
                        () -> new IdInvalidException("Không tìm thấy chi tiết đơn hàng: " + req.getChiTietDonHangId()));

        // 3. Lấy đơn hàng và kiểm tra quyền sở hữu
        DonHang donHang = chiTietDonHang.getDonHang();
        if (donHang == null || donHang.getKhachHang() == null
                || !donHang.getKhachHang().getId().equals(khachHang.getId())) {
            throw new IdInvalidException("Đơn hàng không thuộc về bạn");
        }

        // 4. Kiểm tra trạng thái đơn hàng = 5 (Đã nhận hàng)
        if (donHang.getTrangThai() == null || donHang.getTrangThai() != 5) {
            throw new IdInvalidException("Đơn hàng phải ở trạng thái đã nhận hàng mới có thể đánh giá");
        }

        // 5. Kiểm tra chưa đánh giá chi tiết sản phẩm này (mỗi chi tiết đơn chỉ đánh
        // giá 1 lần)
        if (danhGiaSanPhamRepository.existsByKhachHangIdAndChiTietDonHangId(
                khachHang.getId(), chiTietDonHang.getId())) {
            throw new IdInvalidException("Bạn đã đánh giá sản phẩm này rồi");
        }

        // 6. Validate số sao
        if (req.getSoSao() == null || req.getSoSao() < 1 || req.getSoSao() > 5) {
            throw new IdInvalidException("Số sao phải từ 1 đến 5");
        }

        // 7. Upload ảnh lên MinIO (nếu có)
        String hinhAnhUrl = null;
        if (file != null && !file.isEmpty()) {
            hinhAnhUrl = minioStorageService.uploadSingleFile(file);
        }

        // 8. Tạo đánh giá
        DanhGiaSanPham danhGia = new DanhGiaSanPham();
        danhGia.setKhachHang(khachHang);
        danhGia.setChiTietDonHang(chiTietDonHang);
        danhGia.setSoSao(req.getSoSao());
        danhGia.setGhiTru(req.getGhiTru());
        danhGia.setHinhAnh(hinhAnhUrl);

        return danhGiaSanPhamRepository.save(danhGia);
    }

    @Transactional
    public DanhGiaSanPham update(Long id, ReqDanhGiaSanPhamDTO req, MultipartFile file) throws IdInvalidException {
        DanhGiaSanPham existing = danhGiaSanPhamRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đánh giá: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        if (!existing.getKhachHang().getId().equals(khachHang.getId())) {
            throw new IdInvalidException("Bạn không có quyền sửa đánh giá này");
        }

        if (req.getSoSao() != null) {
            if (req.getSoSao() < 1 || req.getSoSao() > 5) {
                throw new IdInvalidException("Số sao phải từ 1 đến 5");
            }
            existing.setSoSao(req.getSoSao());
        }

        if (req.getGhiTru() != null) {
            existing.setGhiTru(req.getGhiTru());
        }

        if (file != null && !file.isEmpty()) {
            String hinhAnhUrl = minioStorageService.uploadSingleFile(file);
            existing.setHinhAnh(hinhAnhUrl);
        }

        return danhGiaSanPhamRepository.save(existing);
    }

    public void delete(long id) throws IdInvalidException {
        DanhGiaSanPham danhGia = danhGiaSanPhamRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đánh giá: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<KhachHang> khOpt = khachHangRepository.findByEmail(email);
        if (khOpt.isPresent()) {
            if (!danhGia.getKhachHang().getId().equals(khOpt.get().getId())) {
                throw new IdInvalidException("Bạn không có quyền xóa đánh giá này");
            }
        }

        danhGiaSanPhamRepository.deleteById(id);
    }

    public DanhGiaSanPham findById(long id) {
        return danhGiaSanPhamRepository.findById(id).orElse(null);
    }

    public List<DanhGiaSanPham> findAll() {
        return danhGiaSanPhamRepository.findAll();
    }

    public List<DanhGiaSanPham> findByChiTietDonHangId(Long chiTietDonHangId) {
        return danhGiaSanPhamRepository.findByChiTietDonHangId(chiTietDonHangId);
    }

    public List<DanhGiaSanPham> findBySanPhamId(Long sanPhamId) {
        return danhGiaSanPhamRepository.findBySanPhamId(sanPhamId);
    }

    @Transactional(readOnly = true)
    public List<ResDanhGiaSanPhamDTO> findByCurrentKhachHang() throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        return danhGiaSanPhamRepository.findByKhachHangId(khachHang.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ResDanhGiaSanPhamDTO convertToDTO(DanhGiaSanPham dg) {
        ResDanhGiaSanPhamDTO dto = new ResDanhGiaSanPhamDTO();
        dto.setId(dg.getId());
        dto.setSoSao(dg.getSoSao());
        dto.setGhiTru(dg.getGhiTru());
        dto.setHinhAnh(dg.getHinhAnh());
        dto.setNgayTao(dg.getNgayTao());
        dto.setNgayCapNhat(dg.getNgayCapNhat());

        if (dg.getKhachHang() != null) {
            dto.setKhachHangId(dg.getKhachHang().getId());
            dto.setTenKhachHang(dg.getKhachHang().getTenKhachHang());
        }

        if (dg.getChiTietDonHang() != null) {
            dto.setChiTietDonHangId(dg.getChiTietDonHang().getId());
            if (dg.getChiTietDonHang().getDonHang() != null) {
                dto.setDonHangId(dg.getChiTietDonHang().getDonHang().getId());
            }
            if (dg.getChiTietDonHang().getChiTietSanPham() != null
                    && dg.getChiTietDonHang().getChiTietSanPham().getSanPham() != null) {
                SanPham sp = dg.getChiTietDonHang().getChiTietSanPham().getSanPham();
                dto.setSanPhamId(sp.getId());
                dto.setTenSanPham(sp.getTenSanPham());
            }
        }

        return dto;
    }
}
