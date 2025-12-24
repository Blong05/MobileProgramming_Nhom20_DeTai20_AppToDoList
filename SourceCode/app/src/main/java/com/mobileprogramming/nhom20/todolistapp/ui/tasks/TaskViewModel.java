package com.mobileprogramming.nhom20.todolistapp.ui.tasks;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.mobileprogramming.nhom20.todolistapp.data.local.entities.Task;
import com.mobileprogramming.nhom20.todolistapp.data.repository.TaskRepository;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        // Khởi tạo Repository để ViewModel có thể gọi các lệnh lấy/thêm/xóa dữ liệu
        repository = new TaskRepository(application);
        // Lấy danh sách toàn bộ Task dưới dạng LiveData để giao diện tự cập nhật khi có thay đổi
        allTasks = repository.getAllTasks();
    }

    // Hàm trả về danh sách Task để MainActivity "quan sát" (Observe)
    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    // Các hàm trung gian để MainActivity gọi khi người dùng muốn Thêm/Sửa/Xóa
    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }
}