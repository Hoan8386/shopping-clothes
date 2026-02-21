package com.vn.shopping.domain;

import com.vn.shopping.domain.id.KhuyenMaiTheoSanPhamId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KhuyenMaiTheoSanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMaiTheoSanPham {

    @EmbeddedId
    private KhuyenMaiTheoSanPhamId id;

    @ManyToOne
    @MapsId("maKM")
    @JoinColumn(name = "MaKM")
    private KhuyenMai khuyenMai;

    @ManyToOne
    @MapsId("maSanPham")
    @JoinColumn(name = "MaSanPham")
    private SanPham sanPham;

    @Column(name = "PhanTramGiam")
    private Float phanTramGiam;
}
