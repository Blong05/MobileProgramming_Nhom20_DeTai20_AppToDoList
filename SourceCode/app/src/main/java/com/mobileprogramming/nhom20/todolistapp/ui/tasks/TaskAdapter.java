package com.mobileprogramming.nhom20.todolistapp.ui.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mobileprogramming.nhom20.todolistapp.R;
import com.mobileprogramming.nhom20.todolistapp.data.local.entities.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // Cầu nối để báo về MainActivity khi có thay đổi
    public interface OnItemClickListener {
        void onCheckChanged(Task task);
    }

    private OnItemClickListener listener;
    private List<Task> tasks = new ArrayList<>();

    // Hàm này để MainActivity truyền listener vào
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task currentTask = tasks.get(position);

        holder.tvTitle.setText(currentTask.getTitle());

        // --- SỬA ĐOẠN NÀY ---
        // Chuyển con số Milliseconds thành định dạng dd/MM/yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
        String formattedDate = dateFormat.format(new java.util.Date(currentTask.getDueDate()));
        holder.tvDate.setText("Hạn: " + formattedDate);
        // --------------------

        // Quan trọng: Gỡ bỏ listener cũ trước khi set trạng thái để tránh lỗi lặp vô tận
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(currentTask.isCompleted());

        // Bắt sự kiện khi người dùng bấm tick
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                currentTask.setCompleted(isChecked);
                listener.onCheckChanged(currentTask);
            }
        });

        long currentTime = System.currentTimeMillis();
        // Nếu ngày đến hạn nhỏ hơn ngày hiện tại VÀ chưa hoàn thành
        if (currentTask.getDueDate() < currentTime && !currentTask.isCompleted()) {
            holder.tvDate.setTextColor(android.graphics.Color.RED); // Hiện chữ đỏ cảnh báo
            holder.tvDate.setText("QUÁ HẠN: " + formattedDate);
        } else {
            holder.tvDate.setTextColor(android.graphics.Color.GRAY); // Hiện màu xám bình thường
            holder.tvDate.setText("Hạn: " + formattedDate);
        }

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> newTasks) {
        // DiffUtil sẽ tính toán sự khác biệt giữa danh sách cũ và mới
        androidx.recyclerview.widget.DiffUtil.DiffResult diffResult =
                androidx.recyclerview.widget.DiffUtil.calculateDiff(new androidx.recyclerview.widget.DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return tasks.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newTasks.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        // Kiểm tra xem ID của 2 task có giống nhau không
                        return tasks.get(oldItemPosition).getId() == newTasks.get(newItemPosition).getId();
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        // Kiểm tra xem nội dung bên trong (title, status...) có đổi không
                        Task oldTask = tasks.get(oldItemPosition);
                        Task newTask = newTasks.get(newItemPosition);
                        return oldTask.getTitle().equals(newTask.getTitle()) &&
                                oldTask.isCompleted() == newTask.isCompleted() &&
                                oldTask.getDueDate() == newTask.getDueDate();
                    }
                });

        // Cập nhật lại danh sách dữ liệu
        this.tasks.clear();
        this.tasks.addAll(newTasks);

        // Ra lệnh cho Adapter chạy hiệu ứng thay đổi mượt mà
        diffResult.dispatchUpdatesTo(this);
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private CheckBox checkBox;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvDate = itemView.findViewById(R.id.tvDueDate);
            checkBox = itemView.findViewById(R.id.checkBoxComplete);
        }
    }
}