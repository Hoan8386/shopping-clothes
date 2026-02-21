package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DonLuanChuyen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonLuanChuyen {

    @Id
    @Column(name = "MaDonLuanChuyen", length = 50)
    private String maDonLuanChuyen;

    @ManyToOne
    @JoinColumn(name = "MaCuaHangDi")
    private CuaHang cuaHangDi;

    @ManyToOne
    @JoinColumn(name = "MaCuaHangDen")
    private CuaHang cuaHangDen;

    @ManyToOne
    @JoinColumn(name = "MaNhanVienTao")
    private NhanVien nhanVienTao;

    @ManyToOne
    @JoinColumn(name = "MaNhanVienNhan")
    private NhanVien nhanVienNhan;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;
}
