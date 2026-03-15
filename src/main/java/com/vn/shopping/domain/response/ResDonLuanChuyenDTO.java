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
public class ResDonLuanChuyenDTO {
    private Long id;
    private String tenDon;
    private String ghiTru;
    private String ghiTruKiemHang;
    private String trangThai;
    private LocalDateTime thoiGianGiao;
    private LocalDateTime thoiGianNhan;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    // Cửa hàng đặt (nhận hàng)
    private Long cuaHangDatId;
    private String tenCuaHangDat;

    // Cửa hàng gửi
    private Long cuaHangGuiId;
    private String tenCuaHangGui;

    // Loại đơn luân chuyển
    private Long loaiDonLuanChuyenId;
    private String tenLoaiDonLuanChuyen;

    private List<ChiTietDonLuanChuyenDTO> chiTietDonLuanChuyens;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietDonLuanChuyenDTO {
        private Long id;
        private Long chiTietSanPhamId;
        private String tenSanPham;
        private String hinhAnhSanPham;
        private String mauSac;
        private String kichThuoc;
        private String hinhAnh;
        private Integer soLuong;
        private String trangThai;
        private String ghiTru;
        private String ghiTruKiemHang;
    }
}
