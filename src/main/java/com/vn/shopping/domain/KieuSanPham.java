package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "KieuSanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KieuSanPham {

    @Id
    @Column(name = "MaKieuSanPham", length = 50)
    private String maKieuSanPham;

    @Column(name = "TenKieuSanPham", length = 255)
    private String tenKieuSanPham;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;
}
