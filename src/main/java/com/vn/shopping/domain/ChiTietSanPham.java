package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChiTietSanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ChiTietSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTietSanPham")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSanPham")
    private SanPham sanPham;

    @Column(name = "MaPhieuNhap")
    private Long maPhieuNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaMauSac")
    private MauSac mauSac;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKichThuoc")
    private KichThuoc kichThuoc;

    @Column(name = "MaCuaHang")
    private Long maCuaHang;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "GhiTru", length = 255)
    private String ghiTru;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    public void handleBeforeCreate() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
