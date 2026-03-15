package com.vn.shopping.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietDonLuanChuyen;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.domain.DonLuanChuyen;
import com.vn.shopping.domain.LoaiDonLuanChuyen;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.domain.request.ReqDonLuanChuyenDTO;
import com.vn.shopping.domain.response.ResDonLuanChuyenDTO;
import com.vn.shopping.domain.response.ResultPaginationDTO;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.CuaHangRepository;
import com.vn.shopping.repository.DonLuanChuyenRepository;
import com.vn.shopping.repository.LoaiDonLuanChuyenRepository;
import com.vn.shopping.repository.NhanVienRepository;
import com.vn.shopping.util.SecurityUtil;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class DonLuanChuyenService {

    private final DonLuanChuyenRepository donLuanChuyenRepository;
    private final CuaHangRepository cuaHangRepository;
    private final LoaiDonLuanChuyenRepository loaiDonLuanChuyenRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final NhanVienRepository nhanVienRepository;

    public DonLuanChuyenService(DonLuanChuyenRepository donLuanChuyenRepository,
            CuaHangRepository cuaHangRepository,
            LoaiDonLuanChuyenRepository loaiDonLuanChuyenRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            NhanVienRepository nhanVienRepository) {
        this.donLuanChuyenRepository = donLuanChuyenRepository;
        this.cuaHangRepository = cuaHangRepository;
        this.loaiDonLuanChuyenRepository = loaiDonLuanChuyenRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.nhanVienRepository = nhanVienRepository;
    }

    @Transactional
    public DonLuanChuyen taoDonLuanChuyen(ReqDonLuanChuyenDTO req) throws IdInvalidException {
        Optional<NhanVien> currentStaff = getCurrentNhanVien();
        Long cuaHangDatId = req.getCuaHangDatId();

        if (currentStaff.isPresent()) {
            NhanVien nhanVien = currentStaff.get();
            if (nhanVien.getCuaHang() == null || nhanVien.getCuaHang().getId() == null) {
                throw new IdInvalidException("Nhân viên chưa được gán cửa hàng");
            }
            Long staffStoreId = nhanVien.getCuaHang().getId();
            if (cuaHangDatId != null && !staffStoreId.equals(cuaHangDatId)) {
                throw new IdInvalidException("Nhân viên chỉ được tạo đơn cho cửa hàng của mình");
            }
            cuaHangDatId = staffStoreId;
        }

        if (cuaHangDatId == null) {
            throw new IdInvalidException("Cửa hàng đặt không được để trống");
        }

        if (req.getCuaHangGuiId() == null) {
            throw new IdInvalidException("Cửa hàng gửi không được để trống");
        }

        if (req.getLoaiDonLuanChuyenId() == null) {
            throw new IdInvalidException("Loại đơn luân chuyển không được để trống");
        }

        final Long resolvedCuaHangDatId = cuaHangDatId;
        CuaHang cuaHangDat = cuaHangRepository.findById(resolvedCuaHangDatId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng đặt: " + resolvedCuaHangDatId));

        CuaHang cuaHangGui = cuaHangRepository.findById(req.getCuaHangGuiId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng gửi: " + req.getCuaHangGuiId()));

        if (cuaHangDat.getId().equals(cuaHangGui.getId())) {
            throw new IdInvalidException("Cửa hàng đặt và cửa hàng gửi phải khác nhau");
        }

        LoaiDonLuanChuyen loaiDon = loaiDonLuanChuyenRepository.findById(req.getLoaiDonLuanChuyenId())
                .orElseThrow(() -> new IdInvalidException(
                        "Không tìm thấy loại đơn luân chuyển: " + req.getLoaiDonLuanChuyenId()));

        if (req.getChiTietDonLuanChuyens() == null || req.getChiTietDonLuanChuyens().isEmpty()) {
            throw new IdInvalidException("Phải chọn ít nhất một sản phẩm để luân chuyển");
        }

        DonLuanChuyen donLuanChuyen = new DonLuanChuyen();
        donLuanChuyen.setCuaHangDat(cuaHangDat);
        donLuanChuyen.setCuaHangGui(cuaHangGui);
        donLuanChuyen.setLoaiDonLuanChuyen(loaiDon);
        donLuanChuyen.setTenDon(req.getTenDon());
        donLuanChuyen.setGhiTru(req.getGhiTru());
        donLuanChuyen.setTrangThai(0);

        List<ChiTietDonLuanChuyen> chiTietList = new ArrayList<>();
        for (ReqDonLuanChuyenDTO.ChiTietDonLuanChuyenItem item : req.getChiTietDonLuanChuyens()) {
            if (item.getChiTietSanPhamId() == null) {
                throw new IdInvalidException("Chi tiết sản phẩm không được để trống");
            }
            if (item.getSoLuong() == null || item.getSoLuong() <= 0) {
                throw new IdInvalidException("Số lượng luân chuyển phải lớn hơn 0");
            }

            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(item.getChiTietSanPhamId())
                    .orElseThrow(() -> new IdInvalidException(
                            "Không tìm thấy chi tiết sản phẩm: " + item.getChiTietSanPhamId()));

            if (ctsp.getMaCuaHang() == null || !ctsp.getMaCuaHang().equals(cuaHangGui.getId())) {
                throw new IdInvalidException("Chi tiết sản phẩm " + item.getChiTietSanPhamId()
                        + " không thuộc cửa hàng gửi");
            }

            int sourceQuantity = ctsp.getSoLuong() == null ? 0 : ctsp.getSoLuong();
            if (sourceQuantity < item.getSoLuong()) {
                throw new IdInvalidException("Không đủ tồn kho cho chi tiết sản phẩm: " + item.getChiTietSanPhamId());
            }

            ChiTietDonLuanChuyen chiTiet = new ChiTietDonLuanChuyen();
            chiTiet.setDonLuanChuyen(donLuanChuyen);
            chiTiet.setChiTietSanPham(ctsp);
            chiTiet.setSoLuong(item.getSoLuong());
            chiTiet.setGhiTru(item.getGhiTru());
            chiTiet.setTrangThai(0);
            chiTietList.add(chiTiet);
        }

        donLuanChuyen.setChiTietDonLuanChuyens(chiTietList);
        return donLuanChuyenRepository.save(donLuanChuyen);
    }

    @Transactional
    public DonLuanChuyen capNhatTrangThai(Long id, Integer trangThai) throws IdInvalidException {
        DonLuanChuyen donLuanChuyen = donLuanChuyenRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đơn luân chuyển: " + id));

        if (trangThai == null || trangThai < 0 || trangThai > 4) {
            throw new IdInvalidException("Trạng thái đơn luân chuyển không hợp lệ");
        }

        Optional<NhanVien> currentStaff = getCurrentNhanVien();
        if (currentStaff.isPresent()) {
            NhanVien nhanVien = currentStaff.get();
            if (nhanVien.getCuaHang() == null || nhanVien.getCuaHang().getId() == null) {
                throw new IdInvalidException("Nhân viên chưa được gán cửa hàng");
            }

            Long staffStoreId = nhanVien.getCuaHang().getId();
            Long cuaHangDatId = donLuanChuyen.getCuaHangDat() != null ? donLuanChuyen.getCuaHangDat().getId() : null;
            Long cuaHangGuiId = donLuanChuyen.getCuaHangGui() != null ? donLuanChuyen.getCuaHangGui().getId() : null;
            boolean isStoreDat = cuaHangDatId != null && staffStoreId.equals(cuaHangDatId);
            boolean isStoreGui = cuaHangGuiId != null && staffStoreId.equals(cuaHangGuiId);

            if (!isStoreDat && !isStoreGui) {
                throw new IdInvalidException("Nhân viên chỉ được thao tác đơn luân chuyển của cửa hàng mình");
            }

            int currentStatus = donLuanChuyen.getTrangThai() == null ? -1 : donLuanChuyen.getTrangThai();

            // Luồng nghiệp vụ:
            // - Cửa hàng gửi xác nhận đơn: 0 (Chờ xác nhận) -> 4 (Đã xác nhận) hoặc từ chối
            // -> 3.
            // - Cửa hàng gửi bắt đầu giao: 4 -> 1 (Đang giao) hoặc từ chối -> 3.
            // - Cửa hàng nhận xác nhận đã nhận: 1 -> 2 (Đã nhận).
            boolean validTransition = (isStoreGui && currentStatus == 0 && (trangThai == 4 || trangThai == 3)) ||
                    (isStoreGui && currentStatus == 4 && (trangThai == 1 || trangThai == 3)) ||
                    (isStoreDat && currentStatus == 1 && trangThai == 2);

            if (!validTransition) {
                throw new IdInvalidException("Trạng thái không hợp lệ cho nghiệp vụ nhân viên");
            }
        }

        donLuanChuyen.setTrangThai(trangThai);

        if (trangThai == 1) {
            donLuanChuyen.setThoiGianGiao(LocalDateTime.now());
        }

        if (trangThai == 2) {
            donLuanChuyen.setThoiGianNhan(LocalDateTime.now());
            applyInventoryTransfer(donLuanChuyen);
        }

        if (donLuanChuyen.getChiTietDonLuanChuyens() != null) {
            for (ChiTietDonLuanChuyen ct : donLuanChuyen.getChiTietDonLuanChuyens()) {
                ct.setTrangThai(trangThai);
            }
        }

        return donLuanChuyenRepository.save(donLuanChuyen);
    }

    public DonLuanChuyen findById(Long id) {
        Optional<DonLuanChuyen> don = donLuanChuyenRepository.findById(id);
        if (don.isEmpty()) {
            return null;
        }
        if (!canAccessTransfer(don.get())) {
            return null;
        }
        return don.get();
    }

    public List<DonLuanChuyen> findByCuaHangDatId(Long cuaHangId) {
        Optional<NhanVien> currentStaff = getCurrentNhanVien();
        if (currentStaff.isPresent()) {
            Long staffStoreId = currentStaff.get().getCuaHang() != null ? currentStaff.get().getCuaHang().getId()
                    : null;
            if (staffStoreId == null || !staffStoreId.equals(cuaHangId)) {
                return new ArrayList<>();
            }
        }
        return donLuanChuyenRepository.findByCuaHangDatId(cuaHangId);
    }

    public List<DonLuanChuyen> findByCuaHangGuiId(Long cuaHangId) {
        Optional<NhanVien> currentStaff = getCurrentNhanVien();
        if (currentStaff.isPresent()) {
            Long staffStoreId = currentStaff.get().getCuaHang() != null ? currentStaff.get().getCuaHang().getId()
                    : null;
            if (staffStoreId == null || !staffStoreId.equals(cuaHangId)) {
                return new ArrayList<>();
            }
        }
        return donLuanChuyenRepository.findByCuaHangGuiId(cuaHangId);
    }

    public ResultPaginationDTO findAll(Pageable pageable) {
        Optional<NhanVien> currentStaff = getCurrentNhanVien();

        Page<DonLuanChuyen> page;
        if (currentStaff.isPresent() && currentStaff.get().getCuaHang() != null
                && currentStaff.get().getCuaHang().getId() != null) {
            Long storeId = currentStaff.get().getCuaHang().getId();
            List<DonLuanChuyen> received = donLuanChuyenRepository.findByCuaHangDatId(storeId);
            List<DonLuanChuyen> sent = donLuanChuyenRepository.findByCuaHangGuiId(storeId);
            List<DonLuanChuyen> allTransfers = new ArrayList<>(received);
            for (DonLuanChuyen item : sent) {
                if (!allTransfers.contains(item)) {
                    allTransfers.add(item);
                }
            }

            int fromIndex = (int) pageable.getOffset();
            int toIndex = Math.min(fromIndex + pageable.getPageSize(), allTransfers.size());
            List<DonLuanChuyen> content = fromIndex >= allTransfers.size() ? List.of()
                    : allTransfers.subList(fromIndex, toIndex);
            page = new PageImpl<>(content, pageable, allTransfers.size());
        } else {
            page = donLuanChuyenRepository.findAll(pageable);
        }

        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        result.setMeta(meta);

        List<ResDonLuanChuyenDTO> dtos = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        result.setResult(dtos);
        return result;
    }

    public ResDonLuanChuyenDTO convertToDTO(DonLuanChuyen donLuanChuyen) {
        ResDonLuanChuyenDTO dto = new ResDonLuanChuyenDTO();
        dto.setId(donLuanChuyen.getId());
        dto.setTenDon(donLuanChuyen.getTenDon());
        dto.setGhiTru(donLuanChuyen.getGhiTru());
        dto.setGhiTruKiemHang(donLuanChuyen.getGhiTruKiemHang());
        dto.setTrangThai(mapTrangThai(donLuanChuyen.getTrangThai()));
        dto.setThoiGianGiao(donLuanChuyen.getThoiGianGiao());
        dto.setThoiGianNhan(donLuanChuyen.getThoiGianNhan());
        dto.setNgayTao(donLuanChuyen.getNgayTao());
        dto.setNgayCapNhat(donLuanChuyen.getNgayCapNhat());

        if (donLuanChuyen.getCuaHangDat() != null) {
            dto.setCuaHangDatId(donLuanChuyen.getCuaHangDat().getId());
            dto.setTenCuaHangDat(donLuanChuyen.getCuaHangDat().getTenCuaHang());
        }

        if (donLuanChuyen.getCuaHangGui() != null) {
            dto.setCuaHangGuiId(donLuanChuyen.getCuaHangGui().getId());
            dto.setTenCuaHangGui(donLuanChuyen.getCuaHangGui().getTenCuaHang());
        }

        if (donLuanChuyen.getLoaiDonLuanChuyen() != null) {
            dto.setLoaiDonLuanChuyenId(donLuanChuyen.getLoaiDonLuanChuyen().getId());
            dto.setTenLoaiDonLuanChuyen(donLuanChuyen.getLoaiDonLuanChuyen().getTenLoai());
        }

        if (donLuanChuyen.getChiTietDonLuanChuyens() != null) {
            List<ResDonLuanChuyenDTO.ChiTietDonLuanChuyenDTO> chiTietDTOs = new ArrayList<>();
            for (ChiTietDonLuanChuyen ct : donLuanChuyen.getChiTietDonLuanChuyens()) {
                ResDonLuanChuyenDTO.ChiTietDonLuanChuyenDTO ctDto = new ResDonLuanChuyenDTO.ChiTietDonLuanChuyenDTO();
                ctDto.setId(ct.getId());
                ctDto.setHinhAnh(ct.getHinhAnh());
                ctDto.setSoLuong(ct.getSoLuong());
                ctDto.setTrangThai(mapTrangThai(ct.getTrangThai()));
                ctDto.setGhiTru(ct.getGhiTru());
                ctDto.setGhiTruKiemHang(ct.getGhiTruKiemHang());

                ChiTietSanPham ctsp = ct.getChiTietSanPham();
                if (ctsp != null) {
                    ctDto.setChiTietSanPhamId(ctsp.getId());
                    SanPham sp = ctsp.getSanPham();
                    if (sp != null) {
                        ctDto.setTenSanPham(sp.getTenSanPham());
                        ctDto.setHinhAnhSanPham(sp.getHinhAnhChinh());
                    }
                    if (ctsp.getMauSac() != null) {
                        ctDto.setMauSac(ctsp.getMauSac().getTenMauSac());
                    }
                    if (ctsp.getKichThuoc() != null) {
                        ctDto.setKichThuoc(ctsp.getKichThuoc().getTenKichThuoc());
                    }
                }

                chiTietDTOs.add(ctDto);
            }
            dto.setChiTietDonLuanChuyens(chiTietDTOs);
        }

        return dto;
    }

    private Optional<NhanVien> getCurrentNhanVien() {
        Optional<String> currentUserEmail = SecurityUtil.getCurrentUserLogin();
        if (currentUserEmail.isEmpty()) {
            return Optional.empty();
        }
        return nhanVienRepository.findByEmail(currentUserEmail.get());
    }

    private boolean canAccessTransfer(DonLuanChuyen donLuanChuyen) {
        Optional<NhanVien> currentStaff = getCurrentNhanVien();
        if (currentStaff.isEmpty()) {
            return true;
        }
        if (currentStaff.get().getCuaHang() == null || currentStaff.get().getCuaHang().getId() == null) {
            return false;
        }

        Long staffStoreId = currentStaff.get().getCuaHang().getId();
        Long cuaHangDatId = donLuanChuyen.getCuaHangDat() != null ? donLuanChuyen.getCuaHangDat().getId() : null;
        Long cuaHangGuiId = donLuanChuyen.getCuaHangGui() != null ? donLuanChuyen.getCuaHangGui().getId() : null;
        return staffStoreId.equals(cuaHangDatId) || staffStoreId.equals(cuaHangGuiId);
    }

    private void applyInventoryTransfer(DonLuanChuyen donLuanChuyen) throws IdInvalidException {
        if (donLuanChuyen.getChiTietDonLuanChuyens() == null) {
            return;
        }

        for (ChiTietDonLuanChuyen ct : donLuanChuyen.getChiTietDonLuanChuyens()) {
            ChiTietSanPham ctsp = ct.getChiTietSanPham();
            if (ctsp == null || ct.getSoLuong() == null || ct.getSoLuong() <= 0) {
                continue;
            }

            if (ctsp.getMaCuaHang() == null || donLuanChuyen.getCuaHangGui() == null
                    || !ctsp.getMaCuaHang().equals(donLuanChuyen.getCuaHangGui().getId())) {
                throw new IdInvalidException("Chi tiết sản phẩm nguồn không thuộc cửa hàng gửi của đơn");
            }

            int sourceQuantity = ctsp.getSoLuong() == null ? 0 : ctsp.getSoLuong();
            if (sourceQuantity < ct.getSoLuong()) {
                throw new IdInvalidException("Không đủ tồn kho ở cửa hàng gửi cho chi tiết sản phẩm: " + ctsp.getId());
            }

            ctsp.setSoLuong(sourceQuantity - ct.getSoLuong());
            chiTietSanPhamRepository.save(ctsp);

            if (donLuanChuyen.getCuaHangDat() == null || donLuanChuyen.getCuaHangDat().getId() == null) {
                throw new IdInvalidException("Đơn luân chuyển chưa có cửa hàng đặt");
            }

            Optional<ChiTietSanPham> destinationOptional = Optional.empty();
            if (ctsp.getSanPham() != null && ctsp.getMauSac() != null && ctsp.getKichThuoc() != null) {
                destinationOptional = chiTietSanPhamRepository
                        .findFirstBySanPhamIdAndMauSacIdAndKichThuocIdAndMaCuaHang(
                                ctsp.getSanPham().getId(),
                                ctsp.getMauSac().getId(),
                                ctsp.getKichThuoc().getId(),
                                donLuanChuyen.getCuaHangDat().getId());
            }

            ChiTietSanPham ctspNhan = destinationOptional.orElseGet(() -> {
                ChiTietSanPham created = new ChiTietSanPham();
                created.setSanPham(ctsp.getSanPham());
                created.setMauSac(ctsp.getMauSac());
                created.setKichThuoc(ctsp.getKichThuoc());
                created.setMaCuaHang(donLuanChuyen.getCuaHangDat().getId());
                created.setSoLuong(0);
                created.setTrangThai(ctsp.getTrangThai() != null ? ctsp.getTrangThai() : 1);
                created.setMoTa(ctsp.getMoTa());
                created.setGhiTru(ctsp.getGhiTru());
                created.setMaPhieuNhap(ctsp.getMaPhieuNhap());
                return created;
            });

            int destinationQuantity = ctspNhan.getSoLuong() == null ? 0 : ctspNhan.getSoLuong();
            ctspNhan.setSoLuong(destinationQuantity + ct.getSoLuong());
            chiTietSanPhamRepository.save(ctspNhan);
        }
    }

    private String mapTrangThai(Integer trangThai) {
        if (trangThai == null) {
            return "Không xác định";
        }
        return switch (trangThai) {
            case 0 -> "Chờ xác nhận";
            case 1 -> "Đang giao";
            case 2 -> "Đã nhận";
            case 3 -> "Từ chối";
            case 4 -> "Đã xác nhận";
            default -> "Không xác định";
        };
    }
}
