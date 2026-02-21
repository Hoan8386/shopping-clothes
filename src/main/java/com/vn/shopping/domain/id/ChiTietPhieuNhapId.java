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
public class ChiTietPhieuNhapId implements Serializable {

    @Column(name = "MaPhieuNhap", length = 50)
    private String maPhieuNhap;

    @Column(name = "MaChiTietSanPham", length = 50)
    private String maChiTietSanPham;
}
