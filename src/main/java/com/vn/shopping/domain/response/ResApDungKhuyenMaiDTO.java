package com.vn.shopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResApDungKhuyenMaiDTO {

    private Integer tongTienGoc;

    // Khuyến mãi theo hóa đơn
    private Long maKhuyenMaiHoaDon;
    private String tenKhuyenMaiHoaDon;
    private Integer tienGiamHoaDon;

    // Khuyến mãi theo điểm
    private Long maKhuyenMaiDiem;
    private String tenKhuyenMaiDiem;
    private Integer tienGiamDiem;

    // Tổng kết
    private Integer tongTienGiam;
    private Integer tongTienTra;
}
