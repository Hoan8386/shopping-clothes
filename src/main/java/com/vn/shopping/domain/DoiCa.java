package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DoiCa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoiCa {

    @Id
    @Column(name = "MaDoiCa", length = 50)
    private String maDoiCa;

    @ManyToOne
    @JoinColumn(name = "MaLichLamViec")
    private LichLamViec lichLamViec;

    @ManyToOne
    @JoinColumn(name = "MaNhanVienThayThe")
    private NhanVien nhanVienThayThe;

    @Column(name = "GhiTru", columnDefinition = "TEXT")
    private String ghiTru;
}
