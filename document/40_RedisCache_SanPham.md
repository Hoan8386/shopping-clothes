# Redis Cache cho Sản Phẩm

Tài liệu này mô tả phần Redis cache đã được thêm vào backend để giảm thời gian tải lại dữ liệu sản phẩm và chi tiết sản phẩm khi người dùng refresh trang.

## Mục tiêu

- Cache danh sách sản phẩm.
- Cache chi tiết sản phẩm.
- Cache danh sách chi tiết sản phẩm theo sản phẩm.
- Tự động xóa cache khi có thay đổi dữ liệu để tránh hiển thị dữ liệu cũ.

## Cấu hình Redis

File cấu hình nằm tại [RedisConfig.java](../src/main/java/com/vn/shopping/config/RedisConfig.java).

Các điểm chính:

- Bật cache bằng `@EnableCaching`.
- Dùng `RedisCacheManager` làm cache manager chính.
- Key được serialize bằng `StringRedisSerializer`.
- Value được serialize bằng `GenericJackson2JsonRedisSerializer` để tránh lỗi `SerializationException: Cannot serialize`.
- TTL mặc định là 1 giờ.
- Không cache giá trị `null`.

## Các service đã được cache

### Sản phẩm

File: [SanPhamService.java](../src/main/java/com/vn/shopping/service/SanPhamService.java)

Đã cache các API đọc dữ liệu:

- `findByIdDTO(long id)` dùng cache key `detail_<id>`.
- `findAllDTO()` dùng cache key `all`.
- `filterSanPham(...)` dùng cache key có đầy đủ điều kiện lọc và phân trang.

Đã xóa cache khi dữ liệu thay đổi:

- `create(...)`
- `update(...)`
- `delete(...)`

Tất cả đều xóa toàn bộ cache trong cache name `product` để đảm bảo dữ liệu mới được load lại.

### Chi tiết sản phẩm

File: [ChiTietSanPhamService.java](../src/main/java/com/vn/shopping/service/ChiTietSanPhamService.java)

Đã cache các API đọc dữ liệu:

- `findByIdDTO(long id)` dùng cache key `by_id_<id>`.
- `findAllDTO()` dùng cache key `all`.
- `findBySanPhamIdDTO(long sanPhamId)` dùng cache key `by_product_<sanPhamId>`.

Đã xóa cache khi dữ liệu thay đổi:

- `create(...)`
- `createChiTietSanPham(...)`
- `updateChiTietSanPham(...)`
- `update(...)`
- `delete(...)`

Các thao tác này đều xóa toàn bộ cache trong cache name `product_detail`.

## Vì sao trước đó bị lỗi serialize

Lỗi `Cannot serialize` thường xảy ra khi Redis cố lưu object Java nhưng serializer mặc định không phù hợp với kiểu dữ liệu trả về.

Giải pháp đã áp dụng:

- Chuyển value serializer sang JSON serializer.
- Giữ key ở dạng chuỗi.
- Cache các DTO thay vì entity thô để giảm rủi ro với lazy loading và proxy của JPA.

## Cách hoạt động thực tế

1. Request lần đầu gọi API sản phẩm hoặc chi tiết sản phẩm.
2. Service đọc từ database và ghi kết quả vào Redis.
3. Các request sau lấy trực tiếp từ Redis nên nhanh hơn.
4. Khi có thêm/sửa/xóa sản phẩm hoặc chi tiết sản phẩm, cache liên quan bị xóa.
5. Request tiếp theo sẽ load lại dữ liệu mới từ database và cache lại.

## Lưu ý vận hành

- Nếu dữ liệu thay đổi nhưng cache không được xóa, frontend có thể thấy dữ liệu cũ.
- Nếu sau này thêm API mới liên quan đến sản phẩm, nên cân nhắc dùng chung cache name `product` hoặc `product_detail` và evict cùng logic.
- Với dữ liệu cần đồng bộ tức thời, có thể giảm TTL hoặc xóa cache theo key cụ thể thay vì xóa toàn bộ cache name.
