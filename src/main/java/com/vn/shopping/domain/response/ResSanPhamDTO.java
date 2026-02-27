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
public class ResSanPhamDTO {
    private Long id;
    private String tenSanPham;
    private Double giaVon;
    private Double giaBan;
    private Integer giaGiam;
    private String hinhAnhChinh;
    private String moTa;
    private Integer trangThai;
    private String tenKieuSanPham;
    private String tenBoSuuTap;
    private String tenThuongHieu;
}
