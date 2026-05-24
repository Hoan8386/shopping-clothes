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
public class ReqTraHangDTO {
    private Long donHangId;
    private String lyDoTraHang;
    // 0 = Tiền mặt, 1 = Chuyển khoản
    private Integer phuongThucHoanTien;
    private String thongTinChuyenKhoan;
    private String paymentRef;
    private List<ChiTietTraHangItem> chiTietTraHangs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietTraHangItem {
        private Long chiTietDonHangId;
        private Integer soLuong;
        private String ghiTru;
    }
}
