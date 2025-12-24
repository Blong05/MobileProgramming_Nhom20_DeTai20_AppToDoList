package com.mobileprogramming.nhom20.todolistapp.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mobileprogramming.nhom20.todolistapp.data.local.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task); // Thêm một task mới

    @Update
    void update(Task task); // Cập nhật task (ví dụ: đổi tên, đổi hạn chót)

    @Delete
    void delete(Task task); // Xóa task

    // Sửa câu Query cũ thành thế này:
    @Query("SELECT * FROM tasks ORDER BY is_completed ASC, due_date ASC")
    LiveData<List<Task>> getAllTasks(); // Lấy tất cả task, ưu tiên cái nào chưa xong và sắp hết hạn lên trước

    @Query("SELECT * FROM tasks WHERE category_id = :catId")
    LiveData<List<Task>> getTasksByCategory(int catId); // Lọc task theo danh mục
}