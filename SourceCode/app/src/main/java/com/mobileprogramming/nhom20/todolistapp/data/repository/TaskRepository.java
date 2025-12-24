package com.mobileprogramming.nhom20.todolistapp.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.mobileprogramming.nhom20.todolistapp.data.local.database.AppDatabase;
import com.mobileprogramming.nhom20.todolistapp.data.local.dao.TaskDao;
import com.mobileprogramming.nhom20.todolistapp.data.local.entities.Task;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    // Room không cho phép chạy lệnh ghi dữ liệu trên Main Thread (luồng chính), nên ta cần Executor để chạy ngầm.
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        executorService.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        executorService.execute(() -> taskDao.delete(task));
    }
}