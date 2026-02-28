package com.vn.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vn.shopping.domain.DonHang;

import jakarta.persistence.criteria.Predicate;

public class DonHangSpecification {

    /**
     * Lọc đơn hàng theo nhiều tiêu chí:
     * - khachHangId: lọc theo khách hàng (bắt buộc với KHACH_HANG)
     * - cuaHangId: lọc theo cửa hàng
     * - nhanVienId: lọc theo nhân viên
     * - trangThai: lọc theo trạng thái đơn hàng
     * - trangThaiThanhToan: lọc theo trạng thái thanh toán
     * - hinhThucDonHang: lọc theo hình thức (0 = tại quầy, 1 = online)
     */
    public static Specification<DonHang> filter(
            Long khachHangId,
            Long cuaHangId,
            Long nhanVienId,
            Integer trangThai,
            Integer trangThaiThanhToan,
            Integer hinhThucDonHang) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (khachHangId != null) {
                predicates.add(criteriaBuilder.equal(root.get("khachHang").get("id"), khachHangId));
            }

            if (cuaHangId != null) {
                predicates.add(criteriaBuilder.equal(root.get("cuaHang").get("id"), cuaHangId));
            }

            if (nhanVienId != null) {
                predicates.add(criteriaBuilder.equal(root.get("nhanVien").get("id"), nhanVienId));
            }

            if (trangThai != null) {
                predicates.add(criteriaBuilder.equal(root.get("trangThai"), trangThai));
            }

            if (trangThaiThanhToan != null) {
                predicates.add(criteriaBuilder.equal(root.get("trangThaiThanhToan"), trangThaiThanhToan));
            }

            if (hinhThucDonHang != null) {
                predicates.add(criteriaBuilder.equal(root.get("hinhThucDonHang"), hinhThucDonHang));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
