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
public class ResDoiHangDTO {
    private Long id;
    private Long donHangId;
    private String ghiTru;
    private String trangThai;
    private Double tongTien;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private List<ChiTietDoiHangDTO> chiTietDoiHangs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChiTietDoiHangDTO {
        private Long id;
        private String ghiTru;
        private String trangThai;

        // Sản phẩm trả (từ đơn hàng cũ)
        private Long chiTietDonHangId;
        private String tenSanPhamTra;
        private String hinhAnhSanPhamTra;
        private String mauSacTra;
        private String kichThuocTra;
        private Double giaSanPhamTra;
        private Integer soLuongTra;

        // Sản phẩm đổi (sản phẩm mới)
        private Long chiTietSanPhamId;
        private String tenSanPhamDoi;
        private String hinhAnhSanPhamDoi;
        private String mauSacDoi;
        private String kichThuocDoi;
        private Double giaSanPhamDoi;

        // Chênh lệch giá
        private Double chenhLechGia;
    }
}
