package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ThuongHieu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThuongHieu {

    @Id
    @Column(name = "MaThuongHieu", length = 50)
    private String maThuongHieu;

    @Column(name = "TenThuongHieu", length = 255)
    private String tenThuongHieu;
}
