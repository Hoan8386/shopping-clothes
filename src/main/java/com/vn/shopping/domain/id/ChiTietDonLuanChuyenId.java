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
public class ChiTietDonLuanChuyenId implements Serializable {

    @Column(name = "MaDonLuanChuyen", length = 50)
    private String maDonLuanChuyen;

    @Column(name = "MaChiTietSanPham", length = 50)
    private String maChiTietSanPham;
}
