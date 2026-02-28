package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSanPham")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKieuSanPham")
    private KieuSanPham kieuSanPham;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSuuTap")
    private BoSuuTap boSuuTap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaThuongHieu")
    private ThuongHieu thuongHieu;

    @Column(name = "TenSanPham", length = 255)
    private String tenSanPham;

    @Column(name = "GiaVon")
    private Double giaVon;

    @Column(name = "GiaBan")
    private Double giaBan;

    @Column(name = "GiaGiam")
    private Integer giaGiam;

    @Column(name = "HinhAnhChinh", length = 255)
    private String hinhAnhChinh;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "TrangThai")
    private Integer trangThai;

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
