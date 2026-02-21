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
public class KhuyenMaiTheoSanPhamId implements Serializable {

    @Column(name = "MaKM", length = 50)
    private String maKM;

    @Column(name = "MaSanPham", length = 50)
    private String maSanPham;
}
