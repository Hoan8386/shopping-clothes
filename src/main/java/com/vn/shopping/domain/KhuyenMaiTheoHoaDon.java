package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "KhuyenMaiTheoHoaDon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMaiTheoHoaDon {

    @Id
    @Column(name = "MaKM", length = 50)
    private String maKM;

    @ManyToOne
    @JoinColumn(name = "MaKM", insertable = false, updatable = false)
    private KhuyenMai khuyenMai;

    @Column(name = "GiaTriToiThieu", precision = 18, scale = 2)
    private BigDecimal giaTriToiThieu;

    @Column(name = "SoTienGiam", precision = 18, scale = 2)
    private BigDecimal soTienGiam;
}
