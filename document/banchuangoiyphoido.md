# TÀI LIỆU MÔ TẢ API FRONTEND

# Chức năng gợi ý phối đồ

## 1. Lấy ID tiến trình

### Endpoint

```text
https://walk-outmatch-umpire.ngrok-free.dev/api/v1/outfits/request-id
```

### Input

API này không cần truyền dữ liệu đầu vào.

```json
{}
```

### Output

```json
{
  "request_id": "3e3a80060de14d5090d8f041c8bd36e3",
  "status": "created",
  "stage": "created",
  "progress": 0,
  "result": null,
  "error": null,
  "started_at": 1779806863.5116608,
  "updated_at": 1779806863.5116608,
  "finished_at": null
}
```

### Tham số cần quan tâm

| Tham số      | Kiểu dữ liệu | Mô tả                                                                                         |
| ------------ | ------------ | --------------------------------------------------------------------------------------------- |
| `request_id` | `string`     | ID tiến trình được tạo ra. Frontend chỉ cần lấy giá trị này để sử dụng cho các API tiếp theo. |

### Lưu ý

Frontend chỉ cần lấy `request_id`.  
Các trường còn lại không cần xử lý ở API này.

---

## 2. Gợi ý phối đồ

### Endpoint

```text
https://walk-outmatch-umpire.ngrok-free.dev/api/v1/outfits/
```

### Input

```json
{
  "request_id": "2b354f5e4c6a402baee9c5d8858722f2",
  "user_query": "Toi la nam, cao 1m75, nang 68kg, tim mot bo do di choi nang dong."
}
```

### Giải thích input

| Tham số      | Kiểu dữ liệu | Bắt buộc | Mô tả                                              |
| ------------ | ------------ | -------- | -------------------------------------------------- |
| `request_id` | `string`     | Có       | ID tiến trình đã lấy từ API lấy ID tiến trình.     |
| `user_query` | `string`     | Có       | Nội dung yêu cầu gợi ý phối đồ do người dùng nhập. |

### Output

```json
{
  "natural_response": "Dưới đây là các bộ đồ hiện đại, năng động và cá tính phù hợp với vóc dáng và phong cách bạn mong muốn. Mời bạn tham khảo!",
  "outfit_combos": [
    {
      "combo_name": "Streetwear Năng Động",
      "items": [
        {
          "id": "SP005",
          "name": "Áo thun nam form rộng bụi bặm",
          "price": "250000",
          "reason": "Áo form oversized màu đen cá tính, tôn dáng thoải mái và phù hợp phong cách streetwear hiện đại."
        },
        {
          "id": "SP018",
          "name": "Quần jeans đen nam ống suông",
          "price": "420000",
          "reason": "Quần ống suông màu đen dễ phối, tạo line dáng dài và hợp trend phom rộng."
        },
        {
          "id": "SP024",
          "name": "Giày sneaker chunky đen",
          "price": "950000",
          "reason": "Giày đế bự màu đen tăng chiều cao, hoàn thiện phong cách hầm hố, hiện đại."
        }
      ],
      "size_advice": "Áo: Chọn size XL (vì chiều cao 1m75 và cân nặng 68kg phù hợp size XL theo bảng; ưu tiên form rộng). Quần: Chọn size 32 (phù hợp cân nặng 68kg). Giày: Chọn size 42 (phù hợp chiều dài bàn chân khoảng 25.6–26.0cm)."
    },
    {
      "combo_name": "Urban Monochrome Minimal",
      "items": [
        {
          "id": "SP006",
          "name": "Áo thun basic trắng unisex",
          "price": "150000",
          "reason": "Áo thun trơn form rộng màu trắng tạo nền sạch, phù hợp phong cách tối giản hiện đại."
        },
        {
          "id": "SP018",
          "name": "Quần jeans đen nam ống suông",
          "price": "420000",
          "reason": "Quần đen phối cùng áo trắng tạo độ tương phản tinh tế, dễ mix theo xu hướng monochrome."
        },
        {
          "id": "SP023",
          "name": "Giày sneaker trắng đi dạo",
          "price": "800000",
          "reason": "Giày trắng hoàn thiện tổng thể sáng sảnh, năng động và thoải mái cho hoạt động ngày dài."
        }
      ],
      "size_advice": "Áo: Chọn size XL (phù hợp chiều cao 1m75 và form rộng). Quần: Chọn size 32 (dựa trên cân nặng 68kg). Giày: Chọn size 42 (phù hợp chiều dài bàn chân 25.6–26.0cm)."
    }
  ]
}
```

