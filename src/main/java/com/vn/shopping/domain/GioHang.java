package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "GioHang")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaGioHang")
    private Long maGioHang;

    @OneToOne
    @JoinColumn(name = "MaKhachHang", unique = true, nullable = false)
    private KhachHang khachHang;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChiTietGioHang> chiTietGioHangs;

    @PrePersist
    public void handleBeforeCreate() {
        this.ngayTao = LocalDateTime.now();
    }
}
