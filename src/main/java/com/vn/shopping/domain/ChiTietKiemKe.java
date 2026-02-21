package com.vn.shopping.domain;

import com.vn.shopping.domain.id.ChiTietKiemKeId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietKiemKe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietKiemKe {

    @EmbeddedId
    private ChiTietKiemKeId id;

    @ManyToOne
    @MapsId("maKiemKe")
    @JoinColumn(name = "MaKiemKe")
    private KiemKeHangHoa kiemKeHangHoa;

    @ManyToOne
    @MapsId("maChiTietSanPham")
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "SoLuongHeThong")
    private Integer soLuongHeThong;

    @Column(name = "SoLuongThucTe")
    private Integer soLuongThucTe;

    @Column(name = "GhiTru", columnDefinition = "TEXT")
    private String ghiTru;
}
