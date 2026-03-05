package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChiTietPhieuNhapDTO {
    private Long id;
    private Long phieuNhapId;
    private Long chiTietSanPhamId;
    private Integer soLuong;
    private String ghiTru;
    private String ghiTruKiemHang;
    private Integer trangThai;
}
