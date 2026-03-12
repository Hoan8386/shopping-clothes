package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResCuaHangDTO {
    private Long id;
    private String tenCuaHang;
    private String diaChi;
    private String viTri;
    private Double latitude;
    private Double longitude;
    private String soDienThoai;
    private String email;
    private Integer trangThai;
}
