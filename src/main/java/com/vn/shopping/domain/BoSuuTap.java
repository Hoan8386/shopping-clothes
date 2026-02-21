package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BoSuuTap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoSuuTap {

    @Id
    @Column(name = "MaSuuTap", length = 50)
    private String maSuuTap;

    @Column(name = "TenSuuTap", length = 255)
    private String tenSuuTap;

    @Column(name = "MoTa", columnDefinition = "TEXT")
    private String moTa;
}