### Giải thích output

| Tham số            | Kiểu dữ liệu | Mô tả                                           |
| ------------------ | ------------ | ----------------------------------------------- |
| `natural_response` | `string`     | Câu trả lời tự nhiên từ hệ thống gợi ý phối đồ. |
| `outfit_combos`    | `array`      | Danh sách các combo phối đồ được gợi ý.         |

### Giải thích `outfit_combos`

| Tham số       | Kiểu dữ liệu | Mô tả                                         |
| ------------- | ------------ | --------------------------------------------- |
| `combo_name`  | `string`     | Tên combo phối đồ.                            |
| `items`       | `array`      | Danh sách sản phẩm thuộc combo.               |
| `size_advice` | `string`     | Gợi ý chọn size cho các sản phẩm trong combo. |

### Giải thích `items`

| Tham số  | Kiểu dữ liệu | Mô tả                                  |
| -------- | ------------ | -------------------------------------- |
| `id`     | `string`     | Mã sản phẩm.                           |
| `name`   | `string`     | Tên sản phẩm.                          |
| `price`  | `string`     | Giá sản phẩm.                          |
| `reason` | `string`     | Lý do sản phẩm được gợi ý trong combo. |

---

## 3. Lấy tiến độ

### Endpoint

```text
https://walk-outmatch-umpire.ngrok-free.dev/api/v1/outfits/progress/{request-id}
```

### Input

```text
request_id
```

Trong endpoint, thay `{request-id}` bằng `request_id` của tiến trình cần lấy tiến độ.

### Output

```json
{
  "request_id": "1f8f7eed7c544da1857bd723a95a7768",
  "status": "running",
  "stage": "planner",
  "progress": 10,
  "result": null,
  "error": null,
  "started_at": 1779807260.3239052,
  "updated_at": 1779807269.0823839,
  "finished_at": null
}
```

### Tham số cần quan tâm

| Tham số    | Kiểu dữ liệu | Mô tả                                                                    |
| ---------- | ------------ | ------------------------------------------------------------------------ |
| `progress` | `number`     | Tiến độ xử lý của tiến trình. Frontend chỉ cần quan tâm đến giá trị này. |

### Lưu ý

Frontend chỉ cần lấy `progress`.  
Các trường còn lại không cần xử lý ở API này.

---

## 4. Thoát tiến trình / Hủy gợi ý

### Endpoint

```text
https://walk-outmatch-umpire.ngrok-free.dev/api/v1/outfits/cancel/{request-id}
```

### Input

```text
request_id
```

Trong endpoint, thay `{request-id}` bằng `request_id` của tiến trình cần hủy.

### Output

```json
{
  "request_id": "1f8f7eed7c544da1857bd723a95a7768",
  "status": "cancelled",
  "stage": "cancelled",
  "progress": 0,
  "result": null,
  "error": null,
  "started_at": 1779807260.3239052,
  "updated_at": 1779807284.314756,
  "finished_at": 1779807284.314756
}
```

### Tham số cần quan tâm

| Tham số  | Kiểu dữ liệu | Mô tả                                                                         |
| -------- | ------------ | ----------------------------------------------------------------------------- |
| `status` | `string`     | Trạng thái sau khi hủy tiến trình. Frontend chỉ cần quan tâm đến giá trị này. |

### Lưu ý

Frontend chỉ cần kiểm tra `status`.  
Nếu `status` là `cancelled` thì tiến trình đã được hủy.

He thong chi con 4 API:

1. `POST /api/v1/outfits/request-id`
   - Tao `request_id` ngau nhien, khong trung.
2. `POST /api/v1/outfits/`
   - Nhan `request_id` va `user_query`.
   - Chay pipeline va tra ve goi y phoi do cuoi cung.
3. `GET /api/v1/outfits/progress/{request_id}`
   - Tra ve `status`, `stage`, `progress`, `result`, `error`.
4. `POST /api/v1/outfits/cancel/{request_id}`
   - Huy request dang chay theo `request_id`.
