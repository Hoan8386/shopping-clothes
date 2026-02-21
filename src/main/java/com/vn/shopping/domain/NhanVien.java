package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vn.shopping.domain.enums.ChucVu;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NhanVien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {

    @Id
    @Column(name = "MaNhanVien", length = 50)
    private String maNhanVien;

    @Column(name = "TenNhanVien", length = 255)
    private String tenNhanVien;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Sdt", length = 20)
    private String sdt;

    @ManyToOne
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;

    @Enumerated(EnumType.STRING)
    @Column(name = "ChucVu", length = 50)
    private ChucVu chucVu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties(value = { "permissions" })
    private Role role;
}
