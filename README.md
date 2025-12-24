# Đồ Án Môn Lập Trình Di Động - Nhóm 20

## 📝 Đề tài: Ứng dụng Quản lý công việc (App ToDoList)

### 👨‍💻 Thành viên thực hiện
* **Nguyễn Minh Lực** - MSSV: 21110540
* **Nguyễn Tuấn Huy** - MSSV: 22110334
* **Nguyễn Thái Hưng** - MSSV: 23162035
* **Nguyễn Hồng Bảo Long** - MSSV: 23162053

---

### 🚀 Giới thiệu dự án
Ứng dụng giúp người dùng quản lý danh sách công việc hằng ngày hiệu quả, hỗ trợ nhắc nhở thông minh và lưu trữ dữ liệu bền vững theo mô hình kiến trúc **MVVM**.

### 🛠 Công nghệ & Kỹ thuật sử dụng
* **Ngôn ngữ:** Java.
* **Kiến trúc:** MVVM (Model - View - ViewModel) giúp tách biệt logic và giao diện.
* **Database:** **Room Persistence Library** (SQLite) để quản lý dữ liệu cục bộ.
* **Background Task:** **AlarmManager** & **BroadcastReceiver** xử lý nhắc nhở chạy ngầm.
* **UI Components:** RecyclerView, CardView, BottomSheetDialog, Material Design 3.
* **Optimization:** **DiffUtil** dùng để tính toán và cập nhật danh sách mượt mà, tối ưu hiệu năng.

---

### ✅ Các tính năng đã hoàn thiện (Phần 1)

#### 1. Quản lý công việc (CRUD)
* **Thêm mới:** Sử dụng BottomSheet trượt để nhập liệu nhanh chóng.
* **Hiển thị:** Danh sách Task hiển thị chuyên nghiệp với CardView.
* **Cập nhật:** Tích hợp Checkbox để đánh dấu hoàn thành kèm hiệu ứng gạch ngang chữ (Strike-through).
* **Xóa:** Hỗ trợ thao tác vuốt ngang (Swipe to Delete) để xóa công việc cực nhanh.

#### 2. Hệ thống nhắc nhở thông minh
* **Đặt lịch báo thức:** Cho phép chọn Ngày và Giờ chi tiết qua Date & Time Picker.
* **Thông báo đẩy (Push Notification):** Gửi thông báo đến thanh trạng thái điện thoại khi đến hạn công việc, ngay cả khi app đang đóng.
* **Notification Channel:** Thiết lập kênh thông báo riêng biệt, tương thích tốt với Android 8.0 trở lên.

#### 3. Xử lý logic & Hiển thị nâng cao
* **Sắp xếp tự động:** Ưu tiên hiển thị các việc chưa hoàn thành lên trên và sắp xếp theo thời gian hạn gần nhất.
* **Cảnh báo quá hạn:** Tự động chuyển đổi màu sắc thời gian sang **màu đỏ** nếu công việc chưa hoàn thành mà đã quá giờ hẹn.

---

### 📂 Cấu trúc thư mục
* `SourceCode/`: Chứa mã nguồn Android Studio (Java).
    * `data/`: Chứa các lớp Entity, DAO và AppDatabase (Room).
    * `ui/`: Chứa Activity, ViewModel và Adapter xử lý giao diện.
* `Design/`: Chứa hình ảnh giao diện, file thiết kế và sơ đồ Database.

---

### 🔨 Cách cài đặt và chạy thử
1. Clone dự án về máy.
2. Mở bằng **Android Studio (Koala hoặc mới hơn)**.
3. Chạy trên máy ảo hoặc máy thật (Yêu cầu cấp quyền **Post Notifications** và **Exact Alarm** trên Android 13+).