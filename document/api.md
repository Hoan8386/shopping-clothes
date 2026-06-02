# Frontend API README

Tài liệu này dùng cho frontend tích hợp các API: kiểm tra ảnh có người, thử đồ ảo, lấy tiến trình, hủy tiến trình, lấy ảnh kết quả, xóa ảnh kết quả và gợi ý phối đồ.

---

## 1. Base URL

### Local

```txt
https://walk-outmatch-umpire.ngrok-free.dev
```

### API Prefix

```txt
/api/v1
```

### Ví dụ cấu hình frontend

```ts
export const BASE_URL = "http://127.0.0.1:8001";
export const API_PREFIX = "/api/v1";
export const API_BASE_URL = `${BASE_URL}${API_PREFIX}`;
export const API_KEY = "YOUR_API_KEY";
```

---

## 2. Authentication

Các API xử lý chính có dùng API key. Frontend gửi API key qua header.

```ts
headers: {
  "x-api-key": API_KEY
}
```

Lưu ý: khi gửi `FormData`, không tự set `Content-Type`. Trình duyệt sẽ tự sinh `multipart/form-data` kèm boundary.

```ts
await fetch(url, {
  method: "POST",
  headers: {
    "x-api-key": API_KEY,
  },
  body: formData,
});
```

---

## 3. Categories

| Category     | Description                            |
| ------------ | -------------------------------------- |
| `tops`       | Upper body: t-shirts, blouses, jackets |
| `bottoms`    | Lower body: pants, skirts, shorts      |
| `one-pieces` | Full body: dresses, jumpsuits          |

---

## 4. Tổng quan API

| Nhóm              | Method | Endpoint                                       | Auth        | Mô tả                                                         |
| ----------------- | ------ | ---------------------------------------------- | ----------- | ------------------------------------------------------------- |
| Health Check      | GET    | `/`                                            | Không       | Kiểm tra server đang chạy                                     |
| Check Existence   | POST   | `/api/v1/check/person`                         | Có          | Kiểm tra ảnh có người hay không                               |
| Virtual Try-On    | POST   | `/api/v1/virtual-try-on/request-id`            | Có          | Tạo ID tiến trình thử đồ ảo                                   |
| Virtual Try-On    | POST   | `/api/v1/virtual-try-on/process`               | Có          | Chạy thử đồ ảo, truyền `request_id`, trả kết quả khi hoàn tất |
| Virtual Try-On    | GET    | `/api/v1/virtual-try-on/progress/{request_id}` | Có          | Lấy tiến độ xử lý, không trả ảnh                              |
| Virtual Try-On    | POST   | `/api/v1/virtual-try-on/cancel/{request_id}`   | Có          | Hủy tiến trình thử đồ ảo                                      |
| Virtual Try-On    | GET    | `/api/v1/virtual-try-on/result/{file_name}`    | Không       | Xem ảnh kết quả                                               |
| Virtual Try-On    | DELETE | `/api/v1/virtual-try-on/delete/{file_name}`    | Có          | Xóa ảnh kết quả                                               |
| Outfit Suggestion | POST   | `/api/v1/outfits/request-id`                   | Tùy service | Tạo ID tiến trình gợi ý phối đồ                               |
| Outfit Suggestion | POST   | `/api/v1/outfits/`                             | Tùy service | Chạy gợi ý phối đồ                                            |
| Outfit Suggestion | GET    | `/api/v1/outfits/progress/{request_id}`        | Tùy service | Lấy tiến độ gợi ý phối đồ                                     |
| Outfit Suggestion | POST   | `/api/v1/outfits/cancel/{request_id}`          | Tùy service | Hủy tiến trình gợi ý phối đồ                                  |

---

# 5. Health Check

## GET `/`

API public để kiểm tra server đang hoạt động.

### Request

```http
GET /
```

### Response 200

```json
{
  "message": "Server đang hoạt động tốt!"
}
```

### Fetch example

```ts
export async function healthCheck() {
  const res = await fetch(`${BASE_URL}/`);
  return await res.json();
}
```

---

# 6. Check Existence API

API kiểm tra ảnh có người hay không. Nên gọi API này trước khi cho người dùng chạy thử đồ ảo để tránh ảnh đầu vào không hợp lệ.

## POST `/api/v1/check/person`

### Content-Type

```txt
multipart/form-data
```

