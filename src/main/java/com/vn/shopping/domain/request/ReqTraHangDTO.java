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
    private List<ChiTietTraHangItem> chiTietTraHangs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietTraHangItem {
        private Long chiTietDonHangId;
        private String ghiTru;
    }
}
