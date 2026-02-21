package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "DangKyLichLam")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DangKyLichLam {

    @Id
    @Column(name = "MaDangKy", length = 50)
    private String maDangKy;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    @Column(name = "NgayDangKy")
    private LocalDate ngayDangKy;

    @Column(name = "CaDangKy", length = 50)
    private String caDangKy;
}
