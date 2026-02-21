package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietSanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietSanPham {

    @Id
    @Column(name = "MaChiTietSanPham", length = 50)
    private String maChiTietSanPham;

    @ManyToOne
    @JoinColumn(name = "MaSanPham")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "MaKichThuoc")
    private KichThuoc kichThuoc;

    @ManyToOne
    @JoinColumn(name = "MaMauSac")
    private MauSac mauSac;

    @Column(name = "SKU", length = 100, unique = true)
    private String sku;
}
