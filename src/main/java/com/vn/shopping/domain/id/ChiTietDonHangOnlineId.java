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
public class ChiTietDonHangOnlineId implements Serializable {

    @Column(name = "MaDonHangOnline", length = 50)
    private String maDonHangOnline;

    @Column(name = "MaChiTietSanPham", length = 50)
    private String maChiTietSanPham;
}
