package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KhachHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKhachHang")
    private Long id;

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

    @Column(name = "RefreshToken", columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties(value = { "permissions" })
    private Role role;
}
