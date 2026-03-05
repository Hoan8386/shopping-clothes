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
import com.vn.shopping.domain.request.ReqCapNhatDonHangDTO;
import com.vn.shopping.domain.request.ReqTaoDonHangDTO;
import com.vn.shopping.domain.response.ResDonHangDTO;
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
    private final CuaHangRepository cuaHangRepository;

    public DonHangService(DonHangRepository donHangRepository, EntityManager entityManager,
            KhachHangRepository khachHangRepository, NhanVienRepository nhanVienRepository,
            GioHangRepository gioHangRepository, ChiTietDonHangRepository chiTietDonHangRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository, SanPhamRepository sanPhamRepository,
            CuaHangRepository cuaHangRepository) {
        this.donHangRepository = donHangRepository;
        this.entityManager = entityManager;
        this.khachHangRepository = khachHangRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.gioHangRepository = gioHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.cuaHangRepository = cuaHangRepository;
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
        donHang.setSdt(req.getSdt());
        donHang.setMaKhuyenMaiHoaDon(req.getMaKhuyenMaiHoaDon());
        donHang.setMaKhuyenMaiDiem(req.getMaKhuyenMaiDiem());
        donHang.setHinhThucDonHang(1); // 1 = online
        donHang.setTrangThai(0); // 0 = chờ xác nhận
        donHang.setTrangThaiThanhToan(0); // 0 = chưa thanh toán

        if (req.getCuaHangId() != null) {
            CuaHang ch = cuaHangRepository.findById(req.getCuaHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng: " + req.getCuaHangId()));
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

        // Cập nhật trạng thái
        if (req.getTrangThai() != null) {
            Integer trangThaiMoi = req.getTrangThai();
            validateChuyenTrangThai(trangThaiCu, trangThaiMoi);
            existing.setTrangThai(trangThaiMoi);

            // Nếu đơn hàng online (1) và nhân viên cập nhật trạng thái → gán nhân viên
            if (existing.getHinhThucDonHang() != null && existing.getHinhThucDonHang() == 1
                    && (trangThaiMoi == 1 || trangThaiMoi == 2 || trangThaiMoi == 3)) {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                NhanVien nv = nhanVienRepository.findByEmail(email).orElse(null);
                if (nv != null) {
                    existing.setNhanVien(nv);
                }
            }

            // Cộng điểm tích lũy khi khách hàng xác nhận đã nhận hàng (5)
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
        dto.setTongTien(donHang.getTongTien());
        dto.setTienGiam(donHang.getTienGiam());
        dto.setTongTienGiam(donHang.getTongTienGiam());
        dto.setTongTienTra(donHang.getTongTienTra());
        dto.setTrangThai(donHang.getTrangThai());
        dto.setTrangThaiThanhToan(donHang.getTrangThaiThanhToan());
        dto.setHinhThucDonHang(donHang.getHinhThucDonHang());
        dto.setMaKhuyenMaiHoaDon(donHang.getMaKhuyenMaiHoaDon());
        dto.setMaKhuyenMaiDiem(donHang.getMaKhuyenMaiDiem());
        dto.setNgayTao(donHang.getNgayTao());
        dto.setNgayCapNhat(donHang.getNgayCapNhat());

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
}
