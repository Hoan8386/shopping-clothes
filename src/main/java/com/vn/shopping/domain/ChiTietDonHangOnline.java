package com.vn.shopping.domain;

import com.vn.shopping.domain.id.ChiTietDonHangOnlineId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietDonHangOnline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietDonHangOnline {

    @EmbeddedId
    private ChiTietDonHangOnlineId id;

    @ManyToOne
    @MapsId("maDonHangOnline")
    @JoinColumn(name = "MaDonHangOnline")
    private DonHangOnline donHangOnline;

    @ManyToOne
    @MapsId("maChiTietSanPham")
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "SoLuong")
    private Integer soLuong;
}
