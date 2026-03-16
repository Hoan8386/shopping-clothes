package com.vn.shopping.domain.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReqKiemKeHangHoaDTO {
    private Long id;
    private Long loaiKiemKeId;
    private Long cuaHangId;
    private String tenPhieuKiemKe;
    private String ghiChu;
    private LocalDateTime ngayKiemKe;
    private List<ReqChiTietKiemKeDTO> chiTietKiemKes;
}
