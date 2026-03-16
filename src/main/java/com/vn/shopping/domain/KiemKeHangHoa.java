package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "KiemKeHangHoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class KiemKeHangHoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKiemKe")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLoaiKiemKe")
    private LoaiKiemKe loaiKiemKe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhanVienTao")
    private NhanVien nhanVienTao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhanVienDuyet")
    private NhanVien nhanVienDuyet;

    @Column(name = "TenPhieuKiemKe", length = 255)
    private String tenPhieuKiemKe;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    @Column(name = "LyDoYeuCauKiemKeLai", length = 255)
    private String lyDoYeuCauKiemKeLai;

    @Column(name = "NgayKiemKe")
    private LocalDateTime ngayKiemKe;

    @Column(name = "NgayXacNhan")
    private LocalDateTime ngayXacNhan;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "kiemKeHangHoa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChiTietKiemKe> chiTietKiemKes;

    @PrePersist
    public void handleBeforeCreate() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
