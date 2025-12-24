package com.mobileprogramming.nhom20.todolistapp.data.local.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mobileprogramming.nhom20.todolistapp.data.local.dao.CategoryDao;
import com.mobileprogramming.nhom20.todolistapp.data.local.dao.TaskDao;
import com.mobileprogramming.nhom20.todolistapp.data.local.entities.Category;
import com.mobileprogramming.nhom20.todolistapp.data.local.entities.Task;

// Khai báo các bảng sẽ có trong App. Hiện tại mình có Task và Category.
// Mỗi khi bạn thêm 1 file Entity mới (như Project), bạn phải thêm vào mảng entities này.
@Database(entities = {Task.class, Category.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    // Sau này mình sẽ thêm các DAO vào đây để truy vấn dữ liệu
    public abstract TaskDao taskDao();
    public abstract CategoryDao categoryDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "todo_database")
                            .fallbackToDestructiveMigration() // Tự động xóa data cũ nếu thay đổi cấu trúc bảng ở version sau
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}