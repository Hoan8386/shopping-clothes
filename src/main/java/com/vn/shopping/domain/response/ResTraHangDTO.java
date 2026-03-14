package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResTraHangDTO {
    private Long id;
    private Long donHangId;
    private String lyDoTraHang;
    private String trangThai;
    private Double tongTien;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private List<ChiTietTraHangDTO> chiTietTraHangs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietTraHangDTO {
        private Long id;
        private Long chiTietDonHangId;
        private String tenSanPham;
        private String hinhAnhChinh;
        private String tenMauSac;
        private String tenKichThuoc;
        private Double giaSanPham;
        private Integer soLuong;
        private Double thanhTien;
        private String ghiTru;
        private String trangThai;
    }
}
