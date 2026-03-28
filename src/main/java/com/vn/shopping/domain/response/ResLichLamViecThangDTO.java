package com.vn.shopping.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ResLichLamViecThangDTO {
    private Long cuaHangId;
    private Integer year;
    private Integer month;
    private List<NgayLichLamDTO> ngayLichLams;

    @Getter
    @Setter
    public static class NgayLichLamDTO {
        private LocalDate ngayLamViec;
        private Integer trangThaiNgay;
        private List<ChiTietNhanVienTrongNgayDTO> chiTietNhanViens;
    }

    @Getter
    @Setter
    public static class ChiTietNhanVienTrongNgayDTO {
        private Long lichLamViecId;
        private Integer trangThaiLich;
        private NhanVienInfoDTO nhanVien;
        private List<ChiTietCaLamDTO> chiTietCaLams;
    }

    @Getter
    @Setter
    public static class NhanVienInfoDTO {
        private Long id;
        private String tenNhanVien;
        private String email;
        private String soDienThoai;
    }

    @Getter
    @Setter
    public static class ChiTietCaLamDTO {
        private Long id;
        private Integer trangThai;
        private CaLamInfoDTO caLamViec;
    }

    @Getter
    @Setter
    public static class CaLamInfoDTO {
        private Long id;
        private String tenCaLam;
        private LocalTime gioBatDau;
        private LocalTime gioKetThuc;
        private Integer trangThai;
    }
}