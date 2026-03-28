package com.vn.shopping.service;

import com.vn.shopping.domain.CaLamViec;
import com.vn.shopping.domain.ChiTietLichLam;
import com.vn.shopping.domain.LichLamViec;
import com.vn.shopping.domain.NhanVien;
import com.vn.shopping.repository.CaLamViecRepository;
import com.vn.shopping.repository.ChiTietLichLamRepository;
import com.vn.shopping.repository.CuaHangRepository;
import com.vn.shopping.repository.LichLamViecRepository;
import com.vn.shopping.repository.NhanVienRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

@Service
public class LichLamViecService {

    private final LichLamViecRepository lichLamViecRepository;
    private final NhanVienRepository nhanVienRepository;
    private final CaLamViecRepository caLamViecRepository;
    private final ChiTietLichLamRepository chiTietLichLamRepository;
    private final CuaHangRepository cuaHangRepository;
    private final com.vn.shopping.repository.DoiCaRepository doiCaRepository;
    private final com.vn.shopping.repository.LoiPhatSinhRepository loiPhatSinhRepository;

    public LichLamViecService(LichLamViecRepository lichLamViecRepository,
            NhanVienRepository nhanVienRepository,
            CaLamViecRepository caLamViecRepository,
            ChiTietLichLamRepository chiTietLichLamRepository,
            CuaHangRepository cuaHangRepository,
            com.vn.shopping.repository.DoiCaRepository doiCaRepository,
            com.vn.shopping.repository.LoiPhatSinhRepository loiPhatSinhRepository) {
        this.lichLamViecRepository = lichLamViecRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.caLamViecRepository = caLamViecRepository;
        this.chiTietLichLamRepository = chiTietLichLamRepository;
        this.cuaHangRepository = cuaHangRepository;
        this.doiCaRepository = doiCaRepository;
        this.loiPhatSinhRepository = loiPhatSinhRepository;
    }

    private Long resolveCuaHangId(Long providedId) {
        if (providedId == null || providedId == 0) {
            String email = com.vn.shopping.util.SecurityUtil.getCurrentUserLogin().orElse("");
            if (!email.isEmpty()) {
                NhanVien nv = nhanVienRepository.findByEmail(email).orElse(null);
                if (nv != null && nv.getCuaHang() != null) {
                    return nv.getCuaHang().getId();
                }
            }
        }
        return providedId;
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

    public List<LichLamViec> findByCuaHangId(Long cuaHangId) {
        return lichLamViecRepository.findByCuaHangId(cuaHangId);
    }

    public List<LichLamViec> findByCuaHangAndMonth(Long cuaHangId, int year, int month) {
        Long resolvedId = resolveCuaHangId(cuaHangId);
        LocalDate tuNgay = LocalDate.of(year, month, 1);
        LocalDate denNgay = tuNgay.withDayOfMonth(tuNgay.lengthOfMonth());
        List<LichLamViec> storeSchedules = lichLamViecRepository.findByCuaHangIdAndDateRange(resolvedId, tuNgay,
                denNgay);

        if (isCurrentUserAdmin()) {
            return storeSchedules;
        }

        NhanVien currentNhanVien = getCurrentNhanVien()
                .orElseThrow(() -> new RuntimeException("Không xác định được nhân viên hiện tại"));

        return storeSchedules.stream()
                .filter(item -> item.getNhanVien() != null
                        && item.getNhanVien().getId() != null
                        && item.getNhanVien().getId().equals(currentNhanVien.getId()))
                .toList();
    }

    private boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            boolean hasAdminAuthority = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority() == null ? "" : authority.getAuthority().toUpperCase())
                    .anyMatch(authority -> "ROLE_ADMIN".equals(authority) || "ADMIN".equals(authority));

