package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResDanhGiaSanPhamDTO {
    private Long id;
    private Long khachHangId;
    private String tenKhachHang;
    private Long sanPhamId;
    private String tenSanPham;
    private Long donHangId;
    private Integer soSao;
    private String ghiChu;
    private String hinhAnh;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
