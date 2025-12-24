package com.mobileprogramming.nhom20.todolistapp.data.local.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "color_code")
    private String colorCode; // Lưu mã màu Hex (ví dụ: #4CAF50)

    @ColumnInfo(name = "icon_name")
    private String iconName; // Lưu tên icon từ resource (ví dụ: ic_work)

    // Constructor
    public Category(String name, String colorCode, String iconName) {
        this.name = name;
        this.colorCode = colorCode;
        this.iconName = iconName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
}