### Headers

```ts
{
  "x-api-key": API_KEY
}
```

### Form Data

| Field   | Type | Required | Description                                          |
| ------- | ---- | -------- | ---------------------------------------------------- |
| `image` | File | Có       | Ảnh cần kiểm tra. Chỉ hỗ trợ `.jpg`, `.jpeg`, `.png` |

### Request example

```ts
export async function checkPerson(imageFile: File) {
  const formData = new FormData();
  formData.append("image", imageFile);

  const res = await fetch(`${API_BASE_URL}/check/person`, {
    method: "POST",
    headers: {
      "x-api-key": API_KEY,
    },
    body: formData,
  });

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200

```json
{
  "status": "success",
  "message": "Kiểm tra hoàn tất",
  "data": {
    "has_person": true
  }
}
```

### Error 400

```json
{
  "detail": "Chỉ hỗ trợ upload file ảnh định dạng .jpg, .jpeg hoặc .png"
}
```

### Frontend usage

```ts
const check = await checkPerson(personFile);

if (!check.data.has_person) {
  alert("Ảnh người không hợp lệ. Vui lòng chọn ảnh có người.");
  return;
}
```

---

# 7. Virtual Try-On API

Luồng chuẩn bên frontend:

1. Gọi API tạo `request_id`.
2. Gọi API `/process` và truyền `request_id` vào `FormData`.
3. Trong khi `/process` đang chạy, gọi `/progress/{request_id}` mỗi 500ms hoặc 1000ms để cập nhật thanh tiến trình.
4. Nếu người dùng bấm hủy, gọi `/cancel/{request_id}`.
5. Chỉ lấy `image_url` từ response của `/process`, không lấy từ API progress.

---

## 7.1. Tạo ID tiến trình

## POST `/api/v1/virtual-try-on/request-id`

### Headers

```ts
{
  "x-api-key": API_KEY
}
```

### Request Body

Không có.

### Request example

```ts
export async function createTryOnRequestId() {
  const res = await fetch(`${API_BASE_URL}/virtual-try-on/request-id`, {
    method: "POST",
    headers: {
      "x-api-key": API_KEY,
    },
  });

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200

```json
{
  "request_id": "c75039832ac44850ae113654a7b31acd",
  "status": "created",
  "stage": "created",
  "progress": 0,
  "message": "Đã tạo request_id thử đồ ảo.",
  "error": null,
  "started_at": null,
  "updated_at": 1779870000.123,
  "finished_at": null
}
```

---

## 7.2. Chạy thử đồ ảo

## POST `/api/v1/virtual-try-on/process`

API này chạy cho tới khi có kết quả. Khi hoàn tất, API trả `image_url` và `result`.

### Content-Type

```txt
multipart/form-data
```

### Headers

```ts
{
  "x-api-key": API_KEY
}
```

### Form Data

| Field         | Type   | Required | Description                                         |
| ------------- | ------ | -------- | --------------------------------------------------- |
| `request_id`  | string | Có       | ID lấy từ API `/request-id`                         |
| `person_img`  | File   | Có       | Ảnh người                                           |
| `garment_img` | File   | Có       | Ảnh quần/áo                                         |
| `category`    | string | Không    | `tops`, `bottoms`, `one-pieces`. Mặc định `bottoms` |

### Request example

```ts
export async function processVirtualTryOn(params: {
  requestId: string;
  personFile: File;
  garmentFile: File;
  category: "tops" | "bottoms" | "one-pieces";
}) {
  const formData = new FormData();
  formData.append("request_id", params.requestId);
  formData.append("person_img", params.personFile);
  formData.append("garment_img", params.garmentFile);
  formData.append("category", params.category);

  const res = await fetch(`${API_BASE_URL}/virtual-try-on/process`, {
    method: "POST",
    headers: {
      "x-api-key": API_KEY,
    },
    body: formData,
  });

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200 - completed

```json
{
  "request_id": "c75039832ac44850ae113654a7b31acd",
  "status": "completed",
  "stage": "completed",
  "progress": 100,
  "message": "Thử đồ ảo hoàn tất.",
  "error": null,
  "image_url": "http://127.0.0.1:8001/api/v1/virtual-try-on/result/c75039832ac44850ae113654a7b31acd.jpg",
  "result": {
    "image_url": "http://127.0.0.1:8001/api/v1/virtual-try-on/result/c75039832ac44850ae113654a7b31acd.jpg",
    "file_name": "c75039832ac44850ae113654a7b31acd.jpg"
  },
  "started_at": 1779870000.123,
  "updated_at": 1779870015.456,
  "finished_at": 1779870015.456
}
```

### Error 499 - cancelled

```json
{
  "detail": {
    "request_id": "c75039832ac44850ae113654a7b31acd",
    "status": "cancelled",
    "stage": "cancelled",
    "progress": 45,
    "message": "Tiến trình thử đồ ảo đã bị hủy.",
    "error": null
  }
}
```

---

## 7.3. Lấy tiến độ thử đồ ảo

## GET `/api/v1/virtual-try-on/progress/{request_id}`

API này chỉ lấy tiến độ, không trả ảnh, không trả `image_url`, không trả `result`.

### Headers

```ts
{
  "x-api-key": API_KEY
}
```

### Request example

```ts
export async function getTryOnProgress(requestId: string) {
  const res = await fetch(
    `${API_BASE_URL}/virtual-try-on/progress/${requestId}`,
    {
      method: "GET",
      headers: {
        "x-api-key": API_KEY,
      },
    },
  );

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200

```json
{
  "request_id": "c75039832ac44850ae113654a7b31acd",
  "status": "running",
  "stage": "sampling",
  "progress": 73,
  "message": "AI đang xử lý sampling 6/10.",
  "error": null,
  "started_at": 1779870000.123,
  "updated_at": 1779870010.456,
  "finished_at": null
}
```

### Status values

| Status      | Ý nghĩa                        |
| ----------- | ------------------------------ |
| `created`   | Đã tạo `request_id`, chưa chạy |
| `running`   | Đang xử lý                     |
| `completed` | Hoàn tất                       |
| `error`     | Có lỗi                         |
| `cancelled` | Đã hủy                         |

---

## 7.4. Hủy tiến trình thử đồ ảo

## POST `/api/v1/virtual-try-on/cancel/{request_id}`

### Headers

```ts
{
  "x-api-key": API_KEY
}
```

### Request example

```ts
export async function cancelTryOn(requestId: string) {
  const res = await fetch(
    `${API_BASE_URL}/virtual-try-on/cancel/${requestId}`,
    {
      method: "POST",
      headers: {
        "x-api-key": API_KEY,
      },
    },
  );

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200

```json
{
  "request_id": "c75039832ac44850ae113654a7b31acd",
  "status": "cancelled",
  "stage": "cancelled",
  "progress": 73,
  "message": "Người dùng đã yêu cầu hủy tiến trình thử đồ ảo.",
  "error": null,
  "started_at": 1779870000.123,
  "updated_at": 1779870011.0,
  "finished_at": 1779870011.0
}
```

---

## 7.5. Lấy ảnh kết quả

## GET `/api/v1/virtual-try-on/result/{file_name}`

API trả trực tiếp ảnh JPEG.

### Auth

Không yêu cầu API key.

### Request example

```ts
const imageUrl = `${API_BASE_URL}/virtual-try-on/result/${fileName}`;
```

### HTML example

```tsx
<img src={imageUrl} alt="Virtual try-on result" />
```

### Response 200

```txt
Content-Type: image/jpeg
```

### Error 404

```json
{
  "detail": "Không tìm thấy hình ảnh này trên máy chủ hoặc ảnh đã bị xóa."
}
```

---

## 7.6. Xóa ảnh kết quả

## DELETE `/api/v1/virtual-try-on/delete/{file_name}`

### Headers

```ts
{
  "x-api-key": API_KEY
}
```

### Request example

```ts
export async function deleteTryOnImage(fileName: string) {
  const res = await fetch(`${API_BASE_URL}/virtual-try-on/delete/${fileName}`, {
    method: "DELETE",
    headers: {
      "x-api-key": API_KEY,
    },
  });

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200

```json
{
  "status": "success",
  "message": "Đã xóa file ảnh thành công.",
  "deleted_file": "c75039832ac44850ae113654a7b31acd.jpg"
}
```

---

# 8. Full frontend flow: Virtual Try-On

```ts
type TryOnCategory = "tops" | "bottoms" | "one-pieces";

let pollingTimer: ReturnType<typeof setInterval> | null = null;

export async function runVirtualTryOnFlow(params: {
  personFile: File;
  garmentFile: File;
  category: TryOnCategory;
  onProgress?: (progress: number, status: string, message?: string) => void;
}) {
  // 1. Tạo request_id
  const requestData = await createTryOnRequestId();
  const requestId = requestData.request_id;

  // 2. Polling tiến độ
  pollingTimer = setInterval(async () => {
    try {
      const progressData = await getTryOnProgress(requestId);

      params.onProgress?.(
        progressData.progress,
        progressData.status,
        progressData.message,
      );

      if (
        progressData.status === "completed" ||
        progressData.status === "cancelled" ||
        progressData.status === "error"
      ) {
        if (pollingTimer) {
          clearInterval(pollingTimer);
          pollingTimer = null;
        }
      }
    } catch (error) {
      console.error("Lỗi lấy tiến độ thử đồ:", error);
    }
  }, 1000);

  try {
    // 3. Chạy thử đồ, API này chờ tới khi có kết quả
    const result = await processVirtualTryOn({
      requestId,
      personFile: params.personFile,
      garmentFile: params.garmentFile,
      category: params.category,
    });

    if (pollingTimer) {
      clearInterval(pollingTimer);
      pollingTimer = null;
    }

    params.onProgress?.(100, "completed", "Thử đồ ảo hoàn tất.");

    return result;
  } catch (error) {
    if (pollingTimer) {
      clearInterval(pollingTimer);
      pollingTimer = null;
    }

    throw error;
  }
}

export async function cancelVirtualTryOnFlow(requestId: string) {
  if (pollingTimer) {
    clearInterval(pollingTimer);
    pollingTimer = null;
  }

  return await cancelTryOn(requestId);
}
```

---

# 9. Outfit Suggestion API

Nhóm API này dùng cho phần gợi ý phối đồ. Nếu service Outfit Suggestion chạy riêng, dùng `BASE_URL` theo service đó.

## 9.1. Tạo request id phối đồ

## POST `/api/v1/outfits/request-id`

### Request Body

Không có.

### Request example

```ts
export async function createOutfitRequestId() {
  const res = await fetch(`${BASE_URL}/api/v1/outfits/request-id`, {
    method: "POST",
  });

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200

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

---

## 9.2. Chạy gợi ý phối đồ

## POST `/api/v1/outfits/`

### Content-Type

```txt
application/json
```

### Body

| Field        | Type   | Required | Description                               |
| ------------ | ------ | -------- | ----------------------------------------- |
| `request_id` | string | Có       | ID lấy từ `/request-id`                   |
| `user_query` | string | Có       | Nội dung người dùng nhập để gợi ý phối đồ |

### Request example

```ts
export async function suggestOutfit(params: {
  requestId: string;
  userQuery: string;
}) {
  const res = await fetch(`${BASE_URL}/api/v1/outfits/`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      request_id: params.requestId,
      user_query: params.userQuery,
    }),
  });

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Body example

```json
{
  "request_id": "3e3a80060de14d5090d8f041c8bd36e3",
  "user_query": "Tôi là nam, cao 1m75, nặng 68kg, muốn phối đồ đi chơi cuối tuần."
}
```

---

## 9.3. Lấy tiến độ gợi ý phối đồ

## GET `/api/v1/outfits/progress/{request_id}`

### Request example

```ts
export async function getOutfitProgress(requestId: string) {
  const res = await fetch(`${BASE_URL}/api/v1/outfits/progress/${requestId}`);

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200

```json
{
  "request_id": "3e3a80060de14d5090d8f041c8bd36e3",
  "status": "running",
  "stage": "generating",
  "progress": 60,
  "result": null,
  "error": null,
  "started_at": 1779806863.5116608,
  "updated_at": 1779806880.1,
  "finished_at": null
}
```

---

## 9.4. Hủy tiến trình gợi ý phối đồ

## POST `/api/v1/outfits/cancel/{request_id}`

### Request example

```ts
export async function cancelOutfitSuggestion(requestId: string) {
  const res = await fetch(`${BASE_URL}/api/v1/outfits/cancel/${requestId}`, {
    method: "POST",
  });

  if (!res.ok) {
    throw await res.json();
  }

  return await res.json();
}
```

### Response 200

```json
{
  "request_id": "3e3a80060de14d5090d8f041c8bd36e3",
  "status": "cancelled",
  "stage": "cancelled",
  "progress": 40,
  "result": null,
  "error": null,
  "started_at": 1779806863.5116608,
  "updated_at": 1779806880.1,
  "finished_at": 1779806880.1
}
```

---

# 10. Frontend notes

## Không lấy ảnh từ API progress

Với Virtual Try-On:

- API `/process` trả `image_url` và `result`.
- API `/progress/{request_id}` chỉ trả tiến độ.
- Khi `progress` đạt `100`, frontend vẫn nên đợi response của `/process` để lấy `image_url`.

## Không tái sử dụng request_id

Mỗi lần chạy thử đồ hoặc phối đồ nên tạo `request_id` mới. Không dùng lại request đã `completed`, `cancelled` hoặc `error`.

## Polling interval đề xuất

```txt
500ms - 1000ms
```

Nếu giao diện nhẹ, dùng 500ms. Nếu muốn giảm tải server, dùng 1000ms.

## Khi người dùng rời trang

Nên gọi API cancel nếu tiến trình vẫn đang chạy.

```ts
window.addEventListener("beforeunload", () => {
  if (currentRequestId) {
    navigator.sendBeacon(
      `${API_BASE_URL}/virtual-try-on/cancel/${currentRequestId}`,
    );
  }
});
```

Lưu ý: `sendBeacon` mặc định gửi `POST`, nhưng khó custom header API key. Nếu backend bắt buộc API key, nên dùng `fetch` trong cleanup của framework khi còn có thể.

---

# 11. Error handling chung

## 400 Bad Request

Input không hợp lệ, ví dụ ảnh sai định dạng hoặc file lỗi.

```json
{
  "detail": "Lỗi đọc file ảnh 'person_img': cannot identify image file"
}
```

## 404 Not Found

Không tìm thấy `request_id` hoặc file ảnh.

```json
{
  "detail": "Không tìm thấy request_id 'abc'."
}
```

## 409 Conflict

`request_id` không thể chạy lại vì đang chạy hoặc đã hoàn tất.

```json
{
  "detail": "request_id 'abc' đang được xử lý."
}
```

## 499 Client Closed Request / Cancelled

Tiến trình bị hủy.

```json
{
  "detail": {
    "request_id": "abc",
    "status": "cancelled",
    "stage": "cancelled",
    "progress": 50,
    "message": "Tiến trình thử đồ ảo đã bị hủy.",
    "error": null
  }
}
```

## 500 Internal Server Error

Lỗi server hoặc lỗi AI model.

```json
{
  "detail": "Lỗi xử lý thử đồ ảo: AI Model chưa được khởi tạo thành công."
}
```

---

# 12. Gợi ý TypeScript types

```ts
export type TryOnCategory = "tops" | "bottoms" | "one-pieces";

export type ProgressStatus =
  | "created"
  | "queued"
  | "running"
  | "completed"
  | "error"
  | "cancelled";

export interface ProgressResponse {
  request_id: string;
  status: ProgressStatus;
  stage: string;
  progress: number;
  message?: string | null;
  error?: string | null;
  started_at?: number | null;
  updated_at?: number | null;
  finished_at?: number | null;
}

export interface TryOnProcessResponse extends ProgressResponse {
  image_url: string;
  result: {
    image_url: string;
    file_name: string;
  };
}

export interface CheckPersonResponse {
  status: "success";
  message: string;
  data: {
    has_person: boolean;
  };
}
```

---

# 13. Checklist tích hợp nhanh

- [ ] Cấu hình `BASE_URL`.
- [ ] Cấu hình `API_KEY`.
- [ ] Gọi `/check/person` để kiểm tra ảnh người trước khi thử đồ.
- [ ] Gọi `/virtual-try-on/request-id` để lấy `request_id`.
- [ ] Gọi `/virtual-try-on/process` với `FormData`.
- [ ] Poll `/virtual-try-on/progress/{request_id}` để cập nhật UI.
- [ ] Lấy `image_url` từ response `/process`.
- [ ] Dùng `image_url` để hiển thị ảnh kết quả.
- [ ] Gọi `/virtual-try-on/cancel/{request_id}` khi người dùng bấm hủy.
- [ ] Gọi `/virtual-try-on/delete/{file_name}` nếu cần xóa ảnh.
