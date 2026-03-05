package com.vn.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vn.shopping.domain.ChiTietPhieuNhap;
import com.vn.shopping.domain.ChiTietSanPham;
import com.vn.shopping.domain.CuaHang;
import com.vn.shopping.domain.NhaCungCap;
import com.vn.shopping.domain.PhieuNhap;
import com.vn.shopping.domain.SanPham;
import com.vn.shopping.repository.ChiTietPhieuNhapRepository;
import com.vn.shopping.repository.ChiTietSanPhamRepository;
import com.vn.shopping.repository.CuaHangRepository;
import com.vn.shopping.repository.NhaCungCapRepository;
import com.vn.shopping.repository.PhieuNhapRepository;
import com.vn.shopping.repository.SanPhamRepository;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class PhieuNhapService {

    private static final int TRANG_THAI_NHAP_THANH_CONG = 1;

    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;
    private final CuaHangRepository cuaHangRepository;
    private final NhaCungCapRepository nhaCungCapRepository;

    public PhieuNhapService(PhieuNhapRepository phieuNhapRepository,
            ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
            ChiTietSanPhamRepository chiTietSanPhamRepository,
            SanPhamRepository sanPhamRepository,
            CuaHangRepository cuaHangRepository,
            NhaCungCapRepository nhaCungCapRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.chiTietSanPhamRepository = chiTietSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.cuaHangRepository = cuaHangRepository;
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    public PhieuNhap create(PhieuNhap phieuNhap) {
        return phieuNhapRepository.save(phieuNhap);
    }

    public PhieuNhap create(String tenPhieuNhap, Long cuaHangId, Long nhaCungCapId,
            Integer trangThai) throws IdInvalidException {
        PhieuNhap pn = new PhieuNhap();
        pn.setTenPhieuNhap(tenPhieuNhap);
        pn.setTrangThai(trangThai);

        if (cuaHangId != null) {
            CuaHang ch = cuaHangRepository.findById(cuaHangId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy cửa hàng: " + cuaHangId));
            pn.setCuaHang(ch);
        }
        if (nhaCungCapId != null) {
            NhaCungCap ncc = nhaCungCapRepository.findById(nhaCungCapId)
                    .orElseThrow(() -> new IdInvalidException("Không tìm thấy nhà cung cấp: " + nhaCungCapId));
            pn.setNhaCungCap(ncc);
        }

        return phieuNhapRepository.save(pn);
    }

    @Transactional
    public PhieuNhap update(PhieuNhap phieuNhap) throws IdInvalidException {
        PhieuNhap existing = phieuNhapRepository.findById(phieuNhap.getId())
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy phiếu nhập: " + phieuNhap.getId()));

        Integer oldTrangThai = existing.getTrangThai();

        existing.setTenPhieuNhap(phieuNhap.getTenPhieuNhap());
        existing.setCuaHang(phieuNhap.getCuaHang());
        existing.setNhaCungCap(phieuNhap.getNhaCungCap());
        existing.setTrangThai(phieuNhap.getTrangThai());
        existing.setNgayGiaoHang(phieuNhap.getNgayGiaoHang());
        existing.setNgayNhanHang(phieuNhap.getNgayNhanHang());

        PhieuNhap saved = phieuNhapRepository.save(existing);

        // Khi trạng thái chuyển sang "nhập thành công" => cập nhật số lượng
        boolean isNewSuccess = (phieuNhap.getTrangThai() != null
                && phieuNhap.getTrangThai() == TRANG_THAI_NHAP_THANH_CONG
                && (oldTrangThai == null || oldTrangThai != TRANG_THAI_NHAP_THANH_CONG));

        if (isNewSuccess) {
            capNhatSoLuongSauNhapHang(saved.getId());
        }

        return saved;
    }

    /**
     * Cập nhật số lượng ChiTietSanPham và tổng SanPham sau khi nhập hàng thành
     * công.
     * - Cộng soLuong từ ChiTietPhieuNhap vào ChiTietSanPham tương ứng
     * - Tính lại tổng soLuong trên SanPham = tổng soLuong của tất cả ChiTietSanPham
     * thuộc sản phẩm đó
     */
    private void capNhatSoLuongSauNhapHang(Long phieuNhapId) {
        List<ChiTietPhieuNhap> danhSachChiTiet = chiTietPhieuNhapRepository.findByPhieuNhapId(phieuNhapId);

        for (ChiTietPhieuNhap ctpn : danhSachChiTiet) {
            if (ctpn.getChiTietSanPham() == null || ctpn.getSoLuong() == null || ctpn.getSoLuong() <= 0) {
                continue;
            }

            // Cập nhật số lượng chi tiết sản phẩm
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(ctpn.getChiTietSanPham().getId())
                    .orElse(null);
            if (ctsp == null) {
                continue;
            }

            int soLuongHienTai = ctsp.getSoLuong() != null ? ctsp.getSoLuong() : 0;
            ctsp.setSoLuong(soLuongHienTai + ctpn.getSoLuong());
            chiTietSanPhamRepository.save(ctsp);

            // Tính lại tổng số lượng sản phẩm
            if (ctsp.getSanPham() != null) {
                capNhatTongSoLuongSanPham(ctsp.getSanPham().getId());
            }
        }
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
}
