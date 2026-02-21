package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KhuyenMaiTheoDiem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMaiTheoDiem {

    @Id
    @Column(name = "MaKM", length = 50)
    private String maKM;

    @ManyToOne
    @JoinColumn(name = "MaKM", insertable = false, updatable = false)
    private KhuyenMai khuyenMai;

    @Column(name = "DiemYeuCau")
    private Integer diemYeuCau;

    @Column(name = "TiLeGiam")
    private Float tiLeGiam;
}
