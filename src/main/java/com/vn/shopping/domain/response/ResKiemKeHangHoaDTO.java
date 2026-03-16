package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResKiemKeHangHoaDTO {
    private Long id;
    private String tenPhieuKiemKe;
    private Integer trangThai;
    private String trangThaiText;
    private String ghiChu;
    private String lyDoYeuCauKiemKeLai;
    private LocalDateTime ngayKiemKe;
    private LocalDateTime ngayXacNhan;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    private LoaiKiemKeDTO loaiKiemKe;
    private CuaHangDTO cuaHang;
    private NhanVienDTO nhanVienTao;
    private NhanVienDTO nhanVienDuyet;
    private List<ChiTietKiemKeDTO> chiTietKiemKes;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoaiKiemKeDTO {
        private Long id;
        private String tenLoaiKiemKe;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CuaHangDTO {
        private Long id;
        private String tenCuaHang;
        private String diaChi;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NhanVienDTO {
        private Long id;
        private String tenNhanVien;
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietKiemKeDTO {
        private Long id;
        private Long chiTietSanPhamId;
        private String tenSanPham;
        private String tenMauSac;
        private String tenKichThuoc;
        private Integer soLuongHeThong;
        private Integer soLuongThucTe;
        private Integer chenhLech;
        private String ghiChu;
    }
}
