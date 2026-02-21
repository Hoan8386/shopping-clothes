package com.vn.shopping.domain;

import com.vn.shopping.domain.id.ChiTietDonHangTrucTiepId;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ChiTietDonHangTrucTiep")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHangTrucTiep {

    @EmbeddedId
    private ChiTietDonHangTrucTiepId id;

    @ManyToOne
    @MapsId("maDonHang")
    @JoinColumn(name = "MaDonHang")
    private DonHangTrucTiep donHangTrucTiep;

    @ManyToOne
    @MapsId("maChiTietSanPham")
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "GiaBan", precision = 18, scale = 2)
    private BigDecimal giaBan;
}
