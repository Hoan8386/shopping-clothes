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
public class TonKhoCuaHangId implements Serializable {

    @Column(name = "MaCuaHang", length = 50)
    private String maCuaHang;

    @Column(name = "MaChiTietSanPham", length = 50)
    private String maChiTietSanPham;
}
