package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PhieuNhap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhap {

    @Id
    @Column(name = "MaPhieuNhap", length = 50)
    private String maPhieuNhap;

    @ManyToOne
    @JoinColumn(name = "MaNhaCungCap")
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "MaCuaHangNhan")
    private CuaHang cuaHangNhan;

    @Column(name = "NgayNhap")
    private LocalDateTime ngayNhap;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;
}
