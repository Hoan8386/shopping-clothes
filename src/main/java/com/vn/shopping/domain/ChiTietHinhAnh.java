package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietHinhAnh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHinhAnh {

    @Id
    @Column(name = "MaChiTietHinhAnh", length = 50)
    private String maChiTietHinhAnh;

    @ManyToOne
    @JoinColumn(name = "MaHinhAnh")
    private HinhAnh hinhAnh;

    @ManyToOne
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;
}
