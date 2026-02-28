package com.vn.shopping.domain.response;

import java.time.LocalDateTime;
import java.util.List;

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
    private String tenCuaHang;
    private Integer soLuong;
    private Integer trangThai;
    private String moTa;
    private String ghiTru;
    private String tenSanPham;
    private String tenMauSac;
    private String tenKichThuoc;
    private List<String> hinhAnhUrls;
}
