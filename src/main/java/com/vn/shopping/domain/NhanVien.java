package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "TenNhanVien", length = 255)
    private String tenNhanVien;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Sdt", length = 20)
    private String sdt;

    @Enumerated(EnumType.STRING)
    @Column(name = "ChucVu", length = 50)
    private ChucVu chucVu;

    @Column(name = "RefreshToken", columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties(value = { "permissions" })
    private Role role;
}
