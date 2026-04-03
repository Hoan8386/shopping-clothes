package com.vn.shopping.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vn.shopping.domain.*;
import com.vn.shopping.domain.request.ReqDanhGiaSanPhamDTO;
import com.vn.shopping.domain.response.ResDanhGiaSanPhamDTO;
import com.vn.shopping.repository.*;
import com.vn.shopping.util.error.IdInvalidException;

@Service
public class DanhGiaSanPhamService {

    private static final String REVIEW_FOLDER = "reviews";

    private final DanhGiaSanPhamRepository danhGiaSanPhamRepository;
    private final KhachHangRepository khachHangRepository;
    private final ChiTietDonHangRepository chiTietDonHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    public DanhGiaSanPhamService(DanhGiaSanPhamRepository danhGiaSanPhamRepository,
            KhachHangRepository khachHangRepository,
            ChiTietDonHangRepository chiTietDonHangRepository,
            NhanVienRepository nhanVienRepository,
            StorageService storageService,
            ObjectMapper objectMapper) {
        this.danhGiaSanPhamRepository = danhGiaSanPhamRepository;
        this.khachHangRepository = khachHangRepository;
        this.chiTietDonHangRepository = chiTietDonHangRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.storageService = storageService;
        this.objectMapper = objectMapper;
    }

    /**
     * Khách hàng tạo đánh giá sản phẩm.
     * Điều kiện:
     * - Phải đăng nhập
     * - Đơn hàng phải ở trạng thái 5 (Đã nhận hàng)
     * - Đơn hàng phải thuộc về khách hàng
     * - Mỗi chi tiết đơn hàng chỉ được đánh giá 1 lần duy nhất
     */
    @Transactional
    public DanhGiaSanPham create(ReqDanhGiaSanPhamDTO req, MultipartFile file, MultipartFile videoFile)
            throws IdInvalidException {
        // 1. Lấy khách hàng từ token
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        // 2. Kiểm tra chi tiết đơn hàng tồn tại
        ChiTietDonHang chiTietDonHang = chiTietDonHangRepository.findById(req.getChiTietDonHangId())
                .orElseThrow(
                        () -> new IdInvalidException("Không tìm thấy chi tiết đơn hàng: " + req.getChiTietDonHangId()));

        // 3. Lấy đơn hàng và kiểm tra quyền sở hữu
        DonHang donHang = chiTietDonHang.getDonHang();
        if (donHang == null || donHang.getKhachHang() == null
                || !donHang.getKhachHang().getId().equals(khachHang.getId())) {
            throw new IdInvalidException("Đơn hàng không thuộc về bạn");
        }

        // 4. Kiểm tra trạng thái đơn hàng = 5 (Đã nhận hàng)
        if (donHang.getTrangThai() == null || donHang.getTrangThai() != 5) {
            throw new IdInvalidException("Đơn hàng phải ở trạng thái đã nhận hàng mới có thể đánh giá");
        }

        // 5. Kiểm tra chưa đánh giá chi tiết sản phẩm này (mỗi chi tiết đơn chỉ đánh
        // giá 1 lần)
        if (danhGiaSanPhamRepository.existsByKhachHangIdAndChiTietDonHangId(
                khachHang.getId(), chiTietDonHang.getId())) {
            throw new IdInvalidException("Bạn đã đánh giá sản phẩm này rồi");
        }

        // 6. Validate số sao
        if (req.getSoSao() == null || req.getSoSao() < 1 || req.getSoSao() > 5) {
            throw new IdInvalidException("Số sao phải từ 1 đến 5");
        }

        // 7. Upload ảnh lên Cloudinary (nếu có)
        String hinhAnhUrl = null;
        if (file != null && !file.isEmpty()) {
            validateImageFile(file);
            hinhAnhUrl = storageService.uploadSingleFile(file, REVIEW_FOLDER);
        }

        // 8. Upload video lên Cloudinary hoặc nhận link video trực tiếp
        String linkVideo = normalizeNullableText(req.getLinkVideo());
        if (videoFile != null && !videoFile.isEmpty()) {
            validateVideoFile(videoFile);
            linkVideo = storageService.uploadSingleFile(videoFile, REVIEW_FOLDER);
        }

        // 9. Tạo đánh giá
        DanhGiaSanPham danhGia = new DanhGiaSanPham();
        danhGia.setKhachHang(khachHang);
        danhGia.setChiTietDonHang(chiTietDonHang);
        danhGia.setSoSao(req.getSoSao());
        danhGia.setGhiTru(req.getGhiTru());
        danhGia.setHinhAnh(hinhAnhUrl);
        danhGia.setLinkVideo(linkVideo);

        return danhGiaSanPhamRepository.save(danhGia);
    }

