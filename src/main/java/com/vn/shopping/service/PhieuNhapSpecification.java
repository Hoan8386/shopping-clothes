package com.vn.shopping.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vn.shopping.domain.PhieuNhap;

import jakarta.persistence.criteria.Predicate;

public class PhieuNhapSpecification {

    public static Specification<PhieuNhap> filter(
            String tenPhieuNhap,
            Integer trangThai,
            String tenCuaHang,
            String tenNhaCungCap,
            LocalDateTime ngayTaoTu,
            LocalDateTime ngayTaoDen,
            LocalDateTime ngayGiaoHangTu,
            LocalDateTime ngayGiaoHangDen,
            LocalDateTime ngayNhanHangTu,
            LocalDateTime ngayNhanHangDen) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tenPhieuNhap != null && !tenPhieuNhap.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("tenPhieuNhap")),
                        "%" + tenPhieuNhap.trim().toLowerCase() + "%"));
            }

            if (trangThai != null) {
                predicates.add(criteriaBuilder.equal(root.get("trangThai"), trangThai));
            }

            if (tenCuaHang != null && !tenCuaHang.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("cuaHang").get("tenCuaHang")),
                        "%" + tenCuaHang.trim().toLowerCase() + "%"));
            }

            if (tenNhaCungCap != null && !tenNhaCungCap.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nhaCungCap").get("tenNhaCungCap")),
                        "%" + tenNhaCungCap.trim().toLowerCase() + "%"));
            }

            if (ngayTaoTu != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("ngayTao"), ngayTaoTu));
            }
            if (ngayTaoDen != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("ngayTao"), ngayTaoDen));
            }

            if (ngayGiaoHangTu != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("ngayGiaoHang"), ngayGiaoHangTu));
            }
            if (ngayGiaoHangDen != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("ngayGiaoHang"), ngayGiaoHangDen));
            }

            if (ngayNhanHangTu != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("ngayNhanHang"), ngayNhanHangTu));
            }
            if (ngayNhanHangDen != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("ngayNhanHang"), ngayNhanHangDen));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
