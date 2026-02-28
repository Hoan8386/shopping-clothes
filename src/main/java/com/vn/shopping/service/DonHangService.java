package com.vn.shopping.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.request.ReqTaoDonHangDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.*;
import com.vn.shopping.util.error.IdInvalidException;

import jakarta.persistence.EntityManager;

@Service
public class DonHangService {

    private final DonHangRepository donHangRepository;
    private final EntityManager entityManager;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final GioHangRepository gioHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;

    public DonHangService(DonHangRepository donHangRepository, EntityManager entityManager,
            KhachHangRepository khachHangRepository, NhanVienRepository nhanVienRepository,
            GioHangRepository gioHangRepository, ChiTietDonHangRepository chiTietDonHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository, SanPhamRepository sanPhamRepository) {
        this.donHangRepository = donHangRepository;
        this.entityManager = entityManager;
        this.khachHangRepository = khachHangRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.gioHangRepository = gioHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
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
        // 1. Lấy khách hàng từ token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        // 2. Lấy giỏ hàng
        GioHang gioHang = gioHangRepository.findByKhachHangId(khachHang.getId())
                .orElseThrow(() -> new IdInvalidException("Giỏ hàng trống, không thể tạo đơn hàng"));

        List<ChiTietGioHang> chiTietGioHangs = gioHang.getChiTietGioHangs();
        if (chiTietGioHangs == null || chiTietGioHangs.isEmpty()) {
            throw new IdInvalidException("Giỏ hàng trống, không thể tạo đơn hàng");
        }

        // 3. Tạo đơn hàng
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setDiaChi(req.getDiaChi());
        donHang.setMaKhuyenMaiHoaDon(req.getMaKhuyenMaiHoaDon());
        donHang.setMaKhuyenMaiDiem(req.getMaKhuyenMaiDiem());
        donHang.setHinhThucDonHang(1); // 1 = online
        donHang.setTrangThai(0); // 0 = chờ xác nhận
        donHang.setTrangThaiThanhToan(0); // 0 = chưa thanh toán

        if (req.getCuaHangId() != null) {
            CuaHang ch = new CuaHang();
            ch.setId(req.getCuaHangId());
            donHang.setCuaHang(ch);
        }

        DonHang savedDonHang = donHangRepository.save(donHang);

        // 4. Tạo chi tiết đơn hàng từ giỏ hàng
        int tongTien = 0;
        List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();

        for (ChiTietGioHang ctgh : chiTietGioHangs) {
            ChiTietSanPham ctsp = ctgh.getChiTietSanPham();
            SanPham sp = ctsp.getSanPham();

            Double giaBan = sp.getGiaBan() != null ? sp.getGiaBan() : 0;
            Integer giaGiamPhanTram = sp.getGiaGiam() != null ? sp.getGiaGiam() : 0;
            Double giaGiam = giaBan * giaGiamPhanTram / 100;
            Double giaSauGiam = giaBan - giaGiam;
            Double thanhTien = giaSauGiam * ctgh.getSoLuong();

            ChiTietDonHang ctdh = new ChiTietDonHang();
            ctdh.setDonHang(savedDonHang);
            ctdh.setChiTietSanPham(ctsp);
            ctdh.setGiaSanPham(giaBan);
            ctdh.setGiamGia(giaGiamPhanTram.doubleValue());
            ctdh.setGiaGiam(giaGiam);
            ctdh.setSoLuong(ctgh.getSoLuong());
            ctdh.setThanhTien(thanhTien);

            chiTietDonHangs.add(ctdh);
            tongTien += thanhTien.intValue();
        }

        chiTietDonHangRepository.saveAll(chiTietDonHangs);

        // 5. Cập nhật tổng tiền đơn hàng
        savedDonHang.setTongTien(tongTien);
        savedDonHang.setTienGiam(0);
        savedDonHang.setTongTienGiam(0);
        savedDonHang.setTongTienTra(tongTien);
        donHangRepository.save(savedDonHang);

        // 6. Trừ số lượng sản phẩm
        truSoLuongSanPham(chiTietDonHangs);

        // 7. Xóa giỏ hàng sau khi tạo đơn thành công
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
    public DonHang taoDonHangTaiQuay(DonHang donHang) throws IdInvalidException {
        // Lấy nhân viên từ token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên với email: " + email));

        donHang.setNhanVien(nhanVien);
        donHang.setHinhThucDonHang(0); // 0 = tại quầy

        // Nếu nhân viên thuộc cửa hàng nào thì gán cửa hàng đó
        if (nhanVien.getCuaHang() != null) {
            donHang.setCuaHang(nhanVien.getCuaHang());
        }

        DonHang saved = save(donHang);

        // Trừ số lượng sản phẩm từ chi tiết đơn hàng
        if (saved.getChiTietDonHangs() != null && !saved.getChiTietDonHangs().isEmpty()) {
            truSoLuongSanPham(saved.getChiTietDonHangs());
        }

        return saved;
    }

    @Transactional
    public DonHang update(DonHang donHang) {
        DonHang existing = donHangRepository.findById(donHang.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + donHang.getId()));

        existing.setCuaHang(donHang.getCuaHang());
        existing.setKhachHang(donHang.getKhachHang());
        existing.setNhanVien(donHang.getNhanVien());
        existing.setMaKhuyenMaiHoaDon(donHang.getMaKhuyenMaiHoaDon());
        existing.setMaKhuyenMaiDiem(donHang.getMaKhuyenMaiDiem());
        existing.setDiaChi(donHang.getDiaChi());
        existing.setTongTien(donHang.getTongTien());
        existing.setTienGiam(donHang.getTienGiam());
        existing.setTongTienGiam(donHang.getTongTienGiam());
        existing.setTongTienTra(donHang.getTongTienTra());
        existing.setTrangThai(donHang.getTrangThai());
        existing.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        existing.setHinhThucDonHang(donHang.getHinhThucDonHang());

        DonHang saved = donHangRepository.save(existing);
        entityManager.flush();
        entityManager.clear();
        return donHangRepository.findById(saved.getId()).orElse(saved);
    }

    public void delete(long id) {
        donHangRepository.deleteById(id);
    }

    public DonHang findById(long id) {
        return donHangRepository.findById(id).orElse(null);
    }

    public List<DonHang> findAll() {
        return donHangRepository.findAll();
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

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(page.getContent());
        return result;
    }

    /**
     * Lấy đơn hàng theo id.
     * - KHACH_HANG chỉ được xem đơn hàng của chính mình
     * - NHAN_VIEN / ADMIN xem được tất cả
     */
    @Transactional(readOnly = true)
    public DonHang findByIdForCurrentUser(long id) throws IdInvalidException {
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

        return donHang;
    }
}
