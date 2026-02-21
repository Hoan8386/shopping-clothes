package com.vn.shopping.domain.id;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChiTietDonHangTrucTiepId implements Serializable {

    @Column(name = "MaDonHang", length = 50)
    private String maDonHang;

    @Column(name = "MaChiTietSanPham", length = 50)
    private String maChiTietSanPham;
}
