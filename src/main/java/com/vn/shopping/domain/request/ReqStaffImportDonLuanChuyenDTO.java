package com.vn.shopping.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReqStaffImportDonLuanChuyenDTO {
    private Long chiTietSanPhamId;
    private Integer soLuong;
    private String tenDon;
    private String ghiTru;
}
