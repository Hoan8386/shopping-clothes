package com.vn.shopping.service;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.request.ReqChiTietKiemKeDTO;
import com.vn.shopping.domain.request.ReqDuyetKiemKeDTO;
import com.vn.shopping.domain.request.ReqKiemKeHangHoaDTO;
import com.vn.shopping.domain.response.ResKiemKeHangHoaDTO;
import com.vn.shopping.repository.*;
import com.vn.shopping.util.SecurityUtil;
import com.vn.shopping.util.error.IdInvalidException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KiemKeHangHoaService {

    private static final int TRANG_THAI_NHAP = 0;
    private static final int TRANG_THAI_CHO_DUYET = 1;
    private static final int TRANG_THAI_YEU_CAU_KIEM_KE_LAI = 2;
    private static final int TRANG_THAI_DA_XAC_NHAN = 3;

    private final KiemKeHangHoaRepository kiemKeHangHoaRepository;
    private final ChiTietKiemKeRepository chiTietKiemKeRepository;
    private final LoaiKiemKeRepository loaiKiemKeRepository;
    private final CuaHangRepository cuaHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;

    public KiemKeHangHoaService(KiemKeHangHoaRepository kiemKeHangHoaRepository,
            ChiTietKiemKeRepository chiTietKiemKeRepository,
            LoaiKiemKeRepository loaiKiemKeRepository,
            CuaHangRepository cuaHangRepository,
            NhanVienRepository nhanVienRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            SanPhamRepository sanPhamRepository) {
        this.kiemKeHangHoaRepository = kiemKeHangHoaRepository;
        this.chiTietKiemKeRepository = chiTietKiemKeRepository;
        this.loaiKiemKeRepository = loaiKiemKeRepository;
        this.cuaHangRepository = cuaHangRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    @Transactional
    public KiemKeHangHoa create(ReqKiemKeHangHoaDTO dto) throws IdInvalidException {
        NhanVien currentNhanVien = getCurrentNhanVien();

        KiemKeHangHoa phieu = new KiemKeHangHoa();
        phieu.setTenPhieuKiemKe(dto.getTenPhieuKiemKe());
        phieu.setGhiChu(dto.getGhiChu());
        phieu.setTrangThai(TRANG_THAI_NHAP);
        phieu.setNgayKiemKe(dto.getNgayKiemKe() != null ? dto.getNgayKiemKe() : LocalDateTime.now());
        phieu.setNhanVienTao(currentNhanVien);

        if (dto.getLoaiKiemKeId() != null) {
            LoaiKiemKe loaiKiemKe = loaiKiemKeRepository.findById(dto.getLoaiKiemKeId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy loại kiểm kê: " + dto.getLoaiKiemKeId()));
            phieu.setLoaiKiemKe(loaiKiemKe);
        }

        CuaHang cuaHang = resolveCuaHang(dto.getCuaHangId(), currentNhanVien);
        phieu.setCuaHang(cuaHang);

        KiemKeHangHoa saved = kiemKeHangHoaRepository.save(phieu);
        upsertChiTiet(saved, dto.getChiTietKiemKes());
        return saved;
    }

    @Transactional
    public KiemKeHangHoa update(ReqKiemKeHangHoaDTO dto) throws IdInvalidException {
        if (dto.getId() == null || dto.getId() == 0) {
            throw new IdInvalidException("Mã phiếu kiểm kê không được để trống");
        }

        KiemKeHangHoa existing = kiemKeHangHoaRepository.findById(dto.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu kiểm kê: " + dto.getId()));

        NhanVien currentNhanVien = getCurrentNhanVien();
        validateCanEditPhieu(existing, currentNhanVien);

        if (existing.getTrangThai() != TRANG_THAI_NHAP && existing.getTrangThai() != TRANG_THAI_YEU_CAU_KIEM_KE_LAI) {
            throw new IdInvalidException("Phiếu kiểm kê hiện không cho phép chỉnh sửa");
        }

        existing.setTenPhieuKiemKe(dto.getTenPhieuKiemKe());
        existing.setGhiChu(dto.getGhiChu());
        existing.setNgayKiemKe(dto.getNgayKiemKe() != null ? dto.getNgayKiemKe() : existing.getNgayKiemKe());

        if (dto.getLoaiKiemKeId() != null) {
            LoaiKiemKe loaiKiemKe = loaiKiemKeRepository.findById(dto.getLoaiKiemKeId())
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy loại kiểm kê: " + dto.getLoaiKiemKeId()));
            existing.setLoaiKiemKe(loaiKiemKe);
        }

        CuaHang cuaHang = resolveCuaHang(dto.getCuaHangId(), currentNhanVien);
        existing.setCuaHang(cuaHang);

        KiemKeHangHoa saved = kiemKeHangHoaRepository.save(existing);
        upsertChiTiet(saved, dto.getChiTietKiemKes());
        return saved;
    }

    @Transactional
    public KiemKeHangHoa guiDuyet(Long id) throws IdInvalidException {
        KiemKeHangHoa existing = kiemKeHangHoaRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu kiểm kê: " + id));

        NhanVien currentNhanVien = getCurrentNhanVien();
        validateCanEditPhieu(existing, currentNhanVien);

        if (existing.getTrangThai() != TRANG_THAI_NHAP && existing.getTrangThai() != TRANG_THAI_YEU_CAU_KIEM_KE_LAI) {
            throw new IdInvalidException("Chỉ được gửi duyệt khi phiếu ở trạng thái Nháp hoặc Yêu cầu kiểm kê lại");
        }

        List<ChiTietKiemKe> chiTietKiemKes = chiTietKiemKeRepository.findByKiemKeHangHoaId(id);
        if (chiTietKiemKes.isEmpty()) {
            throw new IdInvalidException("Phiếu kiểm kê chưa có chi tiết");
        }

        boolean hasEmptySoLuong = chiTietKiemKes.stream().anyMatch(x -> x.getSoLuongThucTe() == null);
        if (hasEmptySoLuong) {
            throw new IdInvalidException("Vui lòng điền đủ số lượng thực tế trước khi gửi duyệt");
        }

        existing.setTrangThai(TRANG_THAI_CHO_DUYET);
        existing.setLyDoYeuCauKiemKeLai(null);
        return kiemKeHangHoaRepository.save(existing);
    }

    @Transactional
    public KiemKeHangHoa duyet(Long id, ReqDuyetKiemKeDTO req) throws IdInvalidException {
        KiemKeHangHoa phieu = kiemKeHangHoaRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu kiểm kê: " + id));

        NhanVien currentNhanVien = getCurrentNhanVien();
        validateAdmin(currentNhanVien);

        if (phieu.getTrangThai() == null || phieu.getTrangThai() != TRANG_THAI_CHO_DUYET) {
            throw new IdInvalidException("Chỉ duyệt được phiếu đang ở trạng thái Chờ duyệt");
        }

        String hanhDong = req != null && req.getHanhDong() != null ? req.getHanhDong().trim().toUpperCase() : "";
        if (!"XAC_NHAN".equals(hanhDong) && !"YEU_CAU_KIEM_KE_LAI".equals(hanhDong)) {
            throw new IdInvalidException("Hành động không hợp lệ. Chỉ nhận XAC_NHAN hoặc YEU_CAU_KIEM_KE_LAI");
        }

        phieu.setNhanVienDuyet(currentNhanVien);

        if ("YEU_CAU_KIEM_KE_LAI".equals(hanhDong)) {
            if (req.getLyDo() == null || req.getLyDo().isBlank()) {
                throw new IdInvalidException("Vui lòng nhập lý do yêu cầu kiểm kê lại");
            }
            phieu.setTrangThai(TRANG_THAI_YEU_CAU_KIEM_KE_LAI);
            phieu.setLyDoYeuCauKiemKeLai(req.getLyDo());
            return kiemKeHangHoaRepository.save(phieu);
        }

        applyInventoryAdjustment(phieu);
        phieu.setTrangThai(TRANG_THAI_DA_XAC_NHAN);
        phieu.setLyDoYeuCauKiemKeLai(null);
        phieu.setNgayXacNhan(LocalDateTime.now());
        return kiemKeHangHoaRepository.save(phieu);
    }

    public KiemKeHangHoa findById(Long id) {
        return kiemKeHangHoaRepository.findById(id).orElse(null);
    }

    public List<KiemKeHangHoa> findAll() {
        return kiemKeHangHoaRepository.findAll();
    }

    public List<KiemKeHangHoa> findByCurrentNhanVien() throws IdInvalidException {
        NhanVien currentNhanVien = getCurrentNhanVien();
        if (isAdmin(currentNhanVien)) {
            return kiemKeHangHoaRepository.findAll();
        }

        if (currentNhanVien.getCuaHang() != null && currentNhanVien.getCuaHang().getId() != null) {
            return kiemKeHangHoaRepository.findByCuaHangId(currentNhanVien.getCuaHang().getId());
        }

        return kiemKeHangHoaRepository.findByNhanVienTaoId(currentNhanVien.getId());
    }

    public ResKiemKeHangHoaDTO convertToDTO(KiemKeHangHoa phieu) {
        ResKiemKeHangHoaDTO dto = new ResKiemKeHangHoaDTO();
        dto.setId(phieu.getId());
        dto.setTenPhieuKiemKe(phieu.getTenPhieuKiemKe());
        dto.setTrangThai(phieu.getTrangThai());
        dto.setTrangThaiText(getTrangThaiText(phieu.getTrangThai()));
        dto.setGhiChu(phieu.getGhiChu());
        dto.setLyDoYeuCauKiemKeLai(phieu.getLyDoYeuCauKiemKeLai());
        dto.setNgayKiemKe(phieu.getNgayKiemKe());
        dto.setNgayXacNhan(phieu.getNgayXacNhan());
        dto.setNgayTao(phieu.getNgayTao());
        dto.setNgayCapNhat(phieu.getNgayCapNhat());

        if (phieu.getLoaiKiemKe() != null) {
            dto.setLoaiKiemKe(new ResKiemKeHangHoaDTO.LoaiKiemKeDTO(
                    phieu.getLoaiKiemKe().getId(),
                    phieu.getLoaiKiemKe().getTenLoaiKiemKe()));
        }

        if (phieu.getCuaHang() != null) {
            dto.setCuaHang(new ResKiemKeHangHoaDTO.CuaHangDTO(
                    phieu.getCuaHang().getId(),
                    phieu.getCuaHang().getTenCuaHang(),
                    phieu.getCuaHang().getDiaChi()));
        }

        if (phieu.getNhanVienTao() != null) {
            dto.setNhanVienTao(new ResKiemKeHangHoaDTO.NhanVienDTO(
                    phieu.getNhanVienTao().getId(),
                    phieu.getNhanVienTao().getTenNhanVien(),
                    phieu.getNhanVienTao().getEmail()));
        }

        if (phieu.getNhanVienDuyet() != null) {
            dto.setNhanVienDuyet(new ResKiemKeHangHoaDTO.NhanVienDTO(
                    phieu.getNhanVienDuyet().getId(),
                    phieu.getNhanVienDuyet().getTenNhanVien(),
                    phieu.getNhanVienDuyet().getEmail()));
        }

        List<ChiTietKiemKe> chiTietKiemKes = chiTietKiemKeRepository.findByKiemKeHangHoaId(phieu.getId());
        List<ResKiemKeHangHoaDTO.ChiTietKiemKeDTO> chiTietDTOs = chiTietKiemKes.stream()
                .map(this::convertChiTietToDTO)
                .collect(Collectors.toList());
        dto.setChiTietKiemKes(chiTietDTOs);

        return dto;
    }

    private ResKiemKeHangHoaDTO.ChiTietKiemKeDTO convertChiTietToDTO(ChiTietKiemKe chiTiet) {
        ResKiemKeHangHoaDTO.ChiTietKiemKeDTO dto = new ResKiemKeHangHoaDTO.ChiTietKiemKeDTO();
        dto.setId(chiTiet.getId());
        dto.setSoLuongHeThong(chiTiet.getSoLuongHeThong());
        dto.setSoLuongThucTe(chiTiet.getSoLuongThucTe());
        dto.setChenhLech(chiTiet.getChenhLech());
        dto.setGhiChu(chiTiet.getGhiChu());

        if (chiTiet.getChiTietSanPham() != null) {
            ChiTietSanPham ctsp = chiTiet.getChiTietSanPham();
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

    private void upsertChiTiet(KiemKeHangHoa phieu, List<ReqChiTietKiemKeDTO> items) throws IdInvalidException {
        List<ChiTietKiemKe> oldItems = chiTietKiemKeRepository.findByKiemKeHangHoaId(phieu.getId());
        if (!oldItems.isEmpty()) {
            chiTietKiemKeRepository.deleteAll(oldItems);
        }

        if (items == null || items.isEmpty()) {
            return;
        }

        for (ReqChiTietKiemKeDTO item : items) {
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(item.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết sản phẩm: " + item.getChiTietSanPhamId()));

            validateChiTietSanPhamTheoCuaHang(phieu.getCuaHang(), ctsp);

            int soLuongHeThong = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;
            int soLuongThucTe = item.getSoLuongThucTe() != null ? item.getSoLuongThucTe() : 0;

            ChiTietKiemKe chiTiet = new ChiTietKiemKe();
            chiTiet.setKiemKeHangHoa(phieu);
            chiTiet.setChiTietSanPham(ctsp);
            chiTiet.setSoLuongHeThong(soLuongHeThong);
            chiTiet.setSoLuongThucTe(soLuongThucTe);
            chiTiet.setChenhLech(soLuongThucTe - soLuongHeThong);
            chiTiet.setGhiChu(item.getGhiChu());
            chiTietKiemKeRepository.save(chiTiet);
        }
    }

    private void applyInventoryAdjustment(KiemKeHangHoa phieu) throws IdInvalidException {
        List<ChiTietKiemKe> chiTietKiemKes = chiTietKiemKeRepository.findByKiemKeHangHoaId(phieu.getId());
        if (chiTietKiemKes.isEmpty()) {
            throw new IdInvalidException("Phiếu kiểm kê chưa có chi tiết");
        }

        Set<Long> sanPhamIds = new HashSet<>();
        for (ChiTietKiemKe item : chiTietKiemKes) {
            ChiTietSanPham ctsp = item.getChiTietSanPham();
            if (ctsp == null) {
                continue;
            }

            validateChiTietSanPhamTheoCuaHang(phieu.getCuaHang(), ctsp);

            int soLuongThucTe = item.getSoLuongThucTe() != null ? item.getSoLuongThucTe() : 0;
            ctsp.setSoLuong(soLuongThucTe);
            ctsp.setMaCuaHang(phieu.getCuaHang() != null ? phieu.getCuaHang().getId() : ctsp.getMaCuaHang());
            chiTietSanPhamRepository.save(ctsp);

            if (ctsp.getSanPham() != null) {
                sanPhamIds.add(ctsp.getSanPham().getId());
            }
        }

        for (Long sanPhamId : sanPhamIds) {
            capNhatTongSoLuongSanPham(sanPhamId);
        }
    }

    private void capNhatTongSoLuongSanPham(Long sanPhamId) {
        SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElse(null);
        if (sanPham == null) {
            return;
        }

        List<ChiTietSanPham> dsChiTiet = chiTietSanPhamRepository.findBySanPhamId(sanPhamId);
        int tongSoLuong = dsChiTiet.stream()
                .mapToInt(item -> item.getSoLuong() != null ? item.getSoLuong() : 0)
                .sum();

        sanPham.setSoLuong(tongSoLuong);
        sanPhamRepository.save(sanPham);
    }

    private void validateCanEditPhieu(KiemKeHangHoa phieu, NhanVien currentNhanVien) throws IdInvalidException {
        if (isAdmin(currentNhanVien)) {
            return;
        }

        // Khi admin yêu cầu kiểm kê lại, cho phép nhân viên cùng cửa hàng cập nhật
        // và gửi lại phiếu, không bắt buộc phải là người tạo ban đầu.
        if (phieu.getTrangThai() != null
                && phieu.getTrangThai() == TRANG_THAI_YEU_CAU_KIEM_KE_LAI
                && isSameStoreNhanVien(phieu, currentNhanVien)) {
            return;
        }

        if (phieu.getNhanVienTao() == null
                || !Objects.equals(phieu.getNhanVienTao().getId(), currentNhanVien.getId())) {
            throw new IdInvalidException("Bạn không có quyền chỉnh sửa phiếu kiểm kê này");
        }
    }

    private boolean isSameStoreNhanVien(KiemKeHangHoa phieu, NhanVien currentNhanVien) {
        if (phieu == null || currentNhanVien == null) {
            return false;
        }
        if (phieu.getCuaHang() == null || phieu.getCuaHang().getId() == null) {
            return false;
        }
        if (currentNhanVien.getCuaHang() == null || currentNhanVien.getCuaHang().getId() == null) {
            return false;
        }
        return Objects.equals(phieu.getCuaHang().getId(), currentNhanVien.getCuaHang().getId());
    }

    private CuaHang resolveCuaHang(Long cuaHangId, NhanVien currentNhanVien) throws IdInvalidException {
        if (!isAdmin(currentNhanVien)) {
            if (currentNhanVien.getCuaHang() == null || currentNhanVien.getCuaHang().getId() == null) {
                throw new IdInvalidException("Nhân viên chưa được gán cửa hàng");
            }
            return currentNhanVien.getCuaHang();
        }

        if (cuaHangId == null) {
            throw new IdInvalidException("Vui lòng chọn cửa hàng");
        }

        return cuaHangRepository.findById(cuaHangId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng: " + cuaHangId));
    }

    private void validateAdmin(NhanVien nhanVien) throws IdInvalidException {
        if (!isAdmin(nhanVien)) {
            throw new IdInvalidException("Chỉ ADMIN mới có quyền duyệt phiếu kiểm kê");
        }
    }

    private boolean isAdmin(NhanVien nhanVien) {
        if (nhanVien == null || nhanVien.getRole() == null || nhanVien.getRole().getName() == null) {
            return false;
        }

        String roleName = nhanVien.getRole().getName().trim().toUpperCase();
        return roleName.equals("ADMIN") || roleName.contains("ADMIN");
    }

    private void validateChiTietSanPhamTheoCuaHang(CuaHang cuaHang, ChiTietSanPham ctsp) throws IdInvalidException {
        if (cuaHang == null || cuaHang.getId() == null) {
            return;
        }

        if (ctsp.getMaCuaHang() != null && !Objects.equals(ctsp.getMaCuaHang(), cuaHang.getId())) {
            throw new IdInvalidException(
                    "Chi tiết sản phẩm " + ctsp.getId() + " không thuộc cửa hàng của phiếu kiểm kê");
        }
    }

    private NhanVien getCurrentNhanVien() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (email.isBlank()) {
            throw new IdInvalidException("Không xác định được người dùng hiện tại");
        }

        return nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhân viên theo email: " + email));
    }

    private String getTrangThaiText(Integer trangThai) {
        if (trangThai == null) {
            return "Không xác định";
        }

        switch (trangThai) {
            case TRANG_THAI_NHAP:
                return "Nháp";
            case TRANG_THAI_CHO_DUYET:
                return "Đã gửi";
            case TRANG_THAI_YEU_CAU_KIEM_KE_LAI:
                return "Yêu cầu kiểm kê lại";
            case TRANG_THAI_DA_XAC_NHAN:
                return "Đã xác nhận";
            default:
                return "Không xác định";
        }
    }
}
