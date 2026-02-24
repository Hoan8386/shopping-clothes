package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "SanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {

    @Id
    @Column(name = "MaSanPham", length = 50)
    private String maSanPham;

    @Column(name = "TenSanPham", length = 255, nullable = false)
    private String tenSanPham;

    @Column(name = "GiaVon", precision = 18, scale = 2)
    private BigDecimal giaVon;

    @Column(name = "GiaBan", precision = 18, scale = 2)
    private BigDecimal giaBan;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;
}
