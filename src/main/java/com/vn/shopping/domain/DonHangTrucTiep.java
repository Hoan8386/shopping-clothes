package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DonHangTrucTiep")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonHangTrucTiep {

    @Id
    @Column(name = "MaDonHang", length = 50)
    private String maDonHang;

    @ManyToOne
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien;
}
