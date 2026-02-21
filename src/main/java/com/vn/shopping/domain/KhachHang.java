package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KhachHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {

    @Id
    @Column(name = "MaKhachHang", length = 50)
    private String maKhachHang;

    @Column(name = "TenKhachHang", length = 255)
    private String tenKhachHang;

    @Column(name = "Sdt", length = 20, unique = true)
    private String sdt;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "DiemTichLuy")
    private Integer diemTichLuy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties(value = { "permissions" })
    private Role role;
}