    @Transactional
    public DanhGiaSanPham update(Long id, ReqDanhGiaSanPhamDTO req, MultipartFile file, MultipartFile videoFile)
            throws IdInvalidException {
        DanhGiaSanPham existing = danhGiaSanPhamRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đánh giá: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        if (!existing.getKhachHang().getId().equals(khachHang.getId())) {
            throw new IdInvalidException("Bạn không có quyền sửa đánh giá này");
        }

        if (req.getSoSao() != null) {
            if (req.getSoSao() < 1 || req.getSoSao() > 5) {
                throw new IdInvalidException("Số sao phải từ 1 đến 5");
            }
            existing.setSoSao(req.getSoSao());
        }

        if (req.getGhiTru() != null) {
            existing.setGhiTru(req.getGhiTru());
        }

        if (req.getLinkVideo() != null) {
            existing.setLinkVideo(normalizeNullableText(req.getLinkVideo()));
        }

        if (file != null && !file.isEmpty()) {
            validateImageFile(file);
            String hinhAnhUrl = storageService.uploadSingleFile(file, REVIEW_FOLDER);
            existing.setHinhAnh(hinhAnhUrl);
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            validateVideoFile(videoFile);
            String videoUrl = storageService.uploadSingleFile(videoFile, REVIEW_FOLDER);
            existing.setLinkVideo(videoUrl);
        }

        return danhGiaSanPhamRepository.save(existing);
    }

    public void delete(long id) throws IdInvalidException {
        DanhGiaSanPham danhGia = danhGiaSanPhamRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đánh giá: " + id));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<KhachHang> khOpt = khachHangRepository.findByEmail(email);
        if (khOpt.isPresent()) {
            if (!danhGia.getKhachHang().getId().equals(khOpt.get().getId())) {
                throw new IdInvalidException("Bạn không có quyền xóa đánh giá này");
            }
        }

        danhGiaSanPhamRepository.deleteById(id);
    }

    @Transactional
    public DanhGiaSanPham replyAsAdmin(Long id, String noiDung) throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        NhanVien nhanVien = nhanVienRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Chỉ admin mới có quyền trả lời đánh giá"));

        if (nhanVien.getRole() == null || !"ADMIN".equalsIgnoreCase(nhanVien.getRole().getName())) {
            throw new IdInvalidException("Chỉ admin mới có quyền trả lời đánh giá");
        }

        String noiDungTrim = normalizeNullableText(noiDung);
        if (noiDungTrim == null) {
            throw new IdInvalidException("Nội dung phản hồi không được để trống");
        }
        if (noiDungTrim.length() > 1000) {
            throw new IdInvalidException("Nội dung phản hồi tối đa 1000 ký tự");
        }

        DanhGiaSanPham danhGia = danhGiaSanPhamRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy đánh giá: " + id));

