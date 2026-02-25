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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKichThuoc")
    private long id;

    @Column(name = "TenKichThuoc", length = 50)
    private String tenKichThuoc;
}
