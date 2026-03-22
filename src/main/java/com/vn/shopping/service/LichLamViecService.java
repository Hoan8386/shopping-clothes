package com.vn.shopping.service;

import com.vn.shopping.domain.LichLamViec;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.repository.LichLamViecRepository;
import com.vn.shopping.repository.NhanVienRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class LichLamViecService {

    private final LichLamViecRepository lichLamViecRepository;
    private final NhanVienRepository nhanVienRepository;

    public LichLamViecService(LichLamViecRepository lichLamViecRepository,
            NhanVienRepository nhanVienRepository) {
        this.lichLamViecRepository = lichLamViecRepository;
        this.nhanVienRepository = nhanVienRepository;
    }

    public LichLamViec create(LichLamViec lichLamViec) {
        return lichLamViecRepository.save(lichLamViec);
    }

    public LichLamViec update(LichLamViec lichLamViec) {
        LichLamViec existing = lichLamViecRepository.findById(lichLamViec.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch làm việc: " + lichLamViec.getId()));
        existing.setNhanVien(lichLamViec.getNhanVien());
        existing.setNgayLamViec(lichLamViec.getNgayLamViec());
        existing.setTrangThai(lichLamViec.getTrangThai());
        existing.setJson(lichLamViec.getJson());
        return lichLamViecRepository.save(existing);
    }

    public void delete(long id) {
        lichLamViecRepository.deleteById(id);
    }

    public LichLamViec findById(long id) {
        return lichLamViecRepository.findById(id).orElse(null);
    }

    public List<LichLamViec> findAll() {
        return lichLamViecRepository.findAll();
    }

    public List<LichLamViec> findByNhanVienId(Long nhanVienId) {
        NhanVien nhanVien = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên: " + nhanVienId));
        return lichLamViecRepository.findByNhanVien(nhanVien);
    }

    /**
     * Import lịch làm việc từ file Excel (.xlsx).
     * Cột trong file: A=MaNhanVien, B=NgayLamViec (yyyy-MM-dd), C=TrangThai
     */
    public List<LichLamViec> importFromExcel(MultipartFile file) throws IOException {
        List<LichLamViec> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (InputStream is = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Bỏ qua header row (dòng đầu tiên)
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Bỏ qua dòng trống
                if (isRowEmpty(row)) continue;

                Cell maNhanVienCell = row.getCell(0);
                Cell ngayLamViecCell = row.getCell(1);
                Cell trangThaiCell = row.getCell(2);

                if (maNhanVienCell == null || ngayLamViecCell == null) continue;

                long maNhanVien = (long) getCellNumericValue(maNhanVienCell);
                NhanVien nhanVien = nhanVienRepository.findById(maNhanVien).orElse(null);
                if (nhanVien == null) continue;

                String ngayStr = getCellStringValue(ngayLamViecCell).trim();
                LocalDate ngayLamViec = LocalDate.parse(ngayStr, formatter);

                int trangThai = trangThaiCell != null ? (int) getCellNumericValue(trangThaiCell) : 1;

                LichLamViec llv = new LichLamViec();
                llv.setNhanVien(nhanVien);
                llv.setNgayLamViec(ngayLamViec);
                llv.setTrangThai(trangThai);

                result.add(lichLamViecRepository.save(llv));
            }
        }
        return result;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    private double getCellNumericValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) return cell.getNumericCellValue();
        if (cell.getCellType() == CellType.STRING) {
            try { return Double.parseDouble(cell.getStringCellValue()); } catch (NumberFormatException e) { return 0; }
        }
        return 0;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                yield String.valueOf((long) cell.getNumericCellValue());
            }
            default -> "";
        };
    }
}
