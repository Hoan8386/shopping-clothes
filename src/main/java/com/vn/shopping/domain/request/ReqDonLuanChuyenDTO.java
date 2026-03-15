package com.vn.shopping.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqDonLuanChuyenDTO {
    private Long cuaHangDatId;
    private Long cuaHangGuiId;
    private Long loaiDonLuanChuyenId;
    private String tenDon;
    private String ghiTru;
    private List<ChiTietDonLuanChuyenItem> chiTietDonLuanChuyens;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietDonLuanChuyenItem {
        private Long chiTietSanPhamId;
        private Integer soLuong;
        private String ghiTru;
    }
}
