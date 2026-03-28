package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "LichLamViec")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class LichLamViec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichLam")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNhanVien")
    @JsonIgnoreProperties(value = { "refreshToken", "matKhau", "role" })
    private NhanVien nhanVien;

    @Column(name = "NgayLamViec")
    private LocalDate ngayLamViec;

    @Column(name = "TrangThai")
    private Integer trangThai;

    @Column(name = "Json", columnDefinition = "TEXT")
    private String json;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "lichLamViec", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("lichLamViec")
    private List<ChiTietLichLam> chiTiets;

    @PrePersist
    public void handleBeforeCreate() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
