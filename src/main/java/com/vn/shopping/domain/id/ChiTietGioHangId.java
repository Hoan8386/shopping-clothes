package com.vn.shopping.domain.id;

import java.io.Serializable;
import java.util.Objects;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietGioHangId implements Serializable {
    private Long gioHang;
    private String chiTietSanPham;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ChiTietGioHangId that = (ChiTietGioHangId) o;
        return Objects.equals(gioHang, that.gioHang) && Objects.equals(chiTietSanPham, that.chiTietSanPham);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gioHang, chiTietSanPham);
    }
}
