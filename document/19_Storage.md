# Storage Controller

> **Base Path:** `/storage`  
> **File:** `StorageController.java`  
> Endpoint public để truy cập file hình ảnh từ MinIO storage.

---

## Tổng quan

Hệ thống sử dụng **MinIO** làm object storage để lưu trữ hình ảnh sản phẩm. Endpoint `/storage/{fileName}` cho phép tải ảnh mà **không cần xác thực** (public).

### Đặc điểm

- **Không cần Bearer Token** — endpoint hoàn toàn public
- **Tự động xác định Content-Type** dựa trên phần mở rộng file
- **Cache** — response được cache 1 ngày (`Cache-Control: max-age=86400`)
- **Inline display** — header `Content-Disposition: inline` cho phép hiển thị trực tiếp trong trình duyệt

---

## 1. Lấy file từ storage

| Thuộc tính   | Chi tiết                  |
| ------------ | ------------------------- |
| **URL**      | `GET /storage/{fileName}` |
| **Method**   | `GET`                     |
| **Xác thực** | Không                     |

**Path Parameters:**

| Tham số    | Kiểu   | Mô tả                             |
| ---------- | ------ | --------------------------------- |
| `fileName` | String | Tên file (vd: `polo-den-m-1.jpg`) |

**Response:** `200 OK` — Trả về file binary (ảnh/file)

**Response Headers:**

| Header                | Giá trị                               |
| --------------------- | ------------------------------------- |
| `Content-Type`        | Tự động (vd: `image/jpeg`)            |
| `Cache-Control`       | `max-age=86400`                       |
| `Content-Disposition` | `inline; filename="polo-den-m-1.jpg"` |

**Ví dụ sử dụng:**

```html
<!-- Trong HTML/Frontend -->
<img src="http://localhost:8080/storage/polo-den-m-1.jpg" alt="Sản phẩm" />
```

```
# Trực tiếp trên trình duyệt
http://localhost:8080/storage/polo-den-m-1.jpg
```

**Lỗi:**

| HTTP Status | Mô tả                     |
| ----------- | ------------------------- |
| `500`       | Lỗi khi tải file từ MinIO |

---

## Cấu hình MinIO

Cấu hình trong `application.properties`:

```properties
minio.endpoint=http://localhost:9000
minio.access-key=minioadmin
minio.secret-key=minioadmin
minio.bucket-name=shopping
```

---

## Liên kết với các entity

| Entity    | Trường ảnh     | Cách sử dụng                                   |
| --------- | -------------- | ---------------------------------------------- |
| `SanPham` | `hinhAnhChinh` | `/storage/{hinhAnhChinh}` — ảnh chính sản phẩm |
| `HinhAnh` | `tenHinhAnh`   | `/storage/{tenHinhAnh}` — ảnh chi tiết SP      |
