package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqTaoDonHangDTO {

    // Địa chỉ giao hàng (online bắt buộc, tại quầy có thể null)
    private String diaChi;

    // Mã cửa hàng (nhân viên tạo tại quầy thì truyền, online có thể null)
    private Long cuaHangId;

    // Mã khuyến mãi (tùy chọn)
    private Long maKhuyenMaiHoaDon;
    private Long maKhuyenMaiDiem;

    // Mã khách hàng (nhân viên tạo cho khách thì truyền, online tự lấy từ token)
    private Long khachHangId;
}
