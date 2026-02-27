package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CuaHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CuaHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaCuaHang")
    private Long id;

    @Column(name = "TenCuaHang", length = 255)
    private String tenCuaHang;

    @Column(name = "DiaChi", length = 255)
    private String diaChi;

    @Column(name = "ViTri", length = 255)
    private String viTri;

    @Column(name = "SoDienThoai", length = 255)
    private String soDienThoai;

    @Column(name = "Email", length = 255)
    private String email;

    @Column(name = "TrangThai")
    private Integer trangThai;
}
