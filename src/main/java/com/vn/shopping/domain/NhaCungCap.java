package com.vn.shopping.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NhaCungCap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap {

    @Id
    @Column(name = "MaNhaCungCap", length = 50)
    private String maNhaCungCap;

    @Column(name = "TenNhaCungCap", length = 255)
    private String tenNhaCungCap;

    @Column(name = "SoDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "DiaChi", columnDefinition = "TEXT")
    private String diaChi;
}
