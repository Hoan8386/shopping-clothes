package com.vn.shopping.controller;

import com.vn.shopping.domain.response.ResThongKeDTO;
import com.vn.shopping.service.ThongKeService;
import com.vn.shopping.util.anotation.ApiMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/thong-ke")
public class ThongKeController {

    private final ThongKeService thongKeService;

    public ThongKeController(ThongKeService thongKeService) {
        this.thongKeService = thongKeService;
    }

    @GetMapping("/doanh-thu")
    @ApiMessage("Thong ke doanh thu ban hang")
    public ResponseEntity<ResThongKeDTO.RevenueReport> getRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId) {
        return ResponseEntity.ok(thongKeService.getRevenueReport(fromDate, toDate, cuaHangId));
    }

    @GetMapping("/doanh-thu/export")
    @ApiMessage("Xuat Excel thong ke doanh thu ban hang")
    public ResponseEntity<Resource> exportRevenue(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId)
            throws IOException {
        byte[] bytes = thongKeService.exportRevenueReport(fromDate, toDate, cuaHangId);
        return buildExcelResponse(bytes, "thong-ke-doanh-thu.xlsx");
    }

    @GetMapping("/hieu-suat-don-hang")
    @ApiMessage("Thong ke hieu suat don hang")
    public ResponseEntity<ResThongKeDTO.OrderPerformanceReport> getOrderPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId) {
        return ResponseEntity.ok(thongKeService.getOrderPerformanceReport(fromDate, toDate, cuaHangId));
    }

    @GetMapping("/hieu-suat-don-hang/export")
    @ApiMessage("Xuat Excel thong ke hieu suat don hang")
    public ResponseEntity<Resource> exportOrderPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId)
            throws IOException {
        byte[] bytes = thongKeService.exportOrderPerformanceReport(fromDate, toDate, cuaHangId);
        return buildExcelResponse(bytes, "thong-ke-hieu-suat-don-hang.xlsx");
    }

    @GetMapping("/ton-kho-canh-bao")
    @ApiMessage("Thong ke ton kho va canh bao")
    public ResponseEntity<ResThongKeDTO.InventoryAlertReport> getInventoryAlert(
            @RequestParam(required = false, defaultValue = "5") Integer lowStockThreshold,
            @RequestParam(required = false) Long cuaHangId) {
        return ResponseEntity.ok(thongKeService.getInventoryAlertReport(lowStockThreshold, cuaHangId));
    }

    @GetMapping("/ton-kho-canh-bao/export")
    @ApiMessage("Xuat Excel thong ke ton kho va canh bao")
    public ResponseEntity<Resource> exportInventoryAlert(
            @RequestParam(required = false, defaultValue = "5") Integer lowStockThreshold,
            @RequestParam(required = false) Long cuaHangId) throws IOException {
        byte[] bytes = thongKeService.exportInventoryAlertReport(lowStockThreshold, cuaHangId);
        return buildExcelResponse(bytes, "thong-ke-ton-kho-canh-bao.xlsx");
    }

    @GetMapping("/top-san-pham")
    @ApiMessage("Thong ke top san pham")
    public ResponseEntity<ResThongKeDTO.TopProductReport> getTopProducts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long cuaHangId) {
        return ResponseEntity.ok(thongKeService.getTopProductReport(fromDate, toDate, limit, cuaHangId));
    }

    @GetMapping("/top-san-pham/export")
    @ApiMessage("Xuat Excel thong ke top san pham")
    public ResponseEntity<Resource> exportTopProducts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false) Long cuaHangId) throws IOException {
        byte[] bytes = thongKeService.exportTopProductReport(fromDate, toDate, limit, cuaHangId);
        return buildExcelResponse(bytes, "thong-ke-top-san-pham.xlsx");
    }

    @GetMapping("/nhap-hang-ncc")
    @ApiMessage("Thong ke nhap hang va nha cung cap")
    public ResponseEntity<ResThongKeDTO.ImportSupplierReport> getImportSupplier(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId) {
        return ResponseEntity.ok(thongKeService.getImportSupplierReport(fromDate, toDate, cuaHangId));
    }

    @GetMapping("/nhap-hang-ncc/export")
    @ApiMessage("Xuat Excel thong ke nhap hang va nha cung cap")
    public ResponseEntity<Resource> exportImportSupplier(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId)
            throws IOException {
        byte[] bytes = thongKeService.exportImportSupplierReport(fromDate, toDate, cuaHangId);
        return buildExcelResponse(bytes, "thong-ke-nhap-hang-ncc.xlsx");
    }

    @GetMapping("/tra-doi")
    @ApiMessage("Thong ke tra doi")
    public ResponseEntity<ResThongKeDTO.ReturnExchangeReport> getReturnExchange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId) {
        return ResponseEntity.ok(thongKeService.getReturnExchangeReport(fromDate, toDate, cuaHangId));
    }

    @GetMapping("/tra-doi/export")
    @ApiMessage("Xuat Excel thong ke tra doi")
    public ResponseEntity<Resource> exportReturnExchange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId)
            throws IOException {
        byte[] bytes = thongKeService.exportReturnExchangeReport(fromDate, toDate, cuaHangId);
        return buildExcelResponse(bytes, "thong-ke-tra-doi.xlsx");
    }

    @GetMapping("/khuyen-mai")
    @ApiMessage("Thong ke khuyen mai")
    public ResponseEntity<ResThongKeDTO.PromotionReport> getPromotion(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId) {
        return ResponseEntity.ok(thongKeService.getPromotionReport(fromDate, toDate, cuaHangId));
    }

    @GetMapping("/khuyen-mai/export")
    @ApiMessage("Xuat Excel thong ke khuyen mai")
    public ResponseEntity<Resource> exportPromotion(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId)
            throws IOException {
        byte[] bytes = thongKeService.exportPromotionReport(fromDate, toDate, cuaHangId);
        return buildExcelResponse(bytes, "thong-ke-khuyen-mai.xlsx");
    }

    @GetMapping("/nang-suat-nhan-vien")
    @ApiMessage("Thong ke nang suat nhan vien")
    public ResponseEntity<ResThongKeDTO.StaffPerformanceReport> getStaffPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId) {
        return ResponseEntity.ok(thongKeService.getStaffPerformanceReport(fromDate, toDate, cuaHangId));
    }

    @GetMapping("/nang-suat-nhan-vien/export")
    @ApiMessage("Xuat Excel thong ke nang suat nhan vien")
    public ResponseEntity<Resource> exportStaffPerformance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) Long cuaHangId)
            throws IOException {
        byte[] bytes = thongKeService.exportStaffPerformanceReport(fromDate, toDate, cuaHangId);
        return buildExcelResponse(bytes, "thong-ke-nang-suat-nhan-vien.xlsx");
    }

    private ResponseEntity<Resource> buildExcelResponse(byte[] bytes, String filename) {
        ByteArrayResource resource = new ByteArrayResource(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok().headers(headers).body(resource);
    }
}