            if (hasAdminAuthority) {
                return true;
            }
        }

        // Fallback: read role from employee record resolved from current login.
        return getCurrentNhanVien()
                .map(nv -> nv.getRole() != null
                        && nv.getRole().getName() != null
                        && "ADMIN".equalsIgnoreCase(nv.getRole().getName()))
                .orElse(false);
    }

    private java.util.Optional<NhanVien> getCurrentNhanVien() {
        String email = com.vn.shopping.util.SecurityUtil.getCurrentUserLogin().orElse("");
        if (email.isEmpty()) {
            return java.util.Optional.empty();
        }
        return nhanVienRepository.findByEmail(email);
    }

    @Transactional
    public void updateDayStatus(Long cuaHangId, LocalDate date, int newStatus) {
        Long resolvedId = resolveCuaHangId(cuaHangId);

        List<NhanVien> activeEmployees = nhanVienRepository.findByCuaHang_Id(resolvedId).stream()
                .filter(nv -> nv.getTrangThai() == 1)
                .toList();

        if (newStatus == 0) {
            for (NhanVien nhanVien : activeEmployees) {
                List<LichLamViec> existingLichs = lichLamViecRepository.findByNhanVienAndNgayLamViec(nhanVien, date);
                LichLamViec llv = existingLichs.isEmpty() ? null : existingLichs.get(0);

                if (llv == null) {
                    llv = new LichLamViec();
                    llv.setNhanVien(nhanVien);
                    llv.setNgayLamViec(date);
                }

                List<ChiTietLichLam> chiTiets = chiTietLichLamRepository.findByLichLamViec(llv);
                if (!chiTiets.isEmpty()) {
                    Set<Long> chiTietIds = new HashSet<>();
                    for (ChiTietLichLam chiTiet : chiTiets) {
                        chiTietIds.add(chiTiet.getId());
                    }

                    List<com.vn.shopping.domain.DoiCa> doiCas = doiCaRepository.findByLichLamViec(llv).stream()
                            .filter(d -> d.getChiTietLichLam() != null
                                    && chiTietIds.contains(d.getChiTietLichLam().getId()))
                            .toList();
                    if (!doiCas.isEmpty()) {
                        doiCaRepository.deleteAll(doiCas);
                    }

                    List<com.vn.shopping.domain.LoiPhatSinh> loiPhatSinhs = loiPhatSinhRepository
                            .findByLichLamViec(llv).stream()
                            .filter(l -> l.getChiTietLichLam() != null
                                    && chiTietIds.contains(l.getChiTietLichLam().getId()))
                            .toList();
                    if (!loiPhatSinhs.isEmpty()) {
                        loiPhatSinhRepository.deleteAll(loiPhatSinhs);
                    }

                    chiTietLichLamRepository.deleteAll(chiTiets);
                }

                llv.setChiTiets(new ArrayList<>());
                llv.setTrangThai(0);
                llv.setJson("{\"isHoliday\": true, \"isFestival\": false}");
                lichLamViecRepository.save(llv);
            }
            return;
        }

        for (NhanVien nhanVien : activeEmployees) {
            List<LichLamViec> existingLichs = lichLamViecRepository.findByNhanVienAndNgayLamViec(nhanVien, date);
            LichLamViec llv = existingLichs.isEmpty() ? null : existingLichs.get(0);

            if (newStatus == 2) {
                if (llv == null) {
                    llv = new LichLamViec();
                    llv.setNhanVien(nhanVien);
                    llv.setNgayLamViec(date);
                }
                llv.setTrangThai(newStatus);
                llv.setJson("{\"isHoliday\": false, \"isFestival\": true}");
                lichLamViecRepository.save(llv);
            } else if (newStatus == 1) {
                if (llv != null) {
                    List<ChiTietLichLam> chiTiets = chiTietLichLamRepository.findByLichLamViec(llv);
                    if (chiTiets.isEmpty()) {
                        lichLamViecRepository.delete(llv);
                    } else {
                        llv.setTrangThai(1);
                        llv.setJson(null);
                        lichLamViecRepository.save(llv);
                    }
                }
            }
        }
    }

    public void addShift(Long nhanVienId, LocalDate date, Long caLamViecId) {
        Long resolvedId = resolveCuaHangId(0L);
        NhanVien nhanVien = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại: " + nhanVienId));

        if (nhanVien.getCuaHang() == null || !nhanVien.getCuaHang().getId().equals(resolvedId)) {
            // Option to throw exception if cross-store modification isn't allowed
        }

        List<LichLamViec> existingLichs = lichLamViecRepository.findByNhanVienAndNgayLamViec(nhanVien, date);
        LichLamViec llv = existingLichs.isEmpty() ? null : existingLichs.get(0);

        if (llv == null) {
            llv = new LichLamViec();
            llv.setNhanVien(nhanVien);
            llv.setNgayLamViec(date);
            llv.setTrangThai(1);
            llv = lichLamViecRepository.save(llv);
        }
        CaLamViec ca = caLamViecRepository.findById(caLamViecId)
                .orElseThrow(() -> new RuntimeException("Ca làm không tồn tại"));

        List<ChiTietLichLam> chiTiets = chiTietLichLamRepository.findByLichLamViec(llv);
        boolean exists = chiTiets.stream().anyMatch(ct -> ct.getCaLamViec().getId().equals(caLamViecId));
        if (!exists) {
            ChiTietLichLam ct = new ChiTietLichLam();
            ct.setLichLamViec(llv);
            ct.setCaLamViec(ca);
            ct.setTrangThai(1);
            chiTietLichLamRepository.save(ct);
        }
    }

    public void removeShift(Long nhanVienId, LocalDate date, Long caLamViecId) {
        Long resolvedId = resolveCuaHangId(0L);
        NhanVien nhanVien = nhanVienRepository.findById(nhanVienId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại: " + nhanVienId));

        List<LichLamViec> existingLichs = lichLamViecRepository.findByNhanVienAndNgayLamViec(nhanVien, date);
        LichLamViec llv = existingLichs.isEmpty() ? null : existingLichs.get(0);

        if (llv != null) {
            List<ChiTietLichLam> chiTiets = chiTietLichLamRepository.findByLichLamViec(llv);
            ChiTietLichLam target = chiTiets.stream()
                    .filter(ct -> ct.getCaLamViec().getId().equals(caLamViecId))
                    .findFirst()
                    .orElse(null);

            if (target != null) {
                List<com.vn.shopping.domain.DoiCa> doiCas = doiCaRepository.findByLichLamViec(llv).stream()
                        .filter(d -> d.getChiTietLichLam() != null
                                && d.getChiTietLichLam().getId().equals(target.getId()))
                        .toList();
                if (!doiCas.isEmpty())
                    doiCaRepository.deleteAll(doiCas);

                List<com.vn.shopping.domain.LoiPhatSinh> lois = loiPhatSinhRepository.findByLichLamViec(llv).stream()
                        .filter(l -> l.getChiTietLichLam() != null
                                && l.getChiTietLichLam().getId().equals(target.getId()))
                        .toList();
                if (!lois.isEmpty())
                    loiPhatSinhRepository.deleteAll(lois);

                chiTietLichLamRepository.delete(target);

                if (chiTiets.size() == 1 && llv.getTrangThai() == 1) {
                    lichLamViecRepository.delete(llv);
                }
            }
        }
    }

    /**
     * Import lịch làm việc từ file Excel (.xlsx) theo format Grid chi tiết từng ca
     * cho mỗi ngày.
     */
    public List<LichLamViec> importFromExcel(Long cuaHangId, MultipartFile file) throws IOException {
        List<LichLamViec> result = new ArrayList<>();

        List<CaLamViec> allShifts = caLamViecRepository.findAll();
        Map<String, CaLamViec> shiftMap = new HashMap<>(); // shortcode "S" -> CaLamViec
        for (CaLamViec ca : allShifts) {
            String name = ca.getTenCaLam().toLowerCase();
            if (name.contains("sáng"))
                shiftMap.put("S", ca);
            else if (name.contains("chiều"))
                shiftMap.put("C", ca);
            else if (name.contains("tối"))
                shiftMap.put("T", ca);
            else if (name.contains("toàn"))
                shiftMap.put("X", ca);
            else
                shiftMap.put(String.valueOf(ca.getId()), ca);
        }

        try (InputStream is = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (!rowIterator.hasNext())
                return result;
            Row storeRow = rowIterator.next();
            String storeHeader = getCellStringValue(storeRow.getCell(0));
            int year = 0, month = 0;
            try {
                String thangStr = storeHeader.substring(storeHeader.indexOf("Tháng:") + 6).trim();
                String[] parts = thangStr.split("/");
                month = Integer.parseInt(parts[0].trim());
                year = Integer.parseInt(parts[1].trim());
            } catch (Exception e) {
                year = LocalDate.now().getYear();
                month = LocalDate.now().getMonthValue();
            }
            int daysInMonth = java.time.YearMonth.of(year, month).lengthOfMonth();

            if (rowIterator.hasNext())
                rowIterator.next(); // Skip row 1 (Hướng dẫn)

            if (!rowIterator.hasNext())
                return result;
            Row dayRow = rowIterator.next(); // Row 2: Days (01, 02)

            if (!rowIterator.hasNext())
                return result;
            Row holidayRow = rowIterator.next(); // Row 3: Ngày nghỉ (N/L)

            if (!rowIterator.hasNext())
                return result;
            Row shiftRow = rowIterator.next(); // Row 4: Shifts (S, C, T, X)

            Map<Integer, Integer> holidayMap = new HashMap<>();
            List<Integer> colDays = new ArrayList<>();
            List<String> colShifts = new ArrayList<>();
            int lastDay = 0;
            int lastCellNum = shiftRow.getLastCellNum();

            for (int c = 2; c < lastCellNum; c++) {
                String dayStr = getCellStringValue(dayRow.getCell(c)).trim();
                if (!dayStr.isEmpty()) {
                    try {
                        lastDay = (int) Double.parseDouble(dayStr);
                    } catch (Exception e) {
                    }
                }
                colDays.add(lastDay);
                colShifts.add(getCellStringValue(shiftRow.getCell(c)).trim().toUpperCase());

                if (lastDay > 0) {
                    String hol = getCellStringValue(holidayRow.getCell(c)).trim().toUpperCase();
                    if (hol.equals("N")) {
                        holidayMap.put(lastDay, 0);
                    } else if (hol.equals("L")) {
                        holidayMap.put(lastDay, 2);
                    }
                }
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isRowEmpty(row))
                    continue;

                Cell maNhanVienCell = row.getCell(0);
                if (maNhanVienCell == null)
                    continue;

                long maNhanVien = (long) getCellNumericValue(maNhanVienCell);
                NhanVien nhanVien = nhanVienRepository.findById(maNhanVien).orElse(null);
                if (nhanVien == null || nhanVien.getCuaHang() == null || nhanVien.getCuaHang().getId() != cuaHangId) {
                    continue;
                }

                Map<LocalDate, List<CaLamViec>> dayShifts = new HashMap<>();

                for (int c = 2; c < lastCellNum; c++) {
                    if (c - 2 >= colDays.size())
                        break;
                    int day = colDays.get(c - 2);
                    if (day < 1 || day > daysInMonth)
                        continue;

                    String caCode = colShifts.get(c - 2);
                    CaLamViec ca = shiftMap.get(caCode);
                    if (ca == null)
                        continue;

                    Cell cell = row.getCell(c);
                    String val = getCellStringValue(cell).trim().toUpperCase();
                    if (val.equals("1") || val.equals("X") || val.equals("TRUE")) {
                        LocalDate date = LocalDate.of(year, month, day);
                        dayShifts.computeIfAbsent(date, k -> new ArrayList<>()).add(ca);
                    }
                }

                // Cập nhật CSDL theo tháng cho nhân viên này
                for (int d = 1; d <= daysInMonth; d++) {
                    LocalDate date = LocalDate.of(year, month, d);
                    List<CaLamViec> assignedShifts = dayShifts.getOrDefault(date, new ArrayList<>());
                    int statusDay = holidayMap.getOrDefault(d, 1);

                    if (statusDay == 0) {
                        assignedShifts.clear();
                    }

                    // clear old data for this day
                    List<LichLamViec> existingLichs = lichLamViecRepository.findByNhanVienAndNgayLamViec(nhanVien,
                            date);
                    LichLamViec llv = null;
                    if (!existingLichs.isEmpty()) {
                        llv = existingLichs.get(0);

                        List<com.vn.shopping.domain.DoiCa> oldDoiCas = doiCaRepository.findByLichLamViec(llv);
                        if (oldDoiCas != null && !oldDoiCas.isEmpty()) {
                            doiCaRepository.deleteAll(oldDoiCas);
                        }

                        List<com.vn.shopping.domain.LoiPhatSinh> oldLois = loiPhatSinhRepository.findByLichLamViec(llv);
                        if (oldLois != null && !oldLois.isEmpty()) {
                            loiPhatSinhRepository.deleteAll(oldLois);
                        }

                        List<ChiTietLichLam> oldChiTiets = chiTietLichLamRepository.findByLichLamViec(llv);
                        chiTietLichLamRepository.deleteAll(oldChiTiets);
                    }

                    if (assignedShifts.isEmpty() && statusDay == 1) {
                        if (llv != null)
                            lichLamViecRepository.delete(llv);
                        continue;
                    }

                    if (llv == null) {
                        llv = new LichLamViec();
                        llv.setNhanVien(nhanVien);
                        llv.setNgayLamViec(date);
                    }
                    llv.setTrangThai(statusDay);
                    if (statusDay != 1) {
                        llv.setJson(
                                "{\"isHoliday\": " + (statusDay == 0) + ", \"isFestival\": " + (statusDay == 2) + "}");
                    } else {
                        llv.setJson(null);
                    }
                    llv = lichLamViecRepository.save(llv);

                    for (CaLamViec ca : assignedShifts) {
                        ChiTietLichLam chiTiet = new ChiTietLichLam();
                        chiTiet.setLichLamViec(llv);
                        chiTiet.setCaLamViec(ca);
                        chiTiet.setTrangThai(1);
                        chiTietLichLamRepository.save(chiTiet);
                    }
                    if (!result.contains(llv))
                        result.add(llv);
                }
            }
        }
        return result;
    }

    /**
     * Tải file Excel mẫu để import lịch làm việc cho cửa hàng cụ thể (Format chi
     * tiết Từng Ca).
     */
    public byte[] downloadTemplateFromCuaHang(Long cuaHangId, int year, int month) throws IOException {
        com.vn.shopping.domain.CuaHang cuaHang = cuaHangRepository.findById(cuaHangId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cửa hàng: " + cuaHangId));

        List<NhanVien> nhanViens = nhanVienRepository.findByCuaHang_Id(cuaHangId).stream()
                .filter(nv -> nv.getTrangThai() == 1)
                .toList();

        List<CaLamViec> caLamViecs = caLamViecRepository.findAll();
        Map<Long, String> shiftShortcodes = new HashMap<>();
        for (CaLamViec ca : caLamViecs) {
            String name = ca.getTenCaLam().toLowerCase();
            if (name.contains("sáng"))
                shiftShortcodes.put(ca.getId(), "S");
            else if (name.contains("chiều"))
                shiftShortcodes.put(ca.getId(), "C");
            else if (name.contains("tối"))
                shiftShortcodes.put(ca.getId(), "T");
            else if (name.contains("toàn"))
                shiftShortcodes.put(ca.getId(), "X");
            else
                shiftShortcodes.put(ca.getId(), String.valueOf(ca.getId()));
        }

        try (Workbook workbook = new XSSFWorkbook();
                java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("LichLamViec");

            CellStyle storeStyle = workbook.createCellStyle();
            Font storeFont = workbook.createFont();
            storeFont.setBold(true);
            storeFont.setFontHeightInPoints((short) 14);
            storeStyle.setFont(storeFont);

            Row storeRow = sheet.createRow(0);
            Cell storeCell = storeRow.createCell(0);
            storeCell.setCellValue(
                    "Cửa hàng: " + cuaHang.getTenCuaHang() + " | Tháng: " + String.format("%02d/%d", month, year));
            storeCell.setCellStyle(storeStyle);

            Row guideRow = sheet.createRow(1);
            Cell guideCell = guideRow.createCell(0);
            guideCell.setCellValue(
                    "Điền 1, X hoặc TRUE vào ô trống nếu làm ca đó. Bỏ trống nếu không làm. Các mã ca: S=Sáng, C=Chiều, T=Tối, X=Cả ngày.");

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

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);

            Row dayRow = sheet.createRow(2);
            Row holidayRow = sheet.createRow(3);
            Row shiftRow = sheet.createRow(4);

            dayRow.createCell(0).setCellValue("MaNhanVien");
            dayRow.getCell(0).setCellStyle(headerStyle);
            holidayRow.createCell(0).setCellStyle(headerStyle);
            shiftRow.createCell(0).setCellStyle(headerStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(2, 4, 0, 0));
            sheet.setColumnWidth(0, 4000);

            dayRow.createCell(1).setCellValue("TenNhanVien");
            dayRow.getCell(1).setCellStyle(headerStyle);

            holidayRow.createCell(1).setCellValue("Ngày nghỉ / Ngày lễ (N/L):");
            CellStyle holidayHeaderStyle = workbook.createCellStyle();
            holidayHeaderStyle.cloneStyleFrom(headerStyle);
            holidayHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
            holidayRow.getCell(1).setCellStyle(holidayHeaderStyle);

            shiftRow.createCell(1).setCellValue("Ca làm việc:");
            CellStyle shiftHeaderStyle = workbook.createCellStyle();
            shiftHeaderStyle.cloneStyleFrom(headerStyle);
            shiftHeaderStyle.setAlignment(HorizontalAlignment.RIGHT);
            shiftRow.getCell(1).setCellStyle(shiftHeaderStyle);

            sheet.setColumnWidth(1, 6000);

            int daysInMonth = java.time.YearMonth.of(year, month).lengthOfMonth();
            int colIdx = 2;
            for (int d = 1; d <= daysInMonth; d++) {
                int startCol = colIdx;
                for (CaLamViec ca : caLamViecs) {
                    Cell dCell = dayRow.createCell(colIdx);
                    dCell.setCellValue(String.format("%02d", d));
                    dCell.setCellStyle(headerStyle);

                    Cell hCell = holidayRow.createCell(colIdx);
                    hCell.setCellStyle(dataStyle);

                    Cell shiftCell = shiftRow.createCell(colIdx);
                    shiftCell.setCellValue(shiftShortcodes.get(ca.getId()));
                    shiftCell.setCellStyle(headerStyle);

                    sheet.setColumnWidth(colIdx, 1800);
                    colIdx++;
                }

                if (colIdx - 1 > startCol) {
                    sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(2, 2, startCol, colIdx - 1));
                    sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(3, 3, startCol, colIdx - 1));
                }
            }

            int rowIdx = 5;
            for (NhanVien nv : nhanViens) {
                Row row = sheet.createRow(rowIdx++);
                Cell c0 = row.createCell(0);
                c0.setCellValue(nv.getId());
                c0.setCellStyle(dataStyle);
                Cell c1 = row.createCell(1);
                c1.setCellValue(nv.getTenNhanVien());
                c1.setCellStyle(dataStyle);

                int cData = 2;
                for (int d = 1; d <= daysInMonth; d++) {
                    for (int i = 0; i < caLamViecs.size(); i++) {
                        Cell cx = row.createCell(cData++);
                        cx.setCellValue("");
                        cx.setCellStyle(dataStyle);
                    }
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null)
            return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }

    private double getCellNumericValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC)
            return cell.getNumericCellValue();
        if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null)
            return "";
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
