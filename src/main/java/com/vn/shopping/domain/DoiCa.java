package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DoiCa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class DoiCa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDoiCa")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaLichLam")
    @JsonIgnoreProperties(value = { "json" })
    private LichLamViec lichLamViec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChiTietLich")
    private ChiTietLichLam chiTietLichLam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhanVienNhanCa")
    @JsonIgnoreProperties(value = { "refreshToken", "matKhau", "role" })
    private NhanVien nhanVienNhanCa;

    @Column(name = "LyDo", length = 500)
    private String lyDo;

    @Column(name = "PhanHoi", length = 500)
    private String phanHoi;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "Json", columnDefinition = "TEXT")
    private String json;

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
