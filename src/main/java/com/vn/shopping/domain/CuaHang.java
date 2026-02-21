package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CuaHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuaHang {

    @Id
    @Column(name = "MaCuaHang", length = 50)
    private String maCuaHang;

    @Column(name = "TenCuaHang", length = 255, nullable = false)
    private String tenCuaHang;

    @Column(name = "DiaChi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Sdt", length = 20)
    private String sdt;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;
}
