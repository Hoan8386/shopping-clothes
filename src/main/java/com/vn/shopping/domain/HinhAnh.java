package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HinhAnh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HinhAnh {

    @Id
    @Column(name = "MaHinhAnh", length = 50)
    private String maHinhAnh;

    @Column(name = "DuongDan", columnDefinition = "TEXT")
    private String duongDan;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;
}
