package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuTonKho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LichSuTonKho {

    @Id
    @Column(name = "MaLichSu", length = 50)
    private String maLichSu;

    @ManyToOne
    @JoinColumn(name = "MaCuaHang")
    private CuaHang cuaHang;

    @ManyToOne
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "SoLuongTruoc")
    private Integer soLuongTruoc;

    @Column(name = "SoLuongSau")
    private Integer soLuongSau;

    @Column(name = "SoLuongThayDoi")
    private Integer soLuongThayDoi;

    @Column(name = "LyDo", length = 255)
    private String lyDo;

    @Column(name = "MaNguon", length = 50)
    private String maNguon;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien")
    private NhanVien nhanVien;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;
}
