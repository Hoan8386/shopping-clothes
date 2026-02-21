package com.vn.shopping.domain;

import com.vn.shopping.domain.id.ChiTietGioHangId;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietGioHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ChiTietGioHangId.class)
public class ChiTietGioHang {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaGioHang")
    private GioHang gioHang;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "SoLuong")
    private Integer soLuong;
}
