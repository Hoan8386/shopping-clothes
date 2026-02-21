package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "KhuyenMai")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMai {

    @Id
    @Column(name = "MaKhuyenMai", length = 50)
    private String maKhuyenMai;

    @Column(name = "TenKhuyenMai", length = 255)
    private String tenKhuyenMai;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "TrangThai")
    private Boolean trangThai;
}
