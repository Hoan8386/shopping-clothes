package com.vn.shopping.controller;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.vn.shopping.domain.LichLamViec;
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
    public ResponseEntity<List<LichLamViec>> getByCuaHangAndMonth(
            @PathVariable("cuaHangId") Long cuaHangId,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        return ResponseEntity.ok(lichLamViecService.findByCuaHangAndMonth(cuaHangId, year, month));
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
