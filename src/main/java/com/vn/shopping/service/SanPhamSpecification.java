package com.vn.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vn.shopping.domain.SanPham;

import jakarta.persistence.criteria.Predicate;

public class SanPhamSpecification {

    /**
     * Lọc sản phẩm theo nhiều tiêu chí:
     * - tenSanPham: tìm kiếm theo tên (LIKE, không phân biệt hoa thường)
     * - kieuSanPhamId: lọc theo kiểu sản phẩm
     * - boSuuTapId: lọc theo bộ sưu tập
     * - thuongHieuId: lọc theo thương hiệu
     * - trangThai: lọc theo trạng thái
     * - giaMin: giá bán tối thiểu
     * - giaMax: giá bán tối đa
     */
    public static Specification<SanPham> filter(
            String tenSanPham,
            Long kieuSanPhamId,
            Long boSuuTapId,
            Long thuongHieuId,
            Integer trangThai,
            Double giaMin,
            Double giaMax) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Tìm theo tên sản phẩm (LIKE, không phân biệt hoa thường)
            if (tenSanPham != null && !tenSanPham.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("tenSanPham")),
                        "%" + tenSanPham.trim().toLowerCase() + "%"));
            }

            // Lọc theo kiểu sản phẩm
            if (kieuSanPhamId != null) {
                predicates.add(criteriaBuilder.equal(root.get("kieuSanPham").get("id"), kieuSanPhamId));
            }

            // Lọc theo bộ sưu tập
            if (boSuuTapId != null) {
                predicates.add(criteriaBuilder.equal(root.get("boSuuTap").get("id"), boSuuTapId));
            }

            // Lọc theo thương hiệu
            if (thuongHieuId != null) {
                predicates.add(criteriaBuilder.equal(root.get("thuongHieu").get("id"), thuongHieuId));
            }

            // Lọc theo trạng thái
            if (trangThai != null) {
                predicates.add(criteriaBuilder.equal(root.get("trangThai"), trangThai));
            }

            // Lọc theo giá bán tối thiểu
            if (giaMin != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("giaBan"), giaMin));
            }

            // Lọc theo giá bán tối đa
            if (giaMax != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("giaBan"), giaMax));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
