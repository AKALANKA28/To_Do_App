package com.example.todo.utils

import com.example.todo.model.ToDoModel

interface Dao {
    fun insertTask(model: ToDoModel)
    fun updateTask(id: Int, task: String, description: String, time: String, priority: String)
    fun updateStatus(id: Int, status: Int)
    fun deleteTask(id: Int)
    fun getAllTasks(): List<ToDoModel>
}
