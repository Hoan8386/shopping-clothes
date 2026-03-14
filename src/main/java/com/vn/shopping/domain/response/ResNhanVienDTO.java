package com.vn.shopping.domain.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResNhanVienDTO {
    private Long id;
    private String tenNhanVien;
    private String email;
    private String soDienThoai;
    private LocalDateTime ngayBatDauLam;
    private LocalDateTime ngayKetThucLam;
    private Integer trangThai;
    private CuaHangInfo cuaHang;
    private RoleInfo role;

    @Getter
    @Setter
    public static class CuaHangInfo {
        private Long id;
        private String tenCuaHang;
        private String diaChi;
        private String soDienThoai;
        private String email;
        private Integer trangThai;
    }

    @Getter
    @Setter
    public static class RoleInfo {
        private Long id;
        private String name;
        private String description;
        private boolean active;
    }
}