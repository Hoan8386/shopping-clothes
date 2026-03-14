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
public class ReqDoiHangDTO {
    private Long donHangId;
    private String ghiTru;
    private List<ChiTietDoiHangItem> chiTietDoiHangs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietDoiHangItem {
        private Long chiTietDonHangId; // sản phẩm trả (trong đơn hàng)
        private Long chiTietSanPhamId; // sản phẩm đổi (variant mới)
        private String ghiTru;
    }
}
