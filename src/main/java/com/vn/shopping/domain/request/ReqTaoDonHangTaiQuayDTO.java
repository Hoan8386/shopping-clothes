package com.vn.shopping.domain.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqTaoDonHangTaiQuayDTO {

    private String tenNguoiMua;

    // Nhân viên nhập số điện thoại để tìm và gắn khách hàng đã đăng ký.
    private String sdt;

    // Có thể truyền trực tiếp nếu FE đã lookup và chọn khách hàng.
    private Long khachHangId;

    // Mã khuyến mãi tùy chọn.
    private Long maKhuyenMaiHoaDon;
    private Long maKhuyenMaiDiem;

    // 0 = COD/Tien mat, 1 = VNPAY
    private Integer hinhThucDonHang;

    // Danh sách sản phẩm trong giỏ tạo đơn tại quầy.
    private List<ChiTietDonTaiQuayDTO> chiTietDonHangs;

    @Getter
    @Setter
    public static class ChiTietDonTaiQuayDTO {
        private Long chiTietSanPhamId;
        private Integer soLuong;
    }
}
