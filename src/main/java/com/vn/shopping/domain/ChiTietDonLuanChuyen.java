package com.vn.shopping.domain;

import com.vn.shopping.domain.id.ChiTietDonLuanChuyenId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietDonLuanChuyen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonLuanChuyen {

    @EmbeddedId
    private ChiTietDonLuanChuyenId id;

    @ManyToOne
    @MapsId("maDonLuanChuyen")
    @JoinColumn(name = "MaDonLuanChuyen")
    private DonLuanChuyen donLuanChuyen;

    @ManyToOne
    @MapsId("maChiTietSanPham")
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "SoLuong")
    private Integer soLuong;
}
