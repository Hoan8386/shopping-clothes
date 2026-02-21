package com.vn.shopping.domain;

import com.vn.shopping.domain.id.ChiTietPhieuNhapId;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ChiTietPhieuNhap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuNhap {

    @EmbeddedId
    private ChiTietPhieuNhapId id;

    @ManyToOne
    @MapsId("maPhieuNhap")
    @JoinColumn(name = "MaPhieuNhap")
    private PhieuNhap phieuNhap;

    @ManyToOne
    @MapsId("maChiTietSanPham")
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGiaNhap", precision = 18, scale = 2)
    private BigDecimal donGiaNhap;
}
