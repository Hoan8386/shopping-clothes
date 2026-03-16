package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqTaoDonHangDTO {

    // Địa chỉ giao hàng (online bắt buộc, tại quầy có thể null)
    private String diaChi;

    // Số điện thoại nhận hàng
    private String sdt;

    // Mã cửa hàng (nhân viên tạo tại quầy thì truyền, online có thể null)
    private Long cuaHangId;

    // Mã khuyến mãi (tùy chọn)
    private Long maKhuyenMaiHoaDon;
    private Long maKhuyenMaiDiem;

    // Hình thức thanh toán: 0 = COD/Tiền mặt, 1 = VNPAY
    private Integer hinhThucDonHang;
}
