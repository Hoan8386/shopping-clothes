package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ChiTietDonLuanChuyen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ChiTietDonLuanChuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTietDonLuanChuyen")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaDonLuanChuyen", referencedColumnName = "MaDonLuanChuyen")
    private DonLuanChuyen donLuanChuyen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChiTietSanPham", referencedColumnName = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "HinhAnh", length = 500)
    private String hinhAnh;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "TrangThaiHienThi")
    private Integer trangThaiHienThi;

    @Column(name = "GhiTru", length = 255)
    private String ghiTru;

    @Column(name = "GhiTruKiemHang", length = 255)
    private String ghiTruKiemHang;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "Json", columnDefinition = "TEXT")
    private String json;

    @PrePersist
    public void handleBeforeCreate() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
