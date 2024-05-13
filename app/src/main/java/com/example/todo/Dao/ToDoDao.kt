package com.example.todo.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.todo.model.ToDoModel

class ToDoDao(context: Context) : Dao {
    private val dbHelper = DatabaseHelper(context)

    override fun insertTask(model: ToDoModel) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_2, model.task)
            put(DatabaseHelper.COL_3, 0) // Assuming new tasks are not completed
            put(DatabaseHelper.COL_4, model.description)
            put(DatabaseHelper.COL_5, model.time)
            put(DatabaseHelper.COL_6, model.priority)
        }
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
        db.close()
    }

    override fun updateTask(id: Int, task: String, description: String, time: String, priority: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COL_2, task)
            put(DatabaseHelper.COL_4, description)
            put(DatabaseHelper.COL_5, time)
            put(DatabaseHelper.COL_6, priority)
        }
        db.update(DatabaseHelper.TABLE_NAME, values, "${DatabaseHelper.COL_1}=?", arrayOf(id.toString()))
        db.close()
    }

    override fun updateStatus(id: Int, status: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(DatabaseHelper.COL_3, status)
        db.update(DatabaseHelper.TABLE_NAME, values, "${DatabaseHelper.COL_1}=?", arrayOf(id.toString()))
        db.close()
    }

    override fun deleteTask(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_NAME, "${DatabaseHelper.COL_1}=?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    override fun getAllTasks(): List<ToDoModel> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null)
        val modelList = ArrayList<ToDoModel>()

        with(cursor) {
            while (moveToNext()) {
                val task = ToDoModel(
                    id = getInt(getColumnIndex(DatabaseHelper.COL_1)),
                    task = getString(getColumnIndex(DatabaseHelper.COL_2)),
                    status = getInt(getColumnIndex(DatabaseHelper.COL_3)),
                    description = getString(getColumnIndex(DatabaseHelper.COL_4)),
                    time = getString(getColumnIndex(DatabaseHelper.COL_5)),
                    priority = getString(getColumnIndex(DatabaseHelper.COL_6))
                )
                modelList.add(task)
            }
            close()
        }
        db.close()
        return modelList
    }
}
