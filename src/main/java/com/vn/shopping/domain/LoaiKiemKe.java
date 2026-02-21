package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoaiKiemKe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoaiKiemKe {

    @Id
    @Column(name = "MaLoaiKiemKe", length = 50)
    private String maLoaiKiemKe;

    @Column(name = "TenLoai", length = 255)
    private String tenLoai;
}
