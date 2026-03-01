package com.vn.shopping.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.request.ReqDanhGiaSanPhamDTO;
import com.vn.shopping.domain.response.ResDanhGiaSanPhamDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.*;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class DanhGiaSanPhamService {

    private final DanhGiaSanPhamRepository danhGiaSanPhamRepository;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamRepository sanPhamRepository;
    private final DonHangRepository donHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final MinioStorageService minioStorageService;

    public DanhGiaSanPhamService(DanhGiaSanPhamRepository danhGiaSanPhamRepository,
            KhachHangRepository khachHangRepository,
            SanPhamRepository sanPhamRepository,
            DonHangRepository donHangRepository,
            ChiTietDonHangRepository chiTietDonHangRepository,
            MinioStorageService minioStorageService) {
        this.danhGiaSanPhamRepository = danhGiaSanPhamRepository;
        this.khachHangRepository = khachHangRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.donHangRepository = donHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.minioStorageService = minioStorageService;
    }

    /**
     * Khách hàng tạo đánh giá sản phẩm.
     * Điều kiện:
     * - Phải đăng nhập (lấy khách hàng từ token)
     * - Đơn hàng phải thuộc về khách hàng
     * - Đơn hàng phải có trạng thái = 3 (Thành công)
     * - Sản phẩm phải tồn tại trong chi tiết đơn hàng đó
     * - Chưa đánh giá sản phẩm này trong đơn hàng này
     */
    @Transactional
    public DanhGiaSanPham create(ReqDanhGiaSanPhamDTO req, MultipartFile file) throws IdInvalidException {
        // 1. Lấy khách hàng từ token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        // 2. Kiểm tra sản phẩm tồn tại
        SanPham sanPham = sanPhamRepository.findById(req.getSanPhamId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy sản phẩm: " + req.getSanPhamId()));

        // 3. Kiểm tra đơn hàng tồn tại và thuộc về khách hàng
        DonHang donHang = donHangRepository.findById(req.getDonHangId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn hàng: " + req.getDonHangId()));

        if (donHang.getKhachHang() == null || !donHang.getKhachHang().getId().equals(khachHang.getId())) {
            throw new IdInvalidException("Đơn hàng không thuộc về bạn");
        }

        // 4. Kiểm tra trạng thái đơn hàng = 3 (Thành công)
        if (donHang.getTrangThai() == null || donHang.getTrangThai() != 3) {
            throw new IdInvalidException("Chỉ được đánh giá khi đơn hàng đã giao thành công (trạng thái = 3)");
        }

        // 5. Kiểm tra sản phẩm có trong đơn hàng
        List<ChiTietDonHang> chiTietDonHangs = chiTietDonHangRepository.findByDonHangId(donHang.getId());
        boolean sanPhamTrongDon = chiTietDonHangs.stream()
                .anyMatch(ct -> ct.getChiTietSanPham() != null
                        && ct.getChiTietSanPham().getSanPham() != null
                        && ct.getChiTietSanPham().getSanPham().getId().equals(sanPham.getId()));

        if (!sanPhamTrongDon) {
            throw new IdInvalidException("Sản phẩm này không có trong đơn hàng #" + donHang.getId());
        }

        // 6. Kiểm tra chưa đánh giá sản phẩm trong đơn này
        boolean daDanhGia = danhGiaSanPhamRepository
                .existsByKhachHangIdAndSanPhamIdAndDonHangId(khachHang.getId(), sanPham.getId(), donHang.getId());
        if (daDanhGia) {
            throw new IdInvalidException("Bạn đã đánh giá sản phẩm này trong đơn hàng #" + donHang.getId());
        }

        // 7. Validate số sao
        if (req.getSoSao() == null || req.getSoSao() < 1 || req.getSoSao() > 5) {
            throw new IdInvalidException("Số sao phải từ 1 đến 5");
        }

        // 8. Upload ảnh lên MinIO (nếu có)
        String hinhAnhUrl = null;
        if (file != null && !file.isEmpty()) {
            hinhAnhUrl = minioStorageService.uploadSingleFile(file);
        }

        // 9. Tạo đánh giá
        DanhGiaSanPham danhGia = new DanhGiaSanPham();
        danhGia.setKhachHang(khachHang);
        danhGia.setSanPham(sanPham);
        danhGia.setDonHang(donHang);
        danhGia.setSoSao(req.getSoSao());
        danhGia.setGhiChu(req.getGhiChu());
        danhGia.setHinhAnh(hinhAnhUrl);

        return danhGiaSanPhamRepository.save(danhGia);
    }

    /**
     * Cập nhật đánh giá — chỉ chủ đánh giá mới được sửa
     */
    @Transactional
    public DanhGiaSanPham update(Long id, ReqDanhGiaSanPhamDTO req, MultipartFile file) throws IdInvalidException {
        DanhGiaSanPham existing = danhGiaSanPhamRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đánh giá: " + id));

        // Kiểm tra quyền sở hữu
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        if (!existing.getKhachHang().getId().equals(khachHang.getId())) {
            throw new IdInvalidException("Bạn không có quyền sửa đánh giá này");
        }

        // Validate số sao
        if (req.getSoSao() != null) {
            if (req.getSoSao() < 1 || req.getSoSao() > 5) {
                throw new IdInvalidException("Số sao phải từ 1 đến 5");
            }
            existing.setSoSao(req.getSoSao());
        }

        if (req.getGhiChu() != null) {
            existing.setGhiChu(req.getGhiChu());
        }

        // Upload ảnh mới nếu có
        if (file != null && !file.isEmpty()) {
            String hinhAnhUrl = minioStorageService.uploadSingleFile(file);
            existing.setHinhAnh(hinhAnhUrl);
        }

        return danhGiaSanPhamRepository.save(existing);
    }

    /**
     * Xóa đánh giá — chủ đánh giá hoặc Admin
     */
    public void delete(long id) throws IdInvalidException {
        DanhGiaSanPham danhGia = danhGiaSanPhamRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đánh giá: " + id));

        // Kiểm tra quyền: chủ đánh giá hoặc admin/nhân viên
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<KhachHang> khOpt = khachHangRepository.findByEmail(email);
        if (khOpt.isPresent()) {
            // Là khách hàng → chỉ được xóa đánh giá của mình
            if (!danhGia.getKhachHang().getId().equals(khOpt.get().getId())) {
                throw new IdInvalidException("Bạn không có quyền xóa đánh giá này");
            }
        }
        // Nếu là nhân viên/admin thì có quyền xóa

        danhGiaSanPhamRepository.deleteById(id);
    }

    public DanhGiaSanPham findById(long id) {
        return danhGiaSanPhamRepository.findById(id).orElse(null);
    }

    public List<DanhGiaSanPham> findAll() {
        return danhGiaSanPhamRepository.findAll();
    }

    public List<DanhGiaSanPham> findBySanPhamId(Long sanPhamId) {
        return danhGiaSanPhamRepository.findBySanPhamId(sanPhamId);
    }

    public List<DanhGiaSanPham> findByDonHangId(Long donHangId) {
        return danhGiaSanPhamRepository.findByDonHangId(donHangId);
    }

    /**
     * Lấy đánh giá theo sản phẩm + phân trang
     */
    @Transactional(readOnly = true)
    public ResultPaginationDTO findBySanPhamIdPaginated(Long sanPhamId, Pageable pageable) {
        Page<DanhGiaSanPham> page = danhGiaSanPhamRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("sanPham").get("id"), sanPhamId), pageable);

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        List<ResDanhGiaSanPhamDTO> dtos = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(dtos);
        return result;
    }

    /**
     * Lấy đánh giá của khách hàng đang đăng nhập
     */
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
        dto.setGhiChu(dg.getGhiChu());
        dto.setHinhAnh(dg.getHinhAnh());
        dto.setNgayTao(dg.getNgayTao());
        dto.setNgayCapNhat(dg.getNgayCapNhat());

        if (dg.getKhachHang() != null) {
            dto.setKhachHangId(dg.getKhachHang().getId());
            dto.setTenKhachHang(dg.getKhachHang().getTenKhachHang());
        }
        if (dg.getSanPham() != null) {
            dto.setSanPhamId(dg.getSanPham().getId());
            dto.setTenSanPham(dg.getSanPham().getTenSanPham());
        }
        if (dg.getDonHang() != null) {
            dto.setDonHangId(dg.getDonHang().getId());
        }
        return dto;
    }
}