        danhGia.setJson(mergeReplyMetadata(danhGia.getJson(), noiDungTrim, email));
        return danhGiaSanPhamRepository.save(danhGia);
    }

    public DanhGiaSanPham findById(long id) {
        return danhGiaSanPhamRepository.findById(id).orElse(null);
    }

    public List<DanhGiaSanPham> findAll() {
        return danhGiaSanPhamRepository.findAll();
    }

    public List<DanhGiaSanPham> findByChiTietDonHangId(Long chiTietDonHangId) {
        return danhGiaSanPhamRepository.findByChiTietDonHangId(chiTietDonHangId);
    }

    public List<DanhGiaSanPham> findBySanPhamId(Long sanPhamId) {
        return danhGiaSanPhamRepository.findBySanPhamId(sanPhamId);
    }

    @Transactional(readOnly = true)
    public List<ResDanhGiaSanPhamDTO> findByCurrentKhachHang() throws IdInvalidException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy khách hàng với email: " + email));

        return danhGiaSanPhamRepository.findByKhachHangId(khachHang.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ResDanhGiaSanPhamDTO convertToDTO(DanhGiaSanPham dg) {
        ResDanhGiaSanPhamDTO dto = new ResDanhGiaSanPhamDTO();
        dto.setId(dg.getId());
        dto.setSoSao(dg.getSoSao());
        dto.setGhiTru(dg.getGhiTru());
        dto.setHinhAnh(dg.getHinhAnh());
        dto.setLinkVideo(dg.getLinkVideo());
        dto.setNgayTao(dg.getNgayTao());
        dto.setNgayCapNhat(dg.getNgayCapNhat());

        JsonNode replyNode = getReplyNode(dg.getJson());
        if (replyNode != null) {
            dto.setAdminPhanHoi(getTextNode(replyNode, "adminReply"));
            dto.setAdminPhanHoiBy(getTextNode(replyNode, "adminReplyBy"));
            String replyAt = getTextNode(replyNode, "adminReplyAt");
            if (StringUtils.hasText(replyAt)) {
                try {
                    dto.setAdminPhanHoiAt(java.time.LocalDateTime.parse(replyAt));
                } catch (Exception ignored) {
                    dto.setAdminPhanHoiAt(null);
                }
            }
        }

        if (dg.getKhachHang() != null) {
            dto.setKhachHangId(dg.getKhachHang().getId());
            dto.setTenKhachHang(dg.getKhachHang().getTenKhachHang());
        }

        if (dg.getChiTietDonHang() != null) {
            dto.setChiTietDonHangId(dg.getChiTietDonHang().getId());
            if (dg.getChiTietDonHang().getDonHang() != null) {
                dto.setDonHangId(dg.getChiTietDonHang().getDonHang().getId());
            }
            if (dg.getChiTietDonHang().getChiTietSanPham() != null
                    && dg.getChiTietDonHang().getChiTietSanPham().getSanPham() != null) {
                SanPham sp = dg.getChiTietDonHang().getChiTietSanPham().getSanPham();
                dto.setSanPhamId(sp.getId());
                dto.setTenSanPham(sp.getTenSanPham());
            }
        }

        return dto;
    }

    private void validateImageFile(MultipartFile file) throws IdInvalidException {
        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !contentType.toLowerCase().startsWith("image/")) {
            throw new IdInvalidException("File ảnh không hợp lệ");
        }
    }

    private void validateVideoFile(MultipartFile file) throws IdInvalidException {
        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !contentType.toLowerCase().startsWith("video/")) {
            throw new IdInvalidException("File video không hợp lệ");
        }
    }

    private String normalizeNullableText(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private String mergeReplyMetadata(String rawJson, String replyText, String replyBy) {
        try {
            ObjectNode root;
            if (StringUtils.hasText(rawJson)) {
                JsonNode parsed = objectMapper.readTree(rawJson);
                root = parsed != null && parsed.isObject() ? (ObjectNode) parsed : objectMapper.createObjectNode();
            } else {
                root = objectMapper.createObjectNode();
            }
            root.put("adminReply", replyText);
            root.put("adminReplyBy", replyBy);
            root.put("adminReplyAt", java.time.LocalDateTime.now().toString());
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            ObjectNode fallback = objectMapper.createObjectNode();
            fallback.put("adminReply", replyText);
            fallback.put("adminReplyBy", replyBy);
            fallback.put("adminReplyAt", java.time.LocalDateTime.now().toString());
            return fallback.toString();
        }
    }

    private JsonNode getReplyNode(String rawJson) {
        if (!StringUtils.hasText(rawJson)) {
            return null;
        }
        try {
            JsonNode node = objectMapper.readTree(rawJson);
            return node != null && node.isObject() ? node : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getTextNode(JsonNode node, String fieldName) {
        if (node == null || !node.has(fieldName) || node.get(fieldName).isNull()) {
            return null;
        }
        String value = node.get(fieldName).asText();
        return StringUtils.hasText(value) ? value : null;
    }
}
