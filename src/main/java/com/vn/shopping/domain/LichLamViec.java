package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "LichLamViec")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LichLamViec {

    @Id
    @Column(name = "MaLichLamViec", length = 50)
    private String maLichLamViec;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    @Column(name = "Ngay")
    private LocalDate ngay;

    @Column(name = "Ca", length = 50)
    private String ca;

    @ManyToOne
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;
}
