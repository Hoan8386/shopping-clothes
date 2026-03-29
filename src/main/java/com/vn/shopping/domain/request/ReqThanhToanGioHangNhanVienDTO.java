package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqThanhToanGioHangNhanVienDTO {
    // 0 = COD/Tien mat, 1 = VNPAY
    private Integer hinhThucDonHang;
}
