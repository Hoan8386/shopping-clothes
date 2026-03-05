package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCapNhatDonHangDTO {

    private Long id;

    // Trạng thái mới: 0=đã đặt hàng, 1=đã nhận đơn, 2=đang đóng gói, 3=đã gửi,
    // 4=hủy, 5=đã nhận hàng
    private Integer trangThai;

    // Chỉ cho cập nhật khi trạng thái đơn hàng < 2 (chưa đóng gói)
    private String diaChi;

    private String sdt;
}
