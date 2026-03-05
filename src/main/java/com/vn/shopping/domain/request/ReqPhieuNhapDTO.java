package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReqPhieuNhapDTO {
    private Long id;
    private String tenPhieuNhap;
    private Long cuaHangId;
    private Long nhaCungCapId;
    private Integer trangThai;
    private LocalDateTime ngayGiaoHang;
    private LocalDateTime ngayNhanHang;
}
