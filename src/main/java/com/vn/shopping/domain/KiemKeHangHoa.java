package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "KiemKeHangHoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KiemKeHangHoa {

    @Id
    @Column(name = "MaKiemKe", length = 50)
    private String maKiemKe;

    @ManyToOne
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "TrangThai", length = 50)
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "MaLoaiKiemKe")
    private LoaiKiemKe loaiKiemKe;
}
