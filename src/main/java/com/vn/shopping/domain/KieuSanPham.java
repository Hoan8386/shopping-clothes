package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "KieuSanPham")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class KieuSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKieuSanPham")
    private Long id;

    @Column(name = "TenKieuSanPham", length = 255)
    private String tenKieuSanPham;

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
