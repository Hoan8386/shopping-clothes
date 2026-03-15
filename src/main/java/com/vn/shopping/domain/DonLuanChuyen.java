package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DonLuanChuyen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class DonLuanChuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDonLuanChuyen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CuaHangDat", referencedColumnName = "MaCuaHang")
    private CuaHang cuaHangDat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CuaHangGui", referencedColumnName = "MaCuaHang")
    private CuaHang cuaHangGui;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLoaiDonLuanChuyen", referencedColumnName = "MaLoaiDonLuanChuyen")
    private LoaiDonLuanChuyen loaiDonLuanChuyen;

    @Column(name = "TenDon", length = 255)
    private String tenDon;

    @Column(name = "ThoiGianGiao")
    private LocalDateTime thoiGianGiao;

    @Column(name = "ThoiGianNhan")
    private LocalDateTime thoiGianNhan;

    @Column(name = "TrangThai")
    private Integer trangThai;

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

    @OneToMany(mappedBy = "donLuanChuyen", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChiTietDonLuanChuyen> chiTietDonLuanChuyens;

    @PrePersist
    public void handleBeforeCreate() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
