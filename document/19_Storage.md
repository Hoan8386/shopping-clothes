# Storage Controller

> **Base Path:** `/storage`  
> **File:** `StorageController.java`  
> Phục vụ file/hình ảnh từ MinIO storage. **Không yêu cầu xác thực.**

---

## 1. Lấy file/hình ảnh

| Thuộc tính   | Chi tiết                                                     |
| ------------ | ------------------------------------------------------------ |
| **URL**      | `GET /storage/{fileName}`                                    |
| **Xác thực** | **Không yêu cầu** (đã whitelist trong SecurityConfiguration) |

**Path Parameters:**

| Tham số    | Kiểu   | Mô tả                                |
| ---------- | ------ | ------------------------------------ |
| `fileName` | String | Tên file cần lấy (vd: `abc-123.jpg`) |

**Response:** `200 OK`

- Body: Binary file content
- Content-Type: Tự động nhận diện từ tên file (image/jpeg, image/png, ...)
- Nếu không xác định được → `application/octet-stream`

**Response Headers:**

| Header                | Giá trị                         | Mô tả                            |
| --------------------- | ------------------------------- | -------------------------------- |
| `Content-Disposition` | `inline; filename="<fileName>"` | Hiển thị inline trên trình duyệt |
| `Cache-Control`       | `max-age=86400`                 | Cache 1 ngày                     |

**Ví dụ request:**

```
GET /storage/abc-123.jpg
```

**Lỗi:**

- `404 Not Found` — File không tồn tại trên MinIO

---

## Cách sử dụng

URL hình ảnh trả về từ các API sản phẩm có thể truy cập trực tiếp:

```html
<img src="http://localhost:8080/storage/ten-file.jpg" />
```

Hoặc trong frontend:

```javascript
const imageUrl = `${BASE_URL}/storage/${product.hinhAnh}`;
```
