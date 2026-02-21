package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MauSac")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MauSac {

    @Id
    @Column(name = "MaMauSac", length = 50)
    private String maMauSac;

    @Column(name = "TenMauSac", length = 50)
    private String tenMauSac;
}
