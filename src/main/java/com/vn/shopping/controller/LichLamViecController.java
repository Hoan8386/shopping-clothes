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
     * File Excel phải có 3 cột: MaNhanVien | NgayLamViec (yyyy-MM-dd) | TrangThai
     */
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Import lịch làm việc từ Excel")
    public ResponseEntity<List<LichLamViec>> importFromExcel(
            @RequestParam("file") MultipartFile file) throws IdInvalidException {

        if (file.isEmpty()) {
            throw new IdInvalidException("File không được để trống");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            throw new IdInvalidException("Chỉ chấp nhận file .xlsx");
        }

        try {
            List<LichLamViec> imported = lichLamViecService.importFromExcel(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(imported);
        } catch (IOException e) {
            throw new IdInvalidException("Lỗi đọc file Excel: " + e.getMessage());
        }
    }

    /**
     * Tải file Excel mẫu để import lịch làm việc.
     */
    @GetMapping("/download-template")
    @ApiMessage("Tải file Excel mẫu lịch làm việc")
    public ResponseEntity<Resource> downloadTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("LichLamViec");

            // Header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Data style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "MaNhanVien (*)", "NgayLamViec (*) yyyy-MM-dd", "TrangThai (1=Đang làm, 0=Nghỉ)" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 7000);
            }

            // Create sample data rows
            String[][] sampleData = {
                    { "1", "2026-03-18", "1" },
                    { "2", "2026-03-18", "1" },
                    { "3", "2026-03-19", "1" }
            };
            for (int i = 0; i < sampleData.length; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < sampleData[i].length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(sampleData[i][j]);
                    cell.setCellStyle(dataStyle);
                }
            }

            workbook.write(out);
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            httpHeaders.setContentDispositionFormData("attachment", "lich-lam-viec-mau.xlsx");

            return ResponseEntity.ok().headers(httpHeaders).body(resource);
        }
    }
}
