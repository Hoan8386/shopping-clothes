package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KichThuoc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KichThuoc {

    @Id
    @Column(name = "MaKichThuc", length = 50)
    private String maKichThuc;

    @Column(name = "TenKichThuoc", length = 50)
    private String tenKichThuoc;
}
