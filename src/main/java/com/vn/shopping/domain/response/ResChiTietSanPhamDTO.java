package com.vn.shopping.domain.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResChiTietSanPhamDTO {
    private Long id;
    private Long maPhieuNhap;
    private Long maCuaHang;
    private Integer trangThai;
    private String moTa;
    private String ghiTru;
    private String tenSanPham;
    private String tenMauSac;
    private String tenKichThuoc;
}
