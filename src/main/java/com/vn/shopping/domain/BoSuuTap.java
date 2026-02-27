package com.vn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "BoSuuTap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class BoSuuTap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSuuTap")
    private Long id;

    @Column(name = "TenSuuTap", length = 255)
    private String tenSuuTap;

    @Column(name = "MoTa", length = 255)
    private String moTa;

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
