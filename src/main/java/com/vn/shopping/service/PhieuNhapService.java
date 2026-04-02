package com.vn.shopping.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietPhieuNhap;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.domain.NhaCungCap;
import com.vn.shopping.domain.PhieuNhap;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.request.ReqPhieuNhapDTO;
import com.vn.shopping.domain.response.ResInventorySuggestionDTO;
import com.vn.shopping.domain.response.ResPhieuNhapDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.ChiTietPhieuNhapRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.CuaHangRepository;
import com.vn.shopping.repository.NhaCungCapRepository;
import com.vn.shopping.repository.NhanVienRepository;
import com.vn.shopping.repository.PhieuNhapRepository;
import com.vn.shopping.repository.SanPhamRepository;
import com.vn.shopping.util.SecurityUtil;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class PhieuNhapService {

    private static final int TRANG_THAI_DA_DAT = 0;
    private static final int TRANG_THAI_DA_NHAN = 1;
    private static final int TRANG_THAI_CHAM_GIAO = 2;
    private static final int TRANG_THAI_HUY = 3;
    private static final int TRANG_THAI_THIEU_HANG = 4;
    private static final int TRANG_THAI_HOAN_THANH = 5;
    private static final int DEFAULT_NEAR_OUT_THRESHOLD = 10;

    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;
    private final CuaHangRepository cuaHangRepository;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final NhanVienRepository nhanVienRepository;

    public PhieuNhapService(PhieuNhapRepository phieuNhapRepository,
            ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            SanPhamRepository sanPhamRepository,
            CuaHangRepository cuaHangRepository,
            NhaCungCapRepository nhaCungCapRepository,
            NhanVienRepository nhanVienRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.cuaHangRepository = cuaHangRepository;
        this.nhaCungCapRepository = nhaCungCapRepository;
        this.nhanVienRepository = nhanVienRepository;
    }

    public PhieuNhap create(ReqPhieuNhapDTO dto) throws IdInvalidException {
        PhieuNhap pn = new PhieuNhap();
        pn.setTenPhieuNhap(dto.getTenPhieuNhap());
        pn.setTrangThai(TRANG_THAI_DA_DAT);
        pn.setNgayDatHang(LocalDateTime.now());

        if (dto.getCuaHangId() != null) {
            CuaHang ch = cuaHangRepository.findById(dto.getCuaHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng: " + dto.getCuaHangId()));
            pn.setCuaHang(ch);
        }
        if (dto.getNhaCungCapId() != null) {
            NhaCungCap ncc = nhaCungCapRepository.findById(dto.getNhaCungCapId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhà cung cấp: " + dto.getNhaCungCapId()));
            pn.setNhaCungCap(ncc);
        }

        return phieuNhapRepository.save(pn);
    }

    public ResInventorySuggestionDTO getInventorySuggestions(
            String status,
            Integer nearOutThreshold,
            Long cuaHangId) {

        int threshold = nearOutThreshold == null || nearOutThreshold < 0
                ? DEFAULT_NEAR_OUT_THRESHOLD
                : nearOutThreshold;
        InventoryStockStatus targetStatus = InventoryStockStatus.from(status);

        Map<Long, String> tenCuaHangMap = new HashMap<>();
        cuaHangRepository.findAll().forEach(store -> tenCuaHangMap.put(store.getId(), store.getTenCuaHang()));

        List<ResInventorySuggestionDTO.Item> items = chiTietSanPhamRepository.findAll().stream()
                .filter(detail -> cuaHangId == null || Objects.equals(detail.getMaCuaHang(), cuaHangId))
                .filter(detail -> getInventoryStatus(detail.getSoLuong(), threshold) == targetStatus)
                .sorted(Comparator
                        .comparing((ChiTietSanPham detail) -> Optional.ofNullable(detail.getSoLuong()).orElse(0))
                        .thenComparing(detail -> detail.getId() == null ? Long.MAX_VALUE : detail.getId()))
                .map(detail -> {
                    SanPham sanPham = detail.getSanPham();
                    Long maCuaHang = detail.getMaCuaHang();
                    return new ResInventorySuggestionDTO.Item(
                            detail.getId(),
                            sanPham != null ? sanPham.getId() : null,
                            sanPham != null ? sanPham.getTenSanPham() : null,
                            detail.getMauSac() != null ? detail.getMauSac().getTenMauSac() : null,
                            detail.getKichThuoc() != null ? detail.getKichThuoc().getTenKichThuoc() : null,
                            maCuaHang,
                            maCuaHang != null ? tenCuaHangMap.get(maCuaHang) : null,
                            Optional.ofNullable(detail.getSoLuong()).orElse(0),
                            getInventoryStatus(detail.getSoLuong(), threshold).name());
                })
                .collect(Collectors.toList());

        return new ResInventorySuggestionDTO(
                targetStatus.name(),
                threshold,
                cuaHangId,
                items);
    }

    private InventoryStockStatus getInventoryStatus(Integer quantity, int threshold) {
        int currentQty = Optional.ofNullable(quantity).orElse(0);
        if (currentQty <= 0) {
            return InventoryStockStatus.DA_HET;
        }
        if (currentQty <= threshold) {
            return InventoryStockStatus.SAP_HET;
        }
        return InventoryStockStatus.CON_HANG;
    }

    private enum InventoryStockStatus {
        CON_HANG,
        SAP_HET,
        DA_HET;

        public static InventoryStockStatus from(String rawStatus) {
            if (rawStatus == null || rawStatus.isBlank()) {
                return SAP_HET;
            }

            String normalized = rawStatus.trim().toUpperCase(Locale.ROOT)
                    .replace('-', '_')
                    .replace(' ', '_');

            if ("GAN_HET".equals(normalized) || "NEAR_OUT".equals(normalized)
                    || "LOW_STOCK".equals(normalized)) {
                normalized = "SAP_HET";
            }
            if ("HET_HANG".equals(normalized) || "OUT_OF_STOCK".equals(normalized)) {
                normalized = "DA_HET";
            }
            if ("IN_STOCK".equals(normalized)) {
                normalized = "CON_HANG";
            }

            try {
                return InventoryStockStatus.valueOf(normalized);
            } catch (IllegalArgumentException ex) {
                return SAP_HET;
            }
        }
    }

    @Transactional
    public PhieuNhap update(ReqPhieuNhapDTO dto) throws IdInvalidException {
        PhieuNhap existing = phieuNhapRepository.findById(dto.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu nhập: " + dto.getId()));

        Integer oldTrangThai = existing.getTrangThai();
        Integer newTrangThai = dto.getTrangThai();
        boolean allowCompleteFromThieu = oldTrangThai != null
                && oldTrangThai == TRANG_THAI_THIEU_HANG
                && newTrangThai != null
                && newTrangThai == TRANG_THAI_HOAN_THANH;

        // Không cho phép cập nhật đơn đã hủy
        if (oldTrangThai != null && oldTrangThai == TRANG_THAI_HUY) {
            throw new IdInvalidException("Phiếu nhập đã hủy, không thể cập nhật");
        }

        // Không cho phép cập nhật phiếu đã hoàn thành
        if (oldTrangThai != null && oldTrangThai == TRANG_THAI_HOAN_THANH) {
            throw new IdInvalidException("Phiếu nhập đã hoàn thành, không thể cập nhật");
        }

        // Khi phiếu đang ở trạng thái đã nhận hoặc thiếu hàng, không cho đổi trạng thái
        // thủ công
        if (oldTrangThai != null
                && (oldTrangThai == TRANG_THAI_DA_NHAN || oldTrangThai == TRANG_THAI_THIEU_HANG)
                && newTrangThai != null && newTrangThai != oldTrangThai
                && !allowCompleteFromThieu) {
            throw new IdInvalidException(
                    "Không thể thay đổi trạng thái thủ công khi phiếu đã nhận hàng. Hãy dùng chức năng kiểm kê.");
        }

        if (allowCompleteFromThieu) {
            if (!isCurrentUserAdmin()) {
                throw new IdInvalidException("Chỉ ADMIN mới có quyền hoàn thành phiếu nhập thiếu hàng");
            }
            // Đồng bộ lại số lượng tồn kho theo số lượng thực nhập (trừ thiếu) trước khi
            // hoàn thành.
            capNhatSoLuongSauNhapHang(existing.getId());
        }

        // Validate trạng thái hợp lệ (0, 1, 2, 3, 4, 5)
        if (newTrangThai != null && (newTrangThai < 0 || newTrangThai > 5)) {
            throw new IdInvalidException(
                    "Trạng thái không hợp lệ. 0: Đã đặt, 1: Đã nhận, 2: Chậm giao, 3: Hủy, 4: Thiếu hàng, 5: Hoàn thành");
        }

        existing.setTenPhieuNhap(dto.getTenPhieuNhap());
        existing.setTrangThai(newTrangThai);

        if (dto.getCuaHangId() != null) {
            CuaHang ch = cuaHangRepository.findById(dto.getCuaHangId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng: " + dto.getCuaHangId()));
            existing.setCuaHang(ch);
        }
        if (dto.getNhaCungCapId() != null) {
            NhaCungCap ncc = nhaCungCapRepository.findById(dto.getNhaCungCapId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhà cung cấp: " + dto.getNhaCungCapId()));
            existing.setNhaCungCap(ncc);
        }

        PhieuNhap saved = phieuNhapRepository.save(existing);

        // Khi trạng thái chuyển sang "đã nhận" (1) => chỉ đánh dấu nhận hàng, chưa kiểm
        // kê
        boolean isNhanHang = (newTrangThai != null
                && newTrangThai == TRANG_THAI_DA_NHAN
                && (oldTrangThai == null || oldTrangThai != TRANG_THAI_DA_NHAN));

        if (isNhanHang) {
            existing.setNgayNhanHang(LocalDateTime.now());
            phieuNhapRepository.save(existing);
        }

        return saved;
    }

    private boolean isCurrentUserAdmin() {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (email.isBlank()) {
            return false;
        }
        return nhanVienRepository.findByEmail(email)
                .map(nv -> nv.getRole() != null
                        && nv.getRole().getName() != null
                        && "ADMIN".equalsIgnoreCase(nv.getRole().getName().trim()))
                .orElse(false);
    }

    /**
     * Kiểm kê phiếu nhập: chỉ gọi khi phiếu đang ở trạng thái "Đã nhận" (1).
     * - Duyệt từng chi tiết phiếu nhập: trangThai 0 = đủ, 1 = thiếu
     * - Nếu thiếu: số lượng thực nhập = soLuong - soLuongThieu
     * - Nếu đủ: số lượng thực nhập = soLuong
     * - Cập nhật stock vào ChiTietSanPham + tổng SanPham
     * - Nếu có ít nhất 1 chi tiết thiếu → phiếu nhập = 4 (Thiếu hàng)
     * - Nếu tất cả đủ → phiếu nhập = 5 (Hoàn thành)
     */
    @Transactional
    public PhieuNhap kiemKe(Long phieuNhapId) throws IdInvalidException {
        PhieuNhap phieuNhap = phieuNhapRepository.findById(phieuNhapId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu nhập: " + phieuNhapId));

        int trangThai = phieuNhap.getTrangThai() != null ? phieuNhap.getTrangThai() : -1;
        if (trangThai != TRANG_THAI_DA_NHAN && trangThai != TRANG_THAI_THIEU_HANG) {
            throw new IdInvalidException(
                    "Chỉ có thể kiểm kê phiếu nhập ở trạng thái 'Đã nhận' hoặc 'Thiếu hàng'");
        }

        boolean coThieuHang = capNhatSoLuongSauNhapHang(phieuNhapId);
        if (coThieuHang) {
            phieuNhap.setTrangThai(TRANG_THAI_THIEU_HANG);
        } else {
            phieuNhap.setTrangThai(TRANG_THAI_HOAN_THANH);
        }

        return phieuNhapRepository.save(phieuNhap);
    }

    /**
     * Cập nhật số lượng ChiTietSanPham và tổng SanPham sau khi nhập hàng.
     * - Kiểm tra trạng thái từng chi tiết phiếu nhập: 0 = đủ, 1 = thiếu
     * - Nếu thiếu: số lượng thực nhập = soLuong - soLuongThieu
     * - Nếu đủ: số lượng thực nhập = soLuong
     * - Trả về true nếu có ít nhất 1 chi tiết bị thiếu hàng
     */
    /**
     * Cập nhật số lượng kho theo delta (số lượng mới - số lượng đã nhập trước đó).
     * Nhờ trường soLuongDaNhap lưu trữ lượng đã thực nhập, việc kiểm kê lại sau khi
     * cập nhật chi tiết phiếu nhập (thiếu hàng) sẽ chỉ cộng thêm phần chênh lệch,
     * tránh tình trạng cộng trùng số lượng.
     */
    private boolean capNhatSoLuongSauNhapHang(Long phieuNhapId) {
        PhieuNhap phieuNhap = phieuNhapRepository.findById(phieuNhapId).orElse(null);
        if (phieuNhap == null)
            return false;

        boolean coThieuHang = false;
        List<ChiTietPhieuNhap> danhSachChiTiet = chiTietPhieuNhapRepository.findByPhieuNhapId(phieuNhapId);

        for (ChiTietPhieuNhap ctpn : danhSachChiTiet) {
            if (ctpn.getChiTietSanPham() == null || ctpn.getSoLuong() == null || ctpn.getSoLuong() <= 0)
                continue;

            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(ctpn.getChiTietSanPham().getId()).orElse(null);
            if (ctsp == null)
                continue;

            int soLuongDat = ctpn.getSoLuong();
            int soLuongCanNhap = soLuongDat;

            if (ctpn.getTrangThai() != null && ctpn.getTrangThai() == 1) {
                coThieuHang = true;
                int soLuongThieu = ctpn.getSoLuongThieu() != null ? ctpn.getSoLuongThieu() : 0;
                soLuongCanNhap = Math.max(soLuongDat - soLuongThieu, 0);
            }

            // Tính delta: chỉ cộng thêm phần chênh lệch so với lần kiểm kê trước
            int soLuongDaNhap = ctpn.getSoLuongDaNhap() != null ? ctpn.getSoLuongDaNhap() : 0;
            int delta = soLuongCanNhap - soLuongDaNhap;

            int soLuongHienTai = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;
            ctsp.setSoLuong(soLuongHienTai + delta);
            ctsp.setMaPhieuNhap(phieuNhapId);
            if (phieuNhap.getCuaHang() != null)
                ctsp.setMaCuaHang(phieuNhap.getCuaHang().getId());
            chiTietSanPhamRepository.save(ctsp);

            // Ghi lại số lượng đã nhập thực tế vào chi tiết phiếu nhập
            ctpn.setSoLuongDaNhap(soLuongCanNhap);
            chiTietPhieuNhapRepository.save(ctpn);

            if (ctsp.getSanPham() != null)
                capNhatTongSoLuongSanPham(ctsp.getSanPham().getId());
        }
        return coThieuHang;
    }

    /**
     * Tính lại tổng số lượng sản phẩm = tổng soLuong của tất cả ChiTietSanPham
     * thuộc sản phẩm đó
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

    public void delete(long id) {
        phieuNhapRepository.deleteById(id);
    }

    public PhieuNhap findById(long id) {
        return phieuNhapRepository.findById(id).orElse(null);
    }

    public List<PhieuNhap> findAll() {
        return phieuNhapRepository.findAll();
    }

    public ResultPaginationDTO filterPhieuNhap(
            String tenPhieuNhap, Integer trangThai,
            Long cuaHangId,
            String tenCuaHang, String tenNhaCungCap,
            LocalDateTime ngayTaoTu, LocalDateTime ngayTaoDen,
            LocalDateTime ngayDatHangTu, LocalDateTime ngayDatHangDen,
            LocalDateTime ngayNhanHangTu, LocalDateTime ngayNhanHangDen,
            Pageable pageable) {

        Specification<PhieuNhap> spec = PhieuNhapSpecification.filter(
                tenPhieuNhap, trangThai, cuaHangId, tenCuaHang, tenNhaCungCap,
                ngayTaoTu, ngayTaoDen,
                ngayDatHangTu, ngayDatHangDen, ngayNhanHangTu, ngayNhanHangDen);

        Page<PhieuNhap> page = phieuNhapRepository.findAll(spec, pageable);

        List<ResPhieuNhapDTO> dtoList = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(dtoList);
        return result;
    }

    // ===== DTO Conversion =====

    public ResPhieuNhapDTO convertToDTO(PhieuNhap pn) {
        ResPhieuNhapDTO dto = new ResPhieuNhapDTO();
        dto.setId(pn.getId());
        dto.setTenPhieuNhap(pn.getTenPhieuNhap());
        dto.setTrangThai(pn.getTrangThai());
        dto.setTrangThaiText(getTrangThaiText(pn.getTrangThai()));
        dto.setNgayDatHang(pn.getNgayDatHang());
        dto.setNgayNhanHang(pn.getNgayNhanHang());
        dto.setNgayTao(pn.getNgayTao());
        dto.setNgayCapNhat(pn.getNgayCapNhat());

        if (pn.getCuaHang() != null) {
            CuaHang ch = pn.getCuaHang();
            dto.setCuaHang(new ResPhieuNhapDTO.CuaHangDTO(
                    ch.getId(), ch.getTenCuaHang(), ch.getDiaChi(), ch.getSoDienThoai()));
        }

        if (pn.getNhaCungCap() != null) {
            NhaCungCap ncc = pn.getNhaCungCap();
            dto.setNhaCungCap(new ResPhieuNhapDTO.NhaCungCapDTO(
                    ncc.getId(), ncc.getTenNhaCungCap(), ncc.getSoDienThoai(), ncc.getEmail()));
        }

        if (pn.getChiTietPhieuNhaps() != null) {
            List<ResPhieuNhapDTO.ChiTietPhieuNhapDTO> chiTietDTOs = pn.getChiTietPhieuNhaps().stream()
                    .map(this::convertChiTietToDTO)
                    .collect(Collectors.toList());
            dto.setChiTietPhieuNhaps(chiTietDTOs);
        }

        return dto;
    }

    private ResPhieuNhapDTO.ChiTietPhieuNhapDTO convertChiTietToDTO(ChiTietPhieuNhap ctpn) {
        ResPhieuNhapDTO.ChiTietPhieuNhapDTO dto = new ResPhieuNhapDTO.ChiTietPhieuNhapDTO();
        dto.setId(ctpn.getId());
        dto.setSoLuong(ctpn.getSoLuong());
        dto.setSoLuongThieu(ctpn.getSoLuongThieu());
        dto.setSoLuongDaNhap(ctpn.getSoLuongDaNhap());
        dto.setGhiTru(ctpn.getGhiTru());
        dto.setGhiTruKiemHang(ctpn.getGhiTruKiemHang());
        dto.setTrangThai(ctpn.getTrangThai());
        if (ctpn.getTrangThai() != null) {
            dto.setTrangThaiText(ctpn.getTrangThai() == 0 ? "Đủ" : "Thiếu");
        }

        if (ctpn.getChiTietSanPham() != null) {
            ChiTietSanPham ctsp = ctpn.getChiTietSanPham();
            dto.setChiTietSanPhamId(ctsp.getId());
            if (ctsp.getSanPham() != null) {
                dto.setTenSanPham(ctsp.getSanPham().getTenSanPham());
            }
            if (ctsp.getMauSac() != null) {
                dto.setTenMauSac(ctsp.getMauSac().getTenMauSac());
            }
            if (ctsp.getKichThuoc() != null) {
                dto.setTenKichThuoc(ctsp.getKichThuoc().getTenKichThuoc());
            }
        }

        return dto;
    }

    private String getTrangThaiText(Integer trangThai) {
        if (trangThai == null)
            return null;
        switch (trangThai) {
            case TRANG_THAI_DA_DAT:
                return "Đã đặt";
            case TRANG_THAI_DA_NHAN:
                return "Đã nhận";
            case TRANG_THAI_CHAM_GIAO:
                return "Chậm giao";
            case TRANG_THAI_HUY:
                return "Hủy";
            case TRANG_THAI_THIEU_HANG:
                return "Thiếu hàng";
            case TRANG_THAI_HOAN_THANH:
                return "Hoàn thành";
            default:
                return "Không xác định";
        }
    }
}
