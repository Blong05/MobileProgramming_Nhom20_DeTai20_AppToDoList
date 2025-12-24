package com.mobileprogramming.nhom20.todolistapp.ui.tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobileprogramming.nhom20.todolistapp.R;
import com.mobileprogramming.nhom20.todolistapp.data.local.entities.Task;

public class MainActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private long selectedDate = System.currentTimeMillis();

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "TodoChannel";
            String description = "Kênh nhắc nhở công việc";
            int importance = android.app.NotificationManager.IMPORTANCE_HIGH;
            android.app.NotificationChannel channel = new android.app.NotificationChannel("todo_channel", name, importance);
            channel.setDescription(description);

            android.app.NotificationManager notificationManager = getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new TaskAdapter();
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onCheckChanged(Task task) {
                taskViewModel.update(task);
            }
        });
        recyclerView.setAdapter(adapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            adapter.setTasks(tasks);
        });

        FloatingActionButton fab = findViewById(R.id.fabAddTask);
        fab.setOnClickListener(view -> {
            showAddTaskDialog();
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Task taskToDelete = adapter.getTaskAt(viewHolder.getAdapterPosition());
                taskViewModel.delete(taskToDelete);
                Toast.makeText(MainActivity.this, "Đã xóa công việc", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void showAddTaskDialog() {
        com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog = new com.google.android.material.bottomsheet.BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.add_task, null);
        bottomSheetDialog.setContentView(view);

        android.widget.EditText edtTitle = view.findViewById(R.id.edtTaskTitle);
        android.widget.TextView tvDate = view.findViewById(R.id.tvSelectedDate);
        android.widget.LinearLayout layoutDate = view.findViewById(R.id.layoutSelectDate);
        android.widget.Button btnSave = view.findViewById(R.id.btnSaveTask);

        layoutDate.setOnClickListener(v -> {
            java.util.Calendar calendar = java.util.Calendar.getInstance();

            // 1. Hiện lịch chọn Ngày
            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                calendar.set(java.util.Calendar.YEAR, year);
                calendar.set(java.util.Calendar.MONTH, month);
                calendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                // 2. Sau khi chọn ngày xong, hiện tiếp bảng chọn Giờ
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(this, (view2, hourOfDay, minute) -> {
                    calendar.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(java.util.Calendar.MINUTE, minute);
                    calendar.set(java.util.Calendar.SECOND, 0);

                    selectedDate = calendar.getTimeInMillis(); // Lưu đầy đủ ngày và giờ

                    // Hiển thị lên giao diện cho người dùng thấy
                    String dateTime = dayOfMonth + "/" + (month + 1) + "/" + year + " " + hourOfDay + ":" + String.format("%02d", minute);
                    tvDate.setText(dateTime);

                }, calendar.get(java.util.Calendar.HOUR_OF_DAY), calendar.get(java.util.Calendar.MINUTE), true);

                timePickerDialog.show();

            }, calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString().trim();
            if (!title.isEmpty()) {
                Task newTask = new Task(title, "", selectedDate, 1, 0, 0);

                // 1. Lưu vào Database
                taskViewModel.insert(newTask);

                // 2. Hẹn giờ báo thức (Lệnh mới thêm)
                startAlarm(newTask);

                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(this, "Vui lòng nhập tên!", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.show();
    }

    // --- HÀM HẸN GIỜ MỚI THÊM ---
    private void startAlarm(Task task) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title", task.getTitle());

        // requestCode nên là duy nhất cho mỗi Task để không bị ghi đè
        int requestCode = task.getTitle().hashCode();

        // CHỖ NÀY QUAN TRỌNG: Thêm | PendingIntent.FLAG_IMMUTABLE
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            try {
                // Dùng setExactAndAllowWhileIdle để chạy được cả khi máy đang ngủ
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getDueDate(), pendingIntent);
            } catch (SecurityException e) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    // Mở thẳng trang cài đặt quyền báo thức của App để người dùng bật
                    Intent intentPermission = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intentPermission);
                    Toast.makeText(this, "Hãy bật quyền Alarms & Reminders để nhận thông báo", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}