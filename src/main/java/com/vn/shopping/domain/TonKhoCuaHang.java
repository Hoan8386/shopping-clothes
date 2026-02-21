package com.vn.shopping.domain;

import com.vn.shopping.domain.id.TonKhoCuaHangId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TonKhoCuaHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TonKhoCuaHang {

    @EmbeddedId
    private TonKhoCuaHangId id;

    @ManyToOne
    @MapsId("maCuaHang")
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;

    @ManyToOne
    @MapsId("maChiTietSanPham")
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "SoLuongTon")
    private Integer soLuongTon;
}
