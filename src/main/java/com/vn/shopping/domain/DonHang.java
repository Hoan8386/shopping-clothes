package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DonHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDon")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhachHang")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    @Column(name = "MaKhuyenMaiHoaDon")
    private Long maKhuyenMaiHoaDon;

    @Column(name = "MaKhuyenMaiDiem")
    private Long maKhuyenMaiDiem;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "TongTien")
    private Integer tongTien;

    @Column(name = "TienGiam")
    private Integer tienGiam;

    @Column(name = "TongTienGiam")
    private Integer tongTienGiam;

    @Column(name = "TongTienTra")
    private Integer tongTienTra;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "TrangThaiThanhToan")
    private Integer trangThaiThanhToan;

    @Column(name = "HinhThucDonHang")
    private Integer hinhThucDonHang;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChiTietDonHang> chiTietDonHangs;

    @PrePersist
    public void handleBeforeCreate() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
