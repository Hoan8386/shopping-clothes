package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResInventorySuggestionDTO {
    private String status;
    private Integer nearOutThreshold;
    private Long cuaHangId;
    private List<Item> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private Long chiTietSanPhamId;
        private Long sanPhamId;
        private String tenSanPham;
        private String tenMauSac;
        private String tenKichThuoc;
        private Long maCuaHang;
        private String tenCuaHang;
        private Integer soLuong;
        private String trangThaiTonKho;
    }
}
