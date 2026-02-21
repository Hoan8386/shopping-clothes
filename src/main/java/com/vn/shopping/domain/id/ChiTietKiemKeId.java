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
public class ChiTietKiemKeId implements Serializable {

    @Column(name = "MaKiemKe", length = 50)
    private String maKiemKe;

    @Column(name = "MaChiTietSanPham", length = 50)
    private String maChiTietSanPham;
}
