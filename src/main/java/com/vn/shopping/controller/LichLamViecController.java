package com.vn.shopping.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.LichLamViec;
import com.vn.shopping.domain.response.ResLichLamViecThangDTO;
import com.vn.shopping.service.LichLamViecService;
import com.vn.shopping.util.anotation.ApiMessage;
import com.vn.shopping.util.error.IdInvalidException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api/v1/lich-lam-viec")
public class LichLamViecController {

    private final LichLamViecService lichLamViecService;

    public LichLamViecController(LichLamViecService lichLamViecService) {
        this.lichLamViecService = lichLamViecService;
    }

    @GetMapping
    @ApiMessage("Lấy danh sách lịch làm việc")
    public ResponseEntity<List<LichLamViec>> getAll() {
        return ResponseEntity.ok(lichLamViecService.findAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Lấy lịch làm việc theo id")
    public ResponseEntity<LichLamViec> getById(@PathVariable("id") long id) throws IdInvalidException {
        LichLamViec lichLamViec = lichLamViecService.findById(id);
        if (lichLamViec == null) {
            throw new IdInvalidException("Không tìm thấy lịch làm việc: " + id);
        }
        return ResponseEntity.ok(lichLamViec);
    }

    @GetMapping("/nhan-vien/{nhanVienId}")
    @ApiMessage("Lấy lịch làm việc theo nhân viên")
    public ResponseEntity<List<LichLamViec>> getByNhanVien(@PathVariable("nhanVienId") Long nhanVienId) {
        return ResponseEntity.ok(lichLamViecService.findByNhanVienId(nhanVienId));
    }

    @GetMapping("/cua-hang/{cuaHangId}")
    @ApiMessage("Lấy lịch làm việc theo cửa hàng")
    public ResponseEntity<List<LichLamViec>> getByCuaHang(@PathVariable("cuaHangId") Long cuaHangId) {
        return ResponseEntity.ok(lichLamViecService.findByCuaHangId(cuaHangId));
    }

    @GetMapping("/cua-hang/{cuaHangId}/thang")
    @ApiMessage("Lấy lịch làm việc theo cửa hàng và tháng")
    public ResponseEntity<ResLichLamViecThangDTO> getByCuaHangAndMonth(
            @PathVariable("cuaHangId") Long cuaHangId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        List<LichLamViec> lichLamViecs = lichLamViecService.findByCuaHangAndMonth(cuaHangId, year, month);

        return ResponseEntity.ok(buildMonthlyResponse(cuaHangId, year, month, lichLamViecs));
    }

    private ResLichLamViecThangDTO buildMonthlyResponse(Long cuaHangId, int year, int month,
            List<LichLamViec> lichLamViecs) {

        Map<LocalDate, List<LichLamViec>> lichTheoNgay = new HashMap<>();
        for (LichLamViec lichLamViec : lichLamViecs) {
            lichTheoNgay.computeIfAbsent(lichLamViec.getNgayLamViec(), k -> new ArrayList<>()).add(lichLamViec);
        }

        List<ResLichLamViecThangDTO.NgayLichLamDTO> ngayLichLams = new ArrayList<>();

        lichTheoNgay.forEach((ngay, lichTrongNgay) -> {
            ResLichLamViecThangDTO.NgayLichLamDTO ngayDto = new ResLichLamViecThangDTO.NgayLichLamDTO();
            ngayDto.setNgayLamViec(ngay);
            Integer trangThaiNgay = resolveTrangThaiNgay(lichTrongNgay);
            ngayDto.setTrangThaiNgay(trangThaiNgay);
            if (Integer.valueOf(0).equals(trangThaiNgay)) {
                ngayDto.setChiTietNhanViens(Collections.emptyList());
            } else {
                ngayDto.setChiTietNhanViens(toChiTietNhanVienTrongNgay(lichTrongNgay));
            }

            ngayLichLams.add(ngayDto);
        });

        ResLichLamViecThangDTO response = new ResLichLamViecThangDTO();
        response.setCuaHangId(cuaHangId);
        response.setYear(year);
        response.setMonth(month);
        response.setNgayLichLams(ngayLichLams);

        return response;
    }

    private Integer resolveTrangThaiNgay(List<LichLamViec> lichTrongNgay) {
        if (lichTrongNgay == null || lichTrongNgay.isEmpty()) {
            return 1;
        }

        boolean hasFestival = lichTrongNgay.stream().anyMatch(lich -> Integer.valueOf(2).equals(lich.getTrangThai()));
        if (hasFestival) {
            return 2;
        }

        boolean hasHoliday = lichTrongNgay.stream().anyMatch(lich -> Integer.valueOf(0).equals(lich.getTrangThai()));
        if (hasHoliday) {
            return 0;
        }

        return 1;
    }

    private List<ResLichLamViecThangDTO.ChiTietNhanVienTrongNgayDTO> toChiTietNhanVienTrongNgay(
            List<LichLamViec> lichTrongNgay) {
        return lichTrongNgay.stream()
                .sorted(Comparator.comparing(l -> l.getNhanVien() != null ? l.getNhanVien().getId() : Long.MAX_VALUE))
                .map(lich -> {
                    ResLichLamViecThangDTO.ChiTietNhanVienTrongNgayDTO item = new ResLichLamViecThangDTO.ChiTietNhanVienTrongNgayDTO();
                    item.setLichLamViecId(lich.getId());
                    item.setTrangThaiLich(lich.getTrangThai());
                    item.setNhanVien(toNhanVienInfo(lich));
                    item.setChiTietCaLams(toChiTietCaLam(lich));
                    return item;
                })
                .toList();
    }

    private ResLichLamViecThangDTO.NhanVienInfoDTO toNhanVienInfo(LichLamViec lich) {
        ResLichLamViecThangDTO.NhanVienInfoDTO nhanVienInfoDTO = new ResLichLamViecThangDTO.NhanVienInfoDTO();
        if (lich.getNhanVien() == null) {
            return nhanVienInfoDTO;
        }

        nhanVienInfoDTO.setId(lich.getNhanVien().getId());
        nhanVienInfoDTO.setTenNhanVien(lich.getNhanVien().getTenNhanVien());
        nhanVienInfoDTO.setEmail(lich.getNhanVien().getEmail());
        nhanVienInfoDTO.setSoDienThoai(lich.getNhanVien().getSoDienThoai());
        return nhanVienInfoDTO;
    }

    private List<ResLichLamViecThangDTO.ChiTietCaLamDTO> toChiTietCaLam(LichLamViec lich) {
        if (lich.getChiTiets() == null || lich.getChiTiets().isEmpty()) {
            return new ArrayList<>();
        }

        return lich.getChiTiets().stream()
                .map(chiTiet -> {
                    ResLichLamViecThangDTO.ChiTietCaLamDTO caLamDTO = new ResLichLamViecThangDTO.ChiTietCaLamDTO();
                    caLamDTO.setId(chiTiet.getId());
                    caLamDTO.setTrangThai(chiTiet.getTrangThai());

                    ResLichLamViecThangDTO.CaLamInfoDTO caLamInfoDTO = new ResLichLamViecThangDTO.CaLamInfoDTO();
                    if (chiTiet.getCaLamViec() != null) {
                        caLamInfoDTO.setId(chiTiet.getCaLamViec().getId());
                        caLamInfoDTO.setTenCaLam(chiTiet.getCaLamViec().getTenCaLam());
                        caLamInfoDTO.setGioBatDau(chiTiet.getCaLamViec().getGioBatDau());
                        caLamInfoDTO.setGioKetThuc(chiTiet.getCaLamViec().getGioKetThuc());
                        caLamInfoDTO.setTrangThai(chiTiet.getCaLamViec().getTrangThai());
                    }
                    caLamDTO.setCaLamViec(caLamInfoDTO);

                    return caLamDTO;
                })
                .toList();
    }

    @PutMapping("/cua-hang/{cuaHangId}/ngay/trang-thai")
    @ApiMessage("Cập nhật trạng thái ngày làm việc rảnh/nghỉ/lễ")
    public ResponseEntity<Void> updateDayStatus(
            @PathVariable("cuaHangId") Long cuaHangId,
            @RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date,
            @RequestParam("status") int status) {
        lichLamViecService.updateDayStatus(cuaHangId, date, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cua-hang/{cuaHangId}/ngay/ca-lam-viec")
    @ApiMessage("Thêm nhân viên vào ca làm việc")
    public ResponseEntity<Void> addShift(
            @PathVariable("cuaHangId") Long cuaHangId,
            @RequestParam("nhanVienId") Long nhanVienId,
            @RequestParam("caLamViecId") Long caLamViecId,
            @RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date) {
        lichLamViecService.addShift(nhanVienId, date, caLamViecId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cua-hang/{cuaHangId}/ngay/ca-lam-viec")
    @ApiMessage("Xóa nhân viên khỏi ca làm việc")
    public ResponseEntity<Void> removeShift(
            @PathVariable("cuaHangId") Long cuaHangId,
            @RequestParam("nhanVienId") Long nhanVienId,
            @RequestParam("caLamViecId") Long caLamViecId,
            @RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date) {
        lichLamViecService.removeShift(nhanVienId, date, caLamViecId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @ApiMessage("Tạo lịch làm việc")
    public ResponseEntity<LichLamViec> create(@RequestBody LichLamViec lichLamViec) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lichLamViecService.create(lichLamViec));
    }

    @PutMapping
    @ApiMessage("Cập nhật lịch làm việc")
    public ResponseEntity<LichLamViec> update(@RequestBody LichLamViec lichLamViec) throws IdInvalidException {
        if (lichLamViec.getId() == null || lichLamViec.getId() == 0) {
            throw new IdInvalidException("Mã lịch làm việc không được để trống");
        }
        return ResponseEntity.ok(lichLamViecService.update(lichLamViec));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Xóa lịch làm việc")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        if (lichLamViecService.findById(id) == null) {
            throw new IdInvalidException("Không tìm thấy lịch làm việc: " + id);
        }
        lichLamViecService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Import lịch làm việc từ file Excel.
     * File Excel phải có 3 cột: MaNhanVien | NgayLamViec (yyyy-MM-dd) | MaCaLam
     */
    @PostMapping(value = "/cua-hang/{cuaHangId}/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Import lịch làm việc từ Excel")
    public ResponseEntity<List<LichLamViec>> importFromExcel(
            @PathVariable("cuaHangId") Long cuaHangId,
            @RequestParam("file") MultipartFile file) throws IdInvalidException {

        if (file.isEmpty()) {
            throw new IdInvalidException("File không được để trống");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            throw new IdInvalidException("Chỉ chấp nhận file .xlsx");
        }

        try {
            List<LichLamViec> imported = lichLamViecService.importFromExcel(cuaHangId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(imported);
        } catch (IOException e) {
            throw new IdInvalidException("Lỗi đọc file Excel: " + e.getMessage());
        }
    }

    /**
     * Tải file Excel mẫu để import lịch làm việc.
     */
    @GetMapping("/cua-hang/{cuaHangId}/download-template")
    @ApiMessage("Tải file Excel mẫu lịch làm việc")
    public ResponseEntity<Resource> downloadTemplate(
            @PathVariable("cuaHangId") Long cuaHangId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) throws IOException {
        byte[] bytes = lichLamViecService.downloadTemplateFromCuaHang(cuaHangId, year, month);
        ByteArrayResource resource = new ByteArrayResource(bytes);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        httpHeaders.setContentDispositionFormData("attachment", "lich-lam-viec-mau.xlsx");

        return ResponseEntity.ok().headers(httpHeaders).body(resource);
    }
}
