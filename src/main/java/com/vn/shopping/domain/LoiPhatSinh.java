package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoiPhatSinh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoiPhatSinh {

    @Id
    @Column(name = "MaLoi", length = 50)
    private String maLoi;

    @ManyToOne
    @JoinColumn(name = "MaLichLamViec")
    private LichLamViec lichLamViec;

    @Column(name = "MoTaLoi", columnDefinition = "TEXT")
    private String moTaLoi;
}
