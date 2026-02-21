package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DonHangOnline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonHangOnline {

    @Id
    @Column(name = "MaDonHangOnline", length = 50)
    private String maDonHangOnline;

    @ManyToOne
    @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @Column(name = "NgayDat")
    private LocalDateTime ngayDat;

    @Column(name = "DiaChiGiao", columnDefinition = "TEXT")
    private String diaChiGiao;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "MaCuaHangXuLy")
    private CuaHang cuaHangXuLy;

    @ManyToOne
    @JoinColumn(name = "MaNhanVienXuLy")
    private NhanVien nhanVienXuLy;
}
