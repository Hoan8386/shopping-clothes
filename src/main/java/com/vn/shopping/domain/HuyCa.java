package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HuyCa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HuyCa {

    @Id
    @Column(name = "MaHuyCa", length = 50)
    private String maHuyCa;

    @ManyToOne
    @JoinColumn(name = "MaLichLamViec")
    private LichLamViec lichLamViec;

    @Column(name = "LyDo", columnDefinition = "TEXT")
    private String lyDo;

    @Column(name = "NgayHuy")
    private LocalDateTime ngayHuy;
}
