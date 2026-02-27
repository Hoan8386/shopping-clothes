package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HinhAnh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class HinhAnh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHinhAnh")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChiTietSanPham")
    private ChiTietSanPham chiTietSanPham;

    @Column(name = "TenHinhAnh", length = 255)
    private String tenHinhAnh;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    public void handleBeforeCreate() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
