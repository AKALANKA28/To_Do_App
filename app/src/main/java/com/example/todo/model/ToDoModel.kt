package com.example.todo.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
class ToDoModel(
    @PrimaryKey(autoGenerate = true)

    var task: String = "",
    var description: String = "",
    var time: String = "",
    var id: Int = 0,
    var status: Int = 0,
    var priority: String = "",

)
