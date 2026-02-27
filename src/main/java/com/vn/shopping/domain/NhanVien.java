package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "NhanVien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNhanVien")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;

    @Column(name = "TenNhanVien", length = 255)
    private String tenNhanVien;

    @Column(name = "Email", length = 255)
    private String email;

    @Column(name = "SoDienThoai", length = 255)
    private String soDienThoai;

    @Column(name = "MatKhau", length = 255)
    private String matKhau;

    @Column(name = "NgayBatDauLam")
    private LocalDateTime ngayBatDauLam;

    @Column(name = "NgayKetThucLam")
    private LocalDateTime ngayKetThucLam;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "RefreshToken", columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties(value = { "permissions" })
    private Role role;
}